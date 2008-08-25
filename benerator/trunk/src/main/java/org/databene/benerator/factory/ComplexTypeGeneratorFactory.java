/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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
import org.databene.model.data.DescriptorUtil;
import org.databene.model.data.Entity;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.Mode;
import org.databene.model.data.SimpleTypeDescriptor;
import org.databene.model.data.TypeDescriptor;
import org.databene.model.function.Distribution;
import org.databene.model.function.IndividualWeight;
import org.databene.model.function.Sequence;
import org.databene.model.function.WeightFunction;
import org.databene.model.storage.StorageSystem;
import org.databene.benerator.*;
import org.databene.benerator.composite.ComponentBuilder;
import org.databene.benerator.composite.ComponentTypeConverter;
import org.databene.benerator.composite.ConfiguredEntityGenerator;
import org.databene.benerator.composite.EntityGenerator;
import org.databene.benerator.composite.SimpleTypeEntityGenerator;
import org.databene.benerator.sample.SequencedSampleGenerator;
import org.databene.benerator.sample.WeightedSampleGenerator;
import org.databene.benerator.util.GeneratorUtil;
import org.databene.benerator.wrapper.*;
import org.databene.commons.*;
import org.databene.dataset.DatasetFactory;
import org.databene.document.flat.FlatFileColumnDescriptor;
import org.databene.document.flat.FlatFileUtil;
import org.databene.platform.dbunit.DbUnitEntityIterable;
import org.databene.platform.flat.FlatFileEntityIterable;
import org.databene.platform.csv.CSVEntityIterable;
import org.databene.script.ScriptConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Creates entity generators from entity metadata.<br/>
 * <br/>
 * Created: 08.09.2007 07:45:40
 * @author Volker Bergmann
 */
public class ComplexTypeGeneratorFactory {

    private static final Log logger = LogFactory.getLog(ComplexTypeGeneratorFactory.class);

    // attributes ------------------------------------------------------------------------------------------------------
    
    //private static Escalator escalator = new LoggerEscalator();

    // private constructor for preventing instantiation ----------------------------------------------------------------
    
    private ComplexTypeGeneratorFactory() {}
    
    // public utility methods ------------------------------------------------------------------------------------------

    public static Generator<Entity> createComplexTypeGenerator(ComplexTypeDescriptor type, boolean unique, Context context, GenerationSetup setup) {
        if (logger.isDebugEnabled())
            logger.debug("create(" + type.getName() + ")");
        // create original generator
        Generator<Entity> generator = null;
        generator = (Generator<Entity>) TypeGeneratorFactory.createByGeneratorName(type, context);
        if (generator == null)
            generator = createSourceGenerator(type, context, setup);
        if (generator == null)
            generator = createSyntheticEntityGenerator(type, unique, context, setup);
        else
            generator = createMutatingEntityGenerator(type, context, setup, generator);
        // create wrappers
        generator = TypeGeneratorFactory.wrapWithPostprocessors(generator, type, context);
        generator = wrapGeneratorWithVariables(type, context, setup, generator);
        if (logger.isDebugEnabled())
            logger.debug("Created " + generator);
        return generator;
    }
    
    // private helpers -------------------------------------------------------------------------------------------------

    private static Generator<Entity> wrapGeneratorWithVariables(
            ComplexTypeDescriptor type, Context context, GenerationSetup setup, Generator<Entity> generator) {
        Collection<InstanceDescriptor> variables = variablesOfThisAndParents(type);
            Map<String, Generator<? extends Object>> varGens = new HashMap<String, Generator<? extends Object>>();
            for (InstanceDescriptor variable : variables) {
                Generator<? extends Object> varGen = InstanceGeneratorFactory.createInstanceGenerator(variable, context, setup);
                varGens.put(variable.getName(), varGen);
            }
        return new ConfiguredEntityGenerator((Generator<Entity>) generator, varGens, context);
    }

    private static Collection<InstanceDescriptor> variablesOfThisAndParents(TypeDescriptor type) {
        Collection<InstanceDescriptor> variables = new ArrayList<InstanceDescriptor>();
        while (type instanceof ComplexTypeDescriptor) {
            variables.addAll(((ComplexTypeDescriptor) type).getVariables());
            type = type.getParent();
        }
        return variables;
    }
    
    // private helpers -------------------------------------------------------------------------------------------------

    private static Generator<Entity> createSourceGenerator(
            ComplexTypeDescriptor descriptor, Context context, GenerationSetup setup) {
        // if no sourceObject is specified, there's nothing to do
        String sourceName = descriptor.getSource();
        if (sourceName == null)
            return null;
        // create sourceObject generator
        Generator<Entity> generator = null;
        Object sourceObject = context.get(sourceName);
        if (sourceObject != null) {
            if (sourceObject instanceof StorageSystem) {
                StorageSystem storage = (StorageSystem) sourceObject;
                String selector = descriptor.getSelector();
                generator = new IteratingGenerator<Entity>(storage.queryEntities(descriptor.getName(), selector, context));
            } else if (sourceObject instanceof TypedIterable) {
                generator = new IteratingGenerator((TypedIterable) sourceObject);
            } else if (sourceObject instanceof Generator) {
                generator = (Generator) sourceObject;
            } else
                throw new UnsupportedOperationException("Source type not supported: " + sourceObject.getClass());
        } else {
            if (sourceName.endsWith(".xml"))
                generator = new IteratingGenerator<Entity>(new DbUnitEntityIterable(sourceName, context));
            else if (sourceName.endsWith(".csv")) {
                generator = createCSVSourceGenerator(descriptor, context, setup, sourceName);
            } else if (sourceName.endsWith(".flat")) {
                generator = createFlatSourceGenerator(descriptor, context, setup, sourceName);
            } else
                throw new UnsupportedOperationException("Unknown source type: " + sourceName);
        }
        if (generator.getGeneratedType() != Entity.class)
        	generator = new SimpleTypeEntityGenerator(generator, descriptor);
        if (descriptor.getDistribution() != null)
        	return applyDistribution(descriptor, generator);
        else
        	return TypeGeneratorFactory.wrapWithProxy(generator, descriptor);
    }

	private static Generator<Entity> applyDistribution(
			ComplexTypeDescriptor descriptor, Generator<Entity> generator) {
		List<Entity> values = GeneratorUtil.allProducts(generator);
		Distribution distribution = descriptor.getDistribution();
		if (distribution instanceof Sequence) {
			generator = new SequencedSampleGenerator<Entity>(Entity.class, (Sequence) distribution, values);
		} else if (distribution instanceof WeightFunction || distribution instanceof IndividualWeight)
			generator = new WeightedSampleGenerator<Entity>(Entity.class, distribution, values);
		else
			throw new ConfigurationError("Not a supported distribution: " + distribution);
		if (descriptor.getVariation1() != null)
			BeanUtil.setPropertyValue(generator, "variation1", descriptor.getVariation1(), false);
		if (descriptor.getVariation2() != null)
			BeanUtil.setPropertyValue(generator, "variation2", descriptor.getVariation2(), false);
		return generator;
	}

	private static Generator<Entity> createFlatSourceGenerator(
			ComplexTypeDescriptor descriptor, Context context,
			GenerationSetup setup, String sourceName) {
		Generator<Entity> generator;
		String encoding = descriptor.getEncoding();
		if (encoding == null)
		    encoding = setup.getDefaultEncoding();
		String pattern = descriptor.getPattern();
		if (pattern == null)
		    throw new ConfigurationError("No pattern specified for flat file import: " + sourceName);
		FlatFileColumnDescriptor[] ffcd = FlatFileUtil.parseProperties(pattern);
		ScriptConverter scriptConverter = new ScriptConverter(context);
		FlatFileEntityIterable iterable = new FlatFileEntityIterable(sourceName, descriptor, scriptConverter, encoding, ffcd);
		generator = new IteratingGenerator(iterable);
		return generator;
	}

	private static Generator<Entity> createCSVSourceGenerator(
			ComplexTypeDescriptor complexType, Context context,
			GenerationSetup setup, String sourceName) {
		Generator<Entity> generator;
		String encoding = complexType.getEncoding();
		if (encoding == null)
		    encoding = setup.getDefaultEncoding();
		ScriptConverter scriptConverter = new ScriptConverter(context);
		String dataset = complexType.getDataset();
		String nesting = complexType.getNesting();
		char separator = getSeparator(complexType, setup);
		if (dataset != null && nesting != null) {
		    String[] dataFiles = DatasetFactory.getDataFiles(sourceName, dataset, nesting);
		    Generator<Entity>[] sources = new Generator[dataFiles.length];
		    for (int i = 0; i < dataFiles.length; i++)
		        sources[i] = new IteratingGenerator(new CSVEntityIterable(dataFiles[i], complexType.getName(), separator, encoding));
		    generator = new AlternativeGenerator(Entity.class, sources); 
		} else {
		    // iterate over (possibly large) data file
		    CSVEntityIterable iterable = new CSVEntityIterable(sourceName, complexType.getName(), scriptConverter, separator, encoding);
		    generator = new IteratingGenerator(iterable);
		}
		generator = new ConvertingGenerator<Entity, Entity>(generator, new ComponentTypeConverter(complexType));
		return generator;
	}

	private static char getSeparator(ComplexTypeDescriptor descriptor, GenerationSetup setup) {
		String separatorString = descriptor.getSeparator();
		char separator = setup.getDefaultSeparator();
		if (!StringUtil.isEmpty(separatorString)) {
			Assert.length(separatorString, 1);
			separator = separatorString.charAt(0);
		}
		return separator;
	}

    private static Generator<Entity> createSyntheticEntityGenerator(
            ComplexTypeDescriptor complexType, boolean unique, Context context, GenerationSetup setup) {
        List<ComponentBuilder> componentBuilders = new ArrayList<ComponentBuilder>();
        if (DescriptorUtil.isWrappedSimpleType(complexType)) {
    		TypeDescriptor contentType = complexType.getComponent(ComplexTypeDescriptor.__SIMPLE_CONTENT).getType();
    		Generator<Object> generator = (Generator<Object>) SimpleTypeGeneratorFactory.createSimpleTypeGenerator((SimpleTypeDescriptor) contentType, false, unique, context, setup);
        	return new SimpleTypeEntityGenerator(generator, complexType);
        }
        Collection<ComponentDescriptor> components = complexType.getComponents();
        for (ComponentDescriptor component : components) {
            if (!complexType.equals(component.getType()) && component.getMode() != Mode.ignored) {
            	String componentName = component.getName();
				ComponentDescriptor defaultComponentConfig = setup.getDefaultComponentConfig(componentName);
				if (!complexType.isDeclaredComponent(componentName) && defaultComponentConfig != null)
					component = defaultComponentConfig;
            	ComponentBuilder builder = ComponentBuilderFactory.createComponentBuilder(component, context, setup);
				componentBuilders.add(builder); 
            }
        }
    	return new EntityGenerator(complexType, componentBuilders, context);
    }

	private static Generator<Entity> createMutatingEntityGenerator(
            ComplexTypeDescriptor descriptor, Context context, GenerationSetup setup, Generator<Entity> generator) {
    	List<ComponentBuilder> componentGenerators = new ArrayList<ComponentBuilder>();
        Collection<ComponentDescriptor> components = descriptor.getDeclaredComponents();
        for (ComponentDescriptor component : components)
            if (component.getMode() != Mode.ignored && !ComplexTypeDescriptor.__SIMPLE_CONTENT.equals(component.getName()))
            	componentGenerators.add(ComponentBuilderFactory.createComponentBuilder(component, context, setup));
        return new EntityGenerator(descriptor, generator, componentGenerators, context);
    }
}
