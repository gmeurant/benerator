/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.databene.benerator.Consumer;
import org.databene.benerator.Generator;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.GeneratorTask;
import org.databene.benerator.engine.ResourceManager;
import org.databene.benerator.engine.Statement;
import org.databene.benerator.engine.expression.CachedExpression;
import org.databene.benerator.engine.expression.xml.XMLConsumerExpression;
import org.databene.benerator.engine.statement.GenerateAndConsumeTask;
import org.databene.benerator.engine.statement.GenerateOrIterateStatement;
import org.databene.benerator.engine.statement.LazyStatement;
import org.databene.benerator.engine.statement.TimedGeneratorStatement;
import org.databene.benerator.factory.DescriptorUtil;
import org.databene.benerator.factory.InstanceGeneratorFactory;
import org.databene.benerator.script.BeneratorScriptParser;
import org.databene.commons.CollectionUtil;
import org.databene.commons.Context;
import org.databene.commons.ErrorHandler;
import org.databene.commons.Expression;
import org.databene.commons.StringUtil;
import org.databene.commons.expression.DynamicExpression;
import org.databene.commons.xml.XMLUtil;
import org.databene.model.data.ArrayTypeDescriptor;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.DataModel;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.PrimitiveType;
import org.databene.model.data.TypeDescriptor;
import org.databene.model.data.Uniqueness;
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
public class GenerateOrIterateParser extends AbstractBeneratorDescriptorParser {
	
	private static final Logger logger = LoggerFactory.getLogger(GenerateOrIterateParser.class);

	private static final Set<String> OPTIONAL_ATTRIBUTES = CollectionUtil.toSet(
			ATT_COUNT, ATT_MIN_COUNT, ATT_MAX_COUNT, ATT_COUNT_DISTRIBUTION, 
			ATT_PAGESIZE, ATT_THREADS, ATT_STATS, ATT_ON_ERROR,
			ATT_TEMPLATE, ATT_CONSUMER, 
			ATT_NAME, ATT_TYPE, ATT_GENERATOR, ATT_VALIDATOR, 
			ATT_CONVERTER, ATT_NULL_QUOTA, ATT_UNIQUE, ATT_DISTRIBUTION, ATT_CYCLIC,
			ATT_SOURCE, ATT_SEPARATOR, ATT_ENCODING, ATT_SELECTOR, ATT_SUB_SELECTOR, ATT_DATASET, ATT_NESTING, ATT_LOCALE, ATT_FILTER
		);
	
	private static final Set<String> PART_ELEMENTS = CollectionUtil.toSet(
			EL_VARIABLE, EL_VALUE, EL_ID, EL_COMPOSITE_ID, EL_ATTRIBUTE, EL_REFERENCE, EL_CONSUMER);
	
	//private static final Set<String> UBIQUITOUS_ELEMENTS = CollectionUtil.toSet(
	//		EL_COMMENT, EL_ECHO, EL_WAIT); // TODO v0.7 execute ubiquitous elements also between <vars> and <members>
	
	private static final Set<String> CONSUMER_EXPECTING_ELEMENTS = CollectionUtil.toSet(EL_GENERATE, EL_ITERATE);

	DataModel dataModel = DataModel.getDefaultInstance();

	// DescriptorParser interface --------------------------------------------------------------------------------------
	
	public GenerateOrIterateParser() {
		super("", null, OPTIONAL_ATTRIBUTES);
	}

	@Override
	public boolean supports(Element element, Statement[] parentPath) {
		String name = element.getNodeName();
	    return EL_GENERATE.equals(name) || EL_ITERATE.equals(name);
    }
	
	@Override
	public Statement doParse(final Element element, final Statement[] parentPath, 
			final BeneratorParseContext pContext) {
		final boolean looped = AbstractBeneratorDescriptorParser.containsLoop(parentPath);
		final boolean nested = AbstractBeneratorDescriptorParser.containsGeneratorStatement(parentPath);
		Expression<Statement> expression = new DynamicExpression<Statement>() {
			public Statement evaluate(Context context) {
				return parseGenerate(
						element, parentPath, pContext, (BeneratorContext) context, !looped, nested);
            }
			@Override
			public String toString() {
				return XMLUtil.format(element);
			}
		};
		Statement statement = new LazyStatement(expression);
		statement = new TimedGeneratorStatement(getNameOrType(element), statement, createProfilerPath(parentPath, statement), !looped);
		return statement;
	}
	
	// private helpers -------------------------------------------------------------------------------------------------

	private List<String> createProfilerPath(Statement[] parentPath, Statement currentElement) {
		List<String> path = new ArrayList<String>(parentPath != null ? parentPath.length + 1 : 1);
		if (parentPath != null)
			for (int i = 0; i < parentPath.length; i++)
				path.add(parentPath[i].toString());
		path.add(currentElement.toString());
		return path;
	}

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
    		BeneratorParseContext parsingContext, 
    		BeneratorContext context, boolean infoLog, boolean nested) {
	    InstanceDescriptor descriptor = mapDescriptorElement(element, context);
		
		Generator<Long> countGenerator = DescriptorUtil.createDynamicCountGenerator(descriptor, false, context);
		Expression<Long> pageSize = parsePageSize(element);
		Expression<Integer> threads = DescriptorParserUtil.parseIntAttribute(ATT_THREADS, element, 1);
		Expression<PageListener> pager = (Expression<PageListener>) BeneratorScriptParser.parseBeanSpec(element.getAttribute(ATT_PAGER));
		
		Expression<ErrorHandler> errorHandler = parseOnErrorAttribute(element, element.getAttribute(ATT_NAME));
		GenerateOrIterateStatement creator = new GenerateOrIterateStatement(
				null, countGenerator, DescriptorUtil.getMinCount(descriptor, 0), 
				pageSize, pager, threads, errorHandler, infoLog, nested);
		GeneratorTask task = parseTask(element, parentPath, creator, parsingContext, descriptor, 
				context, infoLog);
		creator.setTask(task);
		return creator;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private GeneratorTask parseTask(Element element, Statement[] parentPath, 
    		GenerateOrIterateStatement statement, BeneratorParseContext parseContext, 
    		InstanceDescriptor descriptor, BeneratorContext context, 
    		boolean infoLog) {
		descriptor.setNullable(false);
		if (infoLog)
			logger.info("{}", descriptor);
		else
			logger.debug("{}", descriptor);
		//boolean isSubCreator = AbstractBeneratorDescriptorParser.containsGeneratorStatement(parentPath);
		
		// create generator
		Generator<?> generator = InstanceGeneratorFactory.createSingleInstanceGenerator(descriptor, Uniqueness.NONE, context);
		
		String taskName = descriptor.getName();
		if (taskName == null)
			taskName = descriptor.getLocalType().getSource();
		
		GenerateAndConsumeTask task = new GenerateAndConsumeTask(taskName, generator, /*isSubCreator,*/ context);

		// parse consumers
		boolean consumerExpected = CONSUMER_EXPECTING_ELEMENTS.contains(element.getNodeName());
		Expression consumer = parseConsumers(element, consumerExpected, task.getResourceManager());
		task.setConsumer(consumer);
		
		// handle sub elements
		Stage stage = Stage.VARS;
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getNodeName();
			if (EL_VARIABLE.equals(childName)) {
				if (stage != Stage.VARS)
					syntaxWarning("variables must be configured before members and sub elements", child);
			} else if (PART_ELEMENTS.contains(childName)) {
				if (stage == Stage.OTHERS)
					syntaxWarning("members must be configured before execution of sub elements ", child);
				stage = Stage.MEMBERS;
			} else {
				stage = Stage.OTHERS;
				Statement[] subPath = parseContext.createSubPath(parentPath, statement);
				Statement subStatement = parseContext.parseChildElement(child, subPath);
				task.addSubStatement(subStatement);
            }
		}
		return task;
    }

	private Expression<Consumer> parseConsumers(Element entityElement, boolean consumersExpected, ResourceManager resourceManager) {
		return new CachedExpression<Consumer>(new XMLConsumerExpression(entityElement, consumersExpected, resourceManager));
	}

	private InstanceDescriptor mapDescriptorElement(Element element, BeneratorContext context) {
		
		// evaluate type
		String type = parseStringAttribute(element, ATT_TYPE, context, false);
		TypeDescriptor localType;
		if (PrimitiveType.ARRAY.getName().equals(type) 
				|| XMLUtil.getChildElements(element, false, EL_VALUE).length > 0)
			localType = new ArrayTypeDescriptor(element.getAttribute(ATT_NAME));
		else {
			TypeDescriptor parentType = dataModel.getTypeDescriptor(type);
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
				else
					localType.setDetailValue(attributeName, attributeValue);
			}
		}
		
		DescriptorUtil.parseComponentConfig(element, instance.getLocalType(), context);
		return instance;
	}

	enum Stage {
		VARS, MEMBERS, OTHERS
	}
	
}
