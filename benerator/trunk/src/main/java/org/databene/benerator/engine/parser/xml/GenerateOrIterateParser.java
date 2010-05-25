/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.benerator.engine.parser.xml;

import static org.databene.benerator.engine.DescriptorConstants.*;
import static org.databene.benerator.parser.xml.XmlDescriptorParser.parseStringAttribute;

import java.util.Map;
import java.util.Set;

import org.databene.benerator.Generator;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.DescriptorParser;
import org.databene.benerator.engine.GeneratorTask;
import org.databene.benerator.engine.ParserFactory;
import org.databene.benerator.engine.ResourceManager;
import org.databene.benerator.engine.Statement;
import org.databene.benerator.engine.expression.ErrorHandlerExpression;
import org.databene.benerator.engine.expression.StringScriptExpression;
import org.databene.benerator.engine.expression.context.DefaultPageSizeExpression;
import org.databene.benerator.engine.expression.xml.XMLConsumerExpression;
import org.databene.benerator.engine.statement.GenerateAndConsumeTask;
import org.databene.benerator.engine.statement.GenerateOrIterateStatement;
import org.databene.benerator.engine.statement.LazyStatement;
import org.databene.benerator.engine.statement.TimedGeneratorStatement;
import org.databene.benerator.factory.GeneratorFactoryUtil;
import org.databene.benerator.factory.InstanceGeneratorFactory;
import org.databene.benerator.parser.ModelParser;
import org.databene.benerator.script.BeneratorScriptParser;
import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.Context;
import org.databene.commons.ErrorHandler;
import org.databene.commons.Expression;
import org.databene.commons.StringUtil;
import org.databene.commons.expression.DynamicExpression;
import org.databene.commons.xml.XMLUtil;
import org.databene.model.consumer.Consumer;
import org.databene.model.data.ArrayTypeDescriptor;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.ComponentDescriptor;
import org.databene.model.data.DataModel;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.PrimitiveType;
import org.databene.model.data.TypeDescriptor;
import org.databene.model.data.Uniqueness;
import org.databene.model.data.VariableHolder;
import org.databene.task.PageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Parses a &lt;generate&gt; or &lt;update&gt; element in a Benerator descriptor file.<br/><br/>
 * Created: 25.10.2009 01:05:18
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class GenerateOrIterateParser implements DescriptorParser {
	
	private static final Logger logger = LoggerFactory.getLogger(GenerateOrIterateParser.class);
	private static final Set<String> PART_NAMES = CollectionUtil.toSet(
			EL_VARIABLE, EL_VALUE, EL_ID, EL_COMPOSITE_ID, EL_ATTRIBUTE, EL_REFERENCE, EL_CONSUMER);
	private Set<String> CONSUMER_EXPECTING_ELEMENTS = CollectionUtil.toSet(EL_GENERATE, EL_ITERATE);
	
	// DescriptorParser interface --------------------------------------------------------------------------------------
	
	public boolean supports(String elementName, String parentName) {
	    return EL_GENERATE.equals(elementName) || EL_ITERATE.equals(elementName);
    }
	
	public Statement parse(final Element element, final Statement[] parentPath, 
			final ResourceManager resourceManager) {
		final boolean looped = AbstractDescriptorParser.containsLoop(parentPath);
		final boolean nested = AbstractDescriptorParser.containsGeneratorStatement(parentPath);
		Expression<Statement> expression = new DynamicExpression<Statement>() {
			public Statement evaluate(Context context) {
				return parseGenerate(
						element, parentPath, resourceManager, (BeneratorContext) context, !looped, nested);
            }
		};
		Statement statement = new LazyStatement(expression);
		if (!looped)
			statement = new TimedGeneratorStatement(getNameOrType(element), statement);
		return statement;
	}
	
	// private helpers -------------------------------------------------------------------------------------------------

	private String getNameOrType(Element element) {
		String result = element.getAttribute(ATT_NAME);
		if (StringUtil.isEmpty(result))
			result = element.getAttribute(ATT_TYPE);
		if (StringUtil.isEmpty(result))
			result = "anonymous";
		return result;
	}
	
    @SuppressWarnings("unchecked")
    public GenerateOrIterateStatement parseGenerate(Element element, Statement[] parentPath,
    		ResourceManager resourceManager, BeneratorContext context, boolean infoLog, boolean nested) {
	    InstanceDescriptor descriptor = mapDescriptorElement(element, context);
		
		Generator<Long> countGenerator = GeneratorFactoryUtil.getCountGenerator(descriptor, false);
		Expression<Long> pageSize = DescriptorParserUtil.parseLongAttribute(ATT_PAGESIZE, element, new DefaultPageSizeExpression());
		Expression<Integer> threads = DescriptorParserUtil.parseIntAttribute(ATT_THREADS, element, 1);
		Expression<PageListener> pager = (Expression<PageListener>) BeneratorScriptParser.parseBeanSpec(element.getAttribute(ATT_PAGER));
		
		String name = element.getAttribute(ATT_NAME);
		StringScriptExpression levelExpr = new StringScriptExpression(element.getAttribute(ATT_ON_ERROR));
		Expression<ErrorHandler> errorHandler = new ErrorHandlerExpression(name, levelExpr);
		GenerateOrIterateStatement creator = new GenerateOrIterateStatement(
				null, countGenerator, pageSize, pager, threads, errorHandler, infoLog, nested);
		GeneratorTask task = parseTask(element, parentPath, creator, descriptor, resourceManager, context, infoLog);
		creator.setTask(task);
		return creator;
	}

	@SuppressWarnings("unchecked")
    private GeneratorTask parseTask(Element element, Statement[] parentPath, GenerateOrIterateStatement statement, 
    		InstanceDescriptor descriptor, ResourceManager resourceManager, BeneratorContext context, 
    		boolean infoLog) {
		descriptor.setNullable(false);
		if (infoLog)
			logger.info(descriptor.toString());
		else if (logger.isDebugEnabled())
			logger.debug(descriptor.toString());
		
		boolean isSubCreator = AbstractDescriptorParser.containsGeneratorStatement(parentPath);
		
		// create generator
		Generator<?> generator = InstanceGeneratorFactory.createSingleInstanceGenerator(descriptor, Uniqueness.NONE, context);
		
		// parse consumers
		boolean consumerExpected = CONSUMER_EXPECTING_ELEMENTS.contains(element.getNodeName());
		Expression consumer = parseConsumers(element, consumerExpected, resourceManager);
		
		String taskName = descriptor.getName();
		if (taskName == null)
			taskName = descriptor.getLocalType().getSource();
		
		GenerateAndConsumeTask task = new GenerateAndConsumeTask(taskName, generator, consumer, isSubCreator);

		// handle sub-<generate/>
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getNodeName();
			if (!PART_NAMES.contains(childName)) {
	            DescriptorParser parser = ParserFactory.getParser(childName, element.getNodeName());
	            Statement subStatement = parser.parse(child, ArrayUtil.append(parentPath, statement), task);
				task.addSubStatement(subStatement);
            }
		}
		return task;
    }

	private Expression<Consumer<?>> parseConsumers(Element entityElement, boolean consumersExpected, ResourceManager resourceManager) {
		return new XMLConsumerExpression(entityElement, consumersExpected, resourceManager);
	}

	private InstanceDescriptor mapDescriptorElement(Element element, BeneratorContext context) {
		
		// evaluate type
		String type = parseStringAttribute(element, ATT_TYPE, context, false);
		TypeDescriptor localType;
		if (PrimitiveType.ARRAY.getName().equals(type) 
				|| XMLUtil.getChildElements(element, false, EL_VALUE).length > 0)
			localType = new ArrayTypeDescriptor(element.getAttribute(ATT_NAME));
		else {
			TypeDescriptor parentType = DataModel.getDefaultInstance().getTypeDescriptor(type);
			if (parentType != null) {
				type = parentType.getName(); // take over capitalization of the parent
				localType = new ComplexTypeDescriptor(parentType.getName(), (ComplexTypeDescriptor) parentType);
			} else
				localType = new ComplexTypeDescriptor(type, "entity");
		}
		
		// assemble instance descriptor
		InstanceDescriptor instance = new InstanceDescriptor(type, type);
		instance.setLocalType(localType);
		
		// map element attributes
		for (Map.Entry<String, String> attribute : XMLUtil.getAttributes(element).entrySet()) {
			String attributeName = attribute.getKey();
			if (!CREATE_ENTITIES_EXT_SETUP.contains(attributeName)) {
				Object attributeValue = attribute.getValue();
				if (instance.supportsDetail(attributeName))
					instance.setDetailValue(attributeName, attributeValue);
				else if (localType != null)
					localType.setDetailValue(attributeName, attributeValue);
				// else we expect different types
			}
		}
		
		// parse child elements
		ModelParser parser = new ModelParser(context);
		int valueCount = 0;
		for (Element child : XMLUtil.getChildElements(element)) {
			String childType = XMLUtil.localName(child);
			if (EL_VARIABLE.equals(childType)) {
				parser.parseVariable(child, (VariableHolder) localType);
			} else if (COMPONENT_TYPES.contains(childType)) {
				ComponentDescriptor component = parser.parseSimpleTypeComponent(child, (ComplexTypeDescriptor) localType);
				((ComplexTypeDescriptor) instance.getTypeDescriptor()).addComponent(component);
			} else if (EL_VALUE.equals(childType)) {
				parser.parseSimpleTypeArrayElement(child, (ArrayTypeDescriptor) localType, valueCount++);
			}
		}
		return instance;
	}

}
