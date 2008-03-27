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
import org.databene.model.data.Entity;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.Mode;
import org.databene.model.data.TypeDescriptor;
import org.databene.model.storage.StorageSystem;
import org.databene.benerator.*;
import org.databene.benerator.composite.ConfiguredEntityGenerator;
import org.databene.benerator.composite.EntityGenerator;
import org.databene.benerator.wrapper.*;
import org.databene.commons.*;
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
public class ComplexTypeGeneratorFactory extends TypeGeneratorFactory {

    private static final Log logger = LogFactory.getLog(ComplexTypeGeneratorFactory.class);

    // attributes ------------------------------------------------------------------------------------------------------
    
    private static Escalator escalator = new LoggerEscalator();

    // private constructor for preventing instantiation ----------------------------------------------------------------
    
    private ComplexTypeGeneratorFactory() {}
    
    // public utility methods ------------------------------------------------------------------------------------------

    public static Generator<Entity> createComplexTypeGenerator(ComplexTypeDescriptor type, Context context, GenerationSetup setup) {
        if (logger.isDebugEnabled())
            logger.debug("create(" + type.getName() + ")");
        // create original generator
        Generator<Entity> generator = null;
        generator = (Generator<Entity>) createByGeneratorName(type, context);
        if (generator == null)
            generator = createSourceGenerator(type, context, setup);
        if (generator == null)
            generator = createGeneratingGenerator(type, context, setup);
        else
            generator = createMutatingEntityGenerator(type, context, setup, generator);
        // create wrappers
        generator = createValidatingGenerator(type, generator);
        generator = createVariableGenerator(type, context, setup, generator);
        if (logger.isDebugEnabled())
            logger.debug("Created " + generator);
        return generator;
    }

    private static Generator<Entity> createVariableGenerator(
            ComplexTypeDescriptor type, Context context, GenerationSetup setup, Generator<Entity> generator) {
        Collection<InstanceDescriptor> variables = variablesOfThisAndParents(type);
        if (variables.size() > 0) {
            Map<String, Generator<? extends Object>> varGens = new HashMap<String, Generator<? extends Object>>();
            for (InstanceDescriptor variable : variables) {
                Generator<? extends Object> varGen = InstanceGeneratorFactory.createInstanceGenerator(variable, context, setup);
                varGens.put(variable.getName(), varGen);
            }
            generator = new ConfiguredEntityGenerator((Generator<Entity>) generator, varGens, context);
        }
        return generator;
    }

    private static Collection<InstanceDescriptor> variablesOfThisAndParents(TypeDescriptor type) {
        Collection<InstanceDescriptor> variables = new ArrayList<InstanceDescriptor>();
        while (type instanceof ComplexTypeDescriptor) {
            variables.addAll(((ComplexTypeDescriptor) type).getVariables());
            //System.out.println(type.getName() + " -> " + type.getParent());
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
                generator = new IteratingGenerator<Entity>(storage.queryEntities(descriptor.getName(), selector));
            } else if (sourceObject instanceof TypedIterable) {
                generator = new IteratingGenerator((TypedIterable) sourceObject);
            } else if (sourceObject instanceof Generator) {
                generator = (Generator) sourceObject;
            } else
                throw new UnsupportedOperationException("Source type not supported: " + sourceObject.getClass());
        } else {
            if (sourceName.endsWith(".xml"))
                generator = new IteratingGenerator<Entity>(new DbUnitEntityIterable(sourceName, context, setup.getDefaultScript()));
            else if (sourceName.endsWith(".csv")) {
                String encoding = descriptor.getEncoding();
                if (encoding == null)
                    encoding = setup.getDefaultEncoding();
                ScriptConverter scriptConverter = new ScriptConverter(context, setup.getDefaultScript());
                CSVEntityIterable iterable = new CSVEntityIterable(sourceName, descriptor.getName(), scriptConverter, ',', encoding);
                generator = new IteratingGenerator(iterable);
            } else if (sourceName.endsWith(".flat")) {
                String encoding = descriptor.getEncoding();
                if (encoding == null)
                    encoding = setup.getDefaultEncoding();
                String pattern = descriptor.getPattern();
                if (pattern == null)
                    throw new ConfigurationError("No pattern specified for flat file import: " + sourceName);
                FlatFileColumnDescriptor[] ffcd = FlatFileUtil.parseProperties(pattern);
                ScriptConverter scriptConverter = new ScriptConverter(context, setup.getDefaultScript());
                FlatFileEntityIterable iterable = new FlatFileEntityIterable(sourceName, descriptor, scriptConverter, encoding, ffcd);
                generator = new IteratingGenerator(iterable);
            } else
                throw new UnsupportedOperationException("Unknown source type: " + sourceName);
        }
        return createProxy(descriptor, generator);
    }

    private static Generator<Entity> createGeneratingGenerator(
            ComplexTypeDescriptor descriptor, Context context, GenerationSetup setup) {
        Map<String, Generator<? extends Object>> componentGenerators = new OrderedMap<String, Generator<? extends Object>>();
        Collection<ComponentDescriptor> descriptors = descriptor.getComponents();
        for (ComponentDescriptor component : descriptors) {
            if (descriptor.getName().equals(component.getName()))
                continue; // avoid recursion
            if (component.getMode() != Mode.ignored) {
                Generator<? extends Object> componentGenerator = ComponentGeneratorFactory.createComponentGenerator(component, context, setup);
                componentGenerators.put(component.getName(), componentGenerator);
            }
        }
        return new EntityGenerator(descriptor, componentGenerators, context);
    }

    private static Generator<Entity> createMutatingEntityGenerator(
            ComplexTypeDescriptor descriptor, Context context, GenerationSetup setup, Generator<Entity> generator) {
        Map<String, Generator<? extends Object>> componentGenerators = new OrderedMap<String, Generator<? extends Object>>();
        Collection<ComponentDescriptor> descriptors = descriptor.getDeclaredComponents();
        for (ComponentDescriptor component : descriptors) {
            if (component.getMode() != Mode.ignored) {
                Generator<? extends Object> componentGenerator = ComponentGeneratorFactory.createComponentGenerator(component, context, setup);
                componentGenerators.put(component.getName(), componentGenerator);
            }
        }
        return new EntityGenerator(descriptor, generator, componentGenerators, context);
    }

}
