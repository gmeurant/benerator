/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.webdecs.xml;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.databene.commons.ArrayFormat;
import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.databene.commons.xml.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Abstract implementation of the {@link XMLElementParser} interface.<br/><br/>
 * Created: 05.12.2010 10:46:38
 * @since 0.5.4
 * @author Volker Bergmann
 */
public abstract class AbstractXMLElementParser<E> implements XMLElementParser<E> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final String elementName;
	protected final Set<Class<?>> supportedParentTypes;
	protected Set<String> requiredAttributes;
	protected Set<String> optionalAttributes;

	public AbstractXMLElementParser(String elementName, 
			Set<String> requiredAttributes, 
			Set<String> optionalAttributes, 
			Class<?>... supportedParentTypes) {
		this.elementName = elementName;
		this.requiredAttributes = (requiredAttributes != null ? requiredAttributes : Collections.<String>emptySet());
		this.optionalAttributes = (optionalAttributes != null ? optionalAttributes : Collections.<String>emptySet());
		this.supportedParentTypes = CollectionUtil.toSet(supportedParentTypes);
	}

	public boolean supports(Element element, E[] parentPath) {
		if (!this.elementName.equals(element.getNodeName()))
			return false;
		return CollectionUtil.isEmpty(this.supportedParentTypes) || parentPath == null ||
			this.supportedParentTypes.contains(ArrayUtil.lastElementOf(parentPath).getClass());
	}
	
	public final E parse(Element element, E[] parentPath, org.databene.webdecs.xml.ParseContext<E> context) {
		checkAttributeSupport(element);
		return doParse(element, parentPath, context);
	}

	protected abstract E doParse(Element element, E[] parentPath, ParseContext<E> context);

	protected void checkAttributeSupport(Element element) {
		for (String attribute : XMLUtil.getAttributes(element).keySet()) {
			if (!requiredAttributes.contains(attribute) && !optionalAttributes.contains(attribute))
				syntaxError("attribute '" + attribute + "' is not supported", element); // TODO v0.6.x print supported attributes
		}
		for (String requiredAttribute : requiredAttributes) {
			if (StringUtil.isEmpty(element.getAttribute(requiredAttribute)))
				syntaxError("Required attribute '" + requiredAttribute + "' is missing", element);
		}
	}

	protected void checkSupportedAttributes(Element element, String... supportedAttributes) {
		for (String actualAttribute : XMLUtil.getAttributes(element).keySet()) {
			if (!ArrayUtil.contains(actualAttribute, supportedAttributes))
				syntaxError("attribute '" + actualAttribute + "' is not supported", element);
		}
	}

	protected void assertElementName(String expectedName, Element element) {
		if (!element.getNodeName().equals(expectedName))
			throw new RuntimeException("Expected element name '" + expectedName + "', " +
					"found: '" + element.getNodeName());
	}

	protected void excludeAttributes(Element element, String... attributeNames) {
		String usedAttribute = null;
		for (String attributeName : attributeNames) {
			if (!StringUtil.isEmpty(element.getAttribute(attributeName))) {
				if (usedAttribute == null)
					usedAttribute = attributeName;
				else
					syntaxError("The attributes '" + usedAttribute + "' and '" + attributeName + "' " +
							"exclude each other", element);
			}
		}
	}

	protected void assertAtLeastOneAttributeIsSet(Element element, String... attributeNames) {
		boolean ok = false;
		for (String attributeName : attributeNames)
			if (!StringUtil.isEmpty(element.getAttribute(attributeName)))
				ok = true;
		if (!ok)
			syntaxError("At least one of these attributes must be set: " + ArrayFormat.format(attributeNames), element);
	}

	protected void assertAttributeIsSet(Element element, String attributeName) {
		if (StringUtil.isEmpty(element.getAttribute(attributeName)))
			syntaxError("Attribute '" + attributeName + "' is missing", element);
	}

	protected void assertAttributeIsNotSet(Element element, String attributeName) {
		if (!StringUtil.isEmpty(element.getAttribute(attributeName)))
			syntaxError("Attributes '" + attributeName + "' must not be set", element);
	}

	protected Object parent(E[] parentPath) {
		if (ArrayUtil.isEmpty(parentPath))
			return null;
		else
			return ArrayUtil.lastElementOf(parentPath);
	}

	protected String parseRequiredName(Element element) {
		String name = parseOptionalName(element);
		if (StringUtil.isEmpty(name))
			syntaxError("'name' attribute is missing", element);
		return name;
	}

	protected String parseOptionalName(Element element) {
		return getOptionalAttribute("name", element);
	}

	protected Integer parseOptionalInteger(String attributeName, Element element) {
		String spec = getOptionalAttribute(attributeName, element);
		return (spec != null ? Integer.parseInt(spec) : null);
	}

	protected Boolean parseOptionalBoolean(String attributeName, Element element) {
		String spec = getOptionalAttribute(attributeName, element);
		return (spec != null ? Boolean.parseBoolean(spec) : null);
	}

	protected Long parseOptionalLong(String attributeName, Element element) {
		String spec = getOptionalAttribute(attributeName, element);
		return (spec != null ? Long.parseLong(spec) : null);
	}

	public String getRequiredAttribute(String name, Element element) {
		String value = getOptionalAttribute(name, element);
		if (value == null)
			syntaxError("'" + name + "' attribute expected", element);
		return value;
	}

	protected String getOptionalAttribute(String name, Element element) {
		return StringUtil.emptyToNull(element.getAttribute(name));
	}

	protected void checkAttributes(Element element, Set<String> supportedAttributes) {
	    for (Map.Entry<String, String> attribute : XMLUtil.getAttributes(element).entrySet()) {
	        if (!supportedAttributes.contains(attribute.getKey()))
				throw new ConfigurationError("Not a supported import attribute: " + attribute.getKey());
        }
    }

	protected static SyntaxError createSyntaxError(String message, Element element) {
		return new SyntaxError("Syntax error: " + message, XMLUtil.format(element));
	}
	
	protected static SyntaxError createSyntaxError(String message, Element element, Exception cause) {
		return new SyntaxError("Syntax error: " + message, cause, XMLUtil.format(element), -1, -1);
	}
	
	protected static void syntaxError(String message, Element element) {
		throw new SyntaxError("Syntax error: " + message, XMLUtil.format(element));
	}
	
	protected void syntaxWarning(String message, Element element) {
		logger.warn("Syntax warning: " + message + " in " + XMLUtil.format(element));
	}
	
}
