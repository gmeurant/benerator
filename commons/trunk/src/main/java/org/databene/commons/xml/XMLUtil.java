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

package org.databene.commons.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.commons.ArrayBuilder;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.ErrorHandler;
import org.databene.commons.IOUtil;
import org.databene.commons.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Provides XML Utility methods.<br/>
 * <br/>
 * Created: 25.08.2007 22:09:26
 * @author Volker Bergmann
 */
public class XMLUtil {
    
    private static final ErrorHandler DEFAULT_ERROR_HANDLER = new ErrorHandler(XMLUtil.class.getSimpleName(), ErrorHandler.Level.fatal);
	private static final Log logger = LogFactory.getLog(XMLUtil.class);
    
    private XMLUtil() {}

    public static String format(Element element) {
        StringBuilder builder = new StringBuilder();
        builder.append('<').append(element.getNodeName());
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attribute = (Attr) attributes.item(i);
            builder.append(' ').append(attribute.getName()).append('=').append(attribute.getValue());
        }
        builder.append("...");
        return builder.toString();
    }
 
    public static String localName(Element element) {
        return localName(element.getNodeName());
    }

    public static String localName(String elementName) {
        if (elementName == null)
            return null;
        int sep = elementName.indexOf(':');
        if (sep < 0)
        	return elementName;
        return elementName.substring(sep + 1);
    }

    public static Element[] getChildElements(Element parent) {
        ArrayBuilder<Element> builder = new ArrayBuilder<Element>(Element.class);
        NodeList childNodes = parent.getChildNodes();
        int n = childNodes.getLength();
        for (int i = 0; i < n; i++) {
            Node item = childNodes.item(i);
            if (item instanceof Element)
                builder.append((Element) item);
        }
        return builder.toArray();
    }
    
    public static Element[] getChildElements(Element parent, boolean namespaceAware, String name) {
        ArrayBuilder<Element> builder = new ArrayBuilder<Element>(Element.class);
        NodeList childNodes = parent.getChildNodes();
        int n = childNodes.getLength();
        for (int i = 0; i < n; i++) {
            Node item = childNodes.item(i);
            if (!(item instanceof Element))
                continue;
            String fqName = item.getNodeName();
            if (namespaceAware) {
                if (fqName.equals(name))
                    builder.append((Element) item);
            } else {
                String[] tokens = StringUtil.tokenize(fqName, ':');
                if (tokens[tokens.length - 1].equals(name))
                    builder.append((Element) item);
            }
        }
        return builder.toArray();
    }

    public static Element getChildElement(Element parent, boolean namespaceAware, boolean required, String name) {
        Element[] elements = getChildElements(parent, namespaceAware, name);
        if (required && elements.length == 0)
            throw new IllegalArgumentException("No element found with name: " + name);
        if (elements.length > 1)
            throw new IllegalArgumentException("More that one element found with name: " + name);
        return (elements.length > 0 ? elements[0] : null);
    }

    public static String getText(Node node) {
        if (node == null)
            return null;
        if (node instanceof Text)
            return node.getNodeValue();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
            if (children.item(i) instanceof Text)
                return children.item(i).getNodeValue();
        return null;
    }

    public static Integer getIntegerAttribute(Element element, String name, Integer defaultValue) {
        if (logger.isDebugEnabled())
            logger.debug("getIntegerAttribute(" + element.getNodeName() + ", " + name + ')');
        String stringValue = element.getAttribute(name);
        if (StringUtil.isEmpty(stringValue))
            return defaultValue;
        return Integer.parseInt(stringValue);
    }

    public static Long getLongAttribute(Element element, String name, long defaultValue) {
        if (logger.isDebugEnabled())
            logger.debug("getLongAttribute(" + element.getNodeName() + ", " + name + ')');
        String stringValue = element.getAttribute(name);
        if (StringUtil.isEmpty(stringValue))
            return defaultValue;
        return Long.parseLong(stringValue);
    }

    public static Map<String, String> getAttributes(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        Map<String, String> result = new HashMap<String, String>();
        int n = attributes.getLength();
        for (int i = 0; i < n; i++) {
            Attr attribute = (Attr) attributes.item(i);
            result.put(attribute.getName(), attribute.getValue());
        }
        return result;
    }

    public static PrintWriter createXMLFile(String uri, String encoding) 
        throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printer = IOUtil.getPrinterForURI(uri, encoding);
        printer.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
        return printer;
    }

    public static String normalizedAttributeValue(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (StringUtil.isEmpty(value))
            value = null;
        return value;
    }

    // XML operations --------------------------------------------------------------------------------------------------

    public static Document parse(String uri) throws IOException {
        return parse(uri, false);
    }

    public static Document parse(String uri, boolean validate) throws IOException {
        InputStream stream = null;
        try {
            stream = IOUtil.getInputStreamForURI(uri);
            return parse(stream, DEFAULT_ERROR_HANDLER, validate);
        } finally {
            IOUtil.close(stream);
        }
    }

    public static Document parseString(String text) throws IOException {
        return parseString(text, false);
    }
        
    public static Document parseString(String text, boolean validate) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug(text);
        return parse(new StringBufferInputStream(text), DEFAULT_ERROR_HANDLER, validate);
    }

    public static Document parse(InputStream stream) throws IOException {
        return parse(stream, DEFAULT_ERROR_HANDLER, false);
    }

    public static Document parse(InputStream stream, final ErrorHandler errorHandler, boolean validate) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(validate);
            factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {

				public void error(SAXParseException e) throws SAXException {
					errorHandler.handleError(e.getMessage(), e);
				}

				public void fatalError(SAXParseException e) throws SAXException {
					errorHandler.handleError(e.getMessage(), e);
				}

				public void warning(SAXParseException e) throws SAXException {
					errorHandler.handleError(e.getMessage(), e);
				}
            	
            });
            return builder.parse(stream);
        } catch (ParserConfigurationException e) {
            throw new ConfigurationError(e);
        } catch (SAXException e) {
            throw new ConfigurationError(e);
        }
    }

    public static NamespaceAlias namespaceAlias(Document document, String namespaceUri) {
        Map<String, String> attributes = XMLUtil.getAttributes(document.getDocumentElement());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String namespaceName = entry.getValue();
            if (namespaceUri.equals(namespaceName)) {
                String def = entry.getKey();
                String alias = (def.contains(":") ? StringUtil.lastToken(def, ':') : "");
				return new NamespaceAlias(alias, namespaceName);
            }
        }
        return new NamespaceAlias("", namespaceUri);
    }

    public static Map<String, String> getNamespaces(Document document) {
    	Map<String, String> namespaces = new HashMap<String, String>();
        Map<String, String> attributes = XMLUtil.getAttributes(document.getDocumentElement());
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            if (attributeName.startsWith("xmlns")) {
                String alias = (attributeName.contains(":") ? StringUtil.lastToken(attributeName, ':') : "");
				namespaces.put(alias, entry.getValue());
            }
        }
        return namespaces;
    }

    public static String getTargetNamespace(Document xsdDocument) {
    	return xsdDocument.getDocumentElement().getAttribute("targetNamespace");
    }

	public static boolean getBooleanAttribute(Element element, String name) {
		return Boolean.parseBoolean(element.getAttribute(name));
	}

	public static double getDoubleAttribute(Element element, String name) {
		return Double.parseDouble(element.getAttribute(name));
	}

	public static void mapAttributesToProperties(Element element, Object bean, boolean strict) {
		for (Map.Entry<String, String> attribute : getAttributes(element).entrySet()) {
			String name = StringUtil.lastToken(attribute.getKey(), ':');
			String value = attribute.getValue();
			Class<? extends Object> type = bean.getClass();
			if (BeanUtil.hasProperty(type, name))
				BeanUtil.setPropertyValue(bean, name, value, false);
		}
	}


}
