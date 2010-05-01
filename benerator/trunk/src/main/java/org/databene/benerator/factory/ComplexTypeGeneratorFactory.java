/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.benerator.factory;

import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.ComponentDescriptor;
import org.databene.model.data.Entity;
import org.databene.model.data.EntitySource;
import org.databene.model.data.Mode;
import org.databene.model.data.SimpleTypeDescriptor;
import org.databene.model.data.TypeDescriptor;
import org.databene.model.data.Uniqueness;
import org.databene.model.storage.StorageSystem;
import org.databene.benerator.*;
import org.databene.benerator.composite.BlankEntityGenerator;
import org.databene.benerator.composite.ComponentBuilder;
import org.databene.benerator.composite.ComponentTypeConverter;
import org.databene.benerator.composite.MutatingGeneratorProxy;
import org.databene.benerator.composite.SimpleTypeEntityGenerator;
import org.databene.benerator.distribution.DistributingGenerator;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.expression.ScriptExpression;
import org.databene.benerator.script.BeneratorScriptParser;
import org.databene.benerator.util.FilteringGenerator;
import org.databene.benerator.wrapper.*;
import org.databene.commons.*;
import org.databene.dataset.DatasetFactory;
import org.databene.document.flat.FlatFileColumnDescriptor;
import org.databene.document.flat.FlatFileUtil;
import org.databene.platform.dbunit.DbUnitEntitySource;
import org.databene.platform.flat.FlatFileEntitySource;
import org.databene.platform.xls.XLSEntitySource;
import org.databene.platform.csv.CSVEntitySource;
import org.databene.script.ScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Creates entity generators from entity metadata.<br/>
 * <br/>
 * Created: 08.09.2007 07:45:40
 * @author Volker Bergmann
 */
public class ComplexTypeGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ComplexTypeGeneratorFactory.class);

    /** private constructor for preventing instantiation */
    private ComplexTypeGeneratorFactory() {}
    
    // public utility methods ------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public static Generator<Entity> createComplexTypeGenerator(String instanceName, 
    		ComplexTypeDescriptor type, Uniqueness uniqueness, BeneratorContext context) {
        if (logger.isDebugEnabled())
            logger.debug("createComplexTypeGenerator(" + type.getName() + ")");
        // create original generator
        Generator<Entity> generator = null;
        generator = (Generator<Entity>) DescriptorUtil.getGeneratorByName(type, context);
        if (generator == null)
            generator = createSourceGenerator(type, uniqueness, context);
        if (generator == null)
            generator = createSyntheticEntityGenerator(instanceName, type, uniqueness, context);
        else
            generator = createMutatingEntityGenerator(instanceName, type, context, generator);
        // create wrappers
        generator = TypeGeneratorFactory.wrapWithPostprocessors(generator, type, context);
        generator = DescriptorUtil.wrapGeneratorWithVariables(type, context, generator);
        generator = DescriptorUtil.wrapWithProxy(generator, type);
        if (logger.isDebugEnabled())
            logger.debug("Created " + generator);
        return generator;
    }
    
    // private helpers -------------------------------------------------------------------------------------------------

    private static Generator<Entity> createSourceGenerator(ComplexTypeDescriptor descriptor, Uniqueness uniqueness, BeneratorContext context) {
        // if no sourceObject is specified, there's nothing to do
        String sourceName = descriptor.getSource();
        if (sourceName == null)
            return null;
        // create sourceObject generator
        Generator<Entity> generator = null;
        Object contextSourceObject = context.get(sourceName);
        if (contextSourceObject != null)
            generator = createSourceGeneratorFromObject(descriptor, context, generator, contextSourceObject);
        else {
        	String lcSourceName = sourceName.toLowerCase();
        	if (lcSourceName.endsWith(".xml"))
	            generator = new IteratingGenerator<Entity>(new DbUnitEntitySource(sourceName, context));
	        else if (lcSourceName.endsWith(".csv"))
	            generator = createCSVSourceGenerator(descriptor, context, sourceName);
	        else if (lcSourceName.endsWith(".flat"))
	            generator = createFlatSourceGenerator(descriptor, context, sourceName);
	        else if (lcSourceName.endsWith(".xls"))
	            generator = createXLSSourceGenerator(descriptor, context, sourceName);
	        else {
	        	try {
		        	Object sourceObject = BeneratorScriptParser.parseBeanSpec(sourceName).evaluate(context);
		        	return createSourceGeneratorFromObject(descriptor, context, generator, sourceObject);
	        	} catch (Exception e) {
	        		throw new UnsupportedOperationException("Unknown source type: " + sourceName);
	        	}
	        }
        }
        if (generator.getGeneratedType() != Entity.class)
        	generator = new SimpleTypeEntityGenerator(generator, descriptor);
        if (descriptor.getFilter() != null) {
        	Expression<Boolean> filter 
        		= new ScriptExpression<Boolean>(ScriptUtil.parseScriptText(descriptor.getFilter()));
        	generator = new FilteringGenerator<Entity>(generator, filter);
        }
    	Distribution distribution = GeneratorFactoryUtil.getDistribution(descriptor.getDistribution(), uniqueness, false, context);
        if (distribution != null)
        	generator = new DistributingGenerator<Entity>(generator, distribution, uniqueness.isUnique());
    	return generator;
    }

    @SuppressWarnings("unchecked")
    private static Generator<Entity> createSourceGeneratorFromObject(ComplexTypeDescriptor descriptor,
            BeneratorContext context, Generator<Entity> generator, Object sourceObject) {
	    if (sourceObject instanceof StorageSystem) {
	        StorageSystem storage = (StorageSystem) sourceObject;
	        String selector = descriptor.getSelector();
	        generator = new IteratingGenerator<Entity>(storage.queryEntities(descriptor.getName(), selector, context));
	    } else if (sourceObject instanceof EntitySource) {
	        generator = new IteratingGenerator<Entity>((EntitySource) sourceObject);
	    } else if (sourceObject instanceof Generator) {
	        generator = (Generator<Entity>) sourceObject;
	    } else
	        throw new UnsupportedOperationException("Source type not supported: " + sourceObject.getClass());
	    return generator;
    }

	private static Generator<Entity> createFlatSourceGenerator(
			ComplexTypeDescriptor descriptor, BeneratorContext context, String sourceName) {
		Generator<Entity> generator;
		String encoding = descriptor.getEncoding();
		if (encoding == null)
		    encoding = context.getDefaultEncoding();
		String pattern = descriptor.getPattern();
		if (pattern == null)
		    throw new ConfigurationError("No pattern specified for flat file import: " + sourceName);
		FlatFileColumnDescriptor[] ffcd = FlatFileUtil.parseProperties(pattern);
		Converter<String, String> scriptConverter = DescriptorUtil.createScriptConverter(context);
		FlatFileEntitySource iterable = new FlatFileEntitySource(sourceName, descriptor, scriptConverter, encoding, null, ffcd);
		iterable.setContext(context);
		generator = new IteratingGenerator<Entity>(iterable);
		return generator;
	}

    @SuppressWarnings("unchecked")
    private static Generator<Entity> createCSVSourceGenerator(
			ComplexTypeDescriptor complexType, BeneratorContext context, String sourceName) {
		Generator<Entity> generator;
		String encoding = complexType.getEncoding();
		if (encoding == null)
		    encoding = context.getDefaultEncoding();
		Converter<String, String> scriptConverter = DescriptorUtil.createScriptConverter(context);
		String dataset = complexType.getDataset();
		String nesting = complexType.getNesting();
		char separator = DescriptorUtil.getSeparator(complexType, context);
		if (dataset != null && nesting != null) {
		    String[] dataFiles = DatasetFactory.getDataFiles(sourceName, dataset, nesting);
		    Generator<Entity>[] sources = new Generator[dataFiles.length];
		    for (int i = 0; i < dataFiles.length; i++) {
	            CSVEntitySource source = new CSVEntitySource(dataFiles[i], complexType.getName(), separator, encoding);
	            source.setContext(context);
	            sources[i] = new IteratingGenerator<Entity>(source);
            }
		    generator = new AlternativeGenerator<Entity>(Entity.class, sources); 
		} else {
		    // iterate over (possibly large) data file
			CSVEntitySource iterable = new CSVEntitySource(sourceName, complexType.getName(), scriptConverter, separator, encoding);
			iterable.setContext(context);
		    generator = new IteratingGenerator<Entity>(iterable);
		}
		generator = new ConvertingGenerator<Entity, Entity>(generator, new ComponentTypeConverter(complexType));
		return generator;
	}

    @SuppressWarnings("unchecked")
    private static Generator<Entity> createXLSSourceGenerator(
			ComplexTypeDescriptor complexType, BeneratorContext context, String sourceName) {
		Generator<Entity> generator;
		String encoding = complexType.getEncoding();
		if (encoding == null)
		    encoding = context.getDefaultEncoding();
		Converter<String, String> scriptConverter = DescriptorUtil.createScriptConverter(context);
		String dataset = complexType.getDataset();
		String nesting = complexType.getNesting();
		if (dataset != null && nesting != null) {
		    String[] dataFiles = DatasetFactory.getDataFiles(sourceName, dataset, nesting);
		    Generator<Entity>[] sources = new Generator[dataFiles.length];
		    for (int i = 0; i < dataFiles.length; i++) {
	            XLSEntitySource source = new XLSEntitySource(dataFiles[i]);
	            source.setContext(context);
	            sources[i] = new IteratingGenerator<Entity>(source);
            }
		    generator = new AlternativeGenerator<Entity>(Entity.class, sources); 
		} else {
		    // iterate over (possibly large) data file
			XLSEntitySource source = new XLSEntitySource(sourceName, scriptConverter);
			source.setContext(context);
		    generator = new IteratingGenerator<Entity>(source);
		}
		generator = new ConvertingGenerator<Entity, Entity>(generator, new ComponentTypeConverter(complexType));
		return generator;
	}

    @SuppressWarnings("unchecked")
    private static Generator<Entity> createSyntheticEntityGenerator(String name, 
            ComplexTypeDescriptor complexType, Uniqueness uniqueness, BeneratorContext context) {
        List<ComponentBuilder<Entity>> componentBuilders = new ArrayList<ComponentBuilder<Entity>>();
        if (DescriptorUtil.isWrappedSimpleType(complexType)) {
    		TypeDescriptor contentType = complexType.getComponent(ComplexTypeDescriptor.__SIMPLE_CONTENT).getTypeDescriptor();
    		Generator<?> generator = SimpleTypeGeneratorFactory.createSimpleTypeGenerator(
    				(SimpleTypeDescriptor) contentType, false, uniqueness, context);
        	return new SimpleTypeEntityGenerator(generator, complexType);
        }
        Collection<ComponentDescriptor> components = complexType.getComponents();
        for (ComponentDescriptor component : components) {
            if (!complexType.equals(component.getTypeDescriptor()) && component.getMode() != Mode.ignored) {
            	String componentName = component.getName();
				ComponentDescriptor defaultComponentConfig = context.getDefaultComponentConfig(componentName);
				if (!complexType.isDeclaredComponent(componentName) && defaultComponentConfig != null)
					component = defaultComponentConfig;
            	ComponentBuilder<Entity> builder = (ComponentBuilder<Entity>) ComponentBuilderFactory.createComponentBuilder(component, context);
				componentBuilders.add(builder); 
            }
        }
    	BlankEntityGenerator source = new BlankEntityGenerator(complexType);
		return new MutatingGeneratorProxy<Entity>(name, source, componentBuilders, context);
    }

	@SuppressWarnings("unchecked")
    private static Generator<Entity> createMutatingEntityGenerator(String name, 
            ComplexTypeDescriptor descriptor, BeneratorContext context, Generator<Entity> generator) {
    	List<ComponentBuilder<Entity>> componentGenerators = new ArrayList<ComponentBuilder<Entity>>();
        Collection<ComponentDescriptor> components = descriptor.getDeclaredComponents();
        for (ComponentDescriptor component : components)
            if (component.getMode() != Mode.ignored && !ComplexTypeDescriptor.__SIMPLE_CONTENT.equals(component.getName())) {
            	ComponentBuilder<Entity> builder = (ComponentBuilder<Entity>) ComponentBuilderFactory.createComponentBuilder(component, context);
	            componentGenerators.add(builder);
            }
        return new MutatingGeneratorProxy<Entity>(name, generator, componentGenerators, context);
    }
	
}
