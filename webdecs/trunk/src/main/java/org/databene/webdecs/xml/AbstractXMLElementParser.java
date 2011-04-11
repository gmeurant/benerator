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

import java.util.Map;
import java.util.Set;

import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.StringUtil;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 * Abstract implementation of the {@link XMLElementParser} interface.<br/><br/>
 * Created: 05.12.2010 10:46:38
 * @since 0.5.4
 * @author Volker Bergmann
 */
public abstract class AbstractXMLElementParser<E> implements XMLElementParser<E> {
	
	protected final String elementName;
	protected final Set<Class<?>> supportedParentTypes;
	protected Set<String> supportedAttributes;



	public AbstractXMLElementParser(String elementName, Set<String> supportedAttributes, Class<?>... supportedParentTypes) {
		this.elementName = elementName;
		this.supportedAttributes = supportedAttributes;
		this.supportedParentTypes = CollectionUtil.toSet(supportedParentTypes);
	}

	public boolean supports(Element element, E[] parentPath) {
		if (!this.elementName.equals(element.getNodeName()))
			return false;
		return this.supportedParentTypes.isEmpty() || 
			this.supportedParentTypes.contains(ArrayUtil.lastElement(parentPath).getClass());
	}

	protected void checkAttributeSupport(Element element) {
		for (String attributeName : XMLUtil.getAttributes(element).keySet()) {
			if (!supportedAttributes.contains(attributeName))
				throw new ConfigurationError("<" + element.getNodeName() + "> does not support attribute '" + attributeName + "'");
		}
	}

	protected static void assertElementName(String expectedName, Element element) {
		if (!element.getNodeName().equals(expectedName))
			throw new ConfigurationError("Expected element name '" + expectedName + "', found: '" + element.getNodeName());
	}

	protected Object parent(E[] parentPath) {
		if (ArrayUtil.isEmpty(parentPath))
			return null;
		else
			return ArrayUtil.lastElement(parentPath);
	}

	protected static String parseRequiredName(Element element) {
		String name = parseOptionalName(element);
		if (StringUtil.isEmpty(name))
			throw new ConfigurationError("No 'name' attribute specified in element " + XMLUtil.format(element));
		return name;
	}

	protected static String parseOptionalName(Element element) {
		return getOptionalAttribute("name", element);
	}

	public static String getRequiredAttribute(String name, Element element) {
		String value = getOptionalAttribute(name, element);
		if (value == null)
			throw new ConfigurationError("'" + name + "' attribute expected in element " + XMLUtil.format(element));
		return value;
	}

	protected static String getOptionalAttribute(String name, Element element) {
		return StringUtil.emptyToNull(element.getAttribute(name));
	}

	protected static void checkAttributes(Element element, Set<String> supportedAttributes) {
	    for (Map.Entry<String, String> attribute : XMLUtil.getAttributes(element).entrySet()) {
	        if (!supportedAttributes.contains(attribute.getKey()))
				throw new ConfigurationError("Not a supported import attribute: " + attribute.getKey());
        }
    }

}
