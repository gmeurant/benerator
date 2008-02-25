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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.bean.BeanFactory;
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.NoOpConverter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyDescriptor;

/**
 * Instantiates a JavaBean from a description in Spring-like XML format.<br/>
 * <br/>
 * Created: 19.08.2007 15:12:40
 * @author Volker Bergmann
 */
public class XMLElement2BeanConverter implements Converter<Element, Object> {

    private static final Log logger = LogFactory.getLog(XMLElement2BeanConverter.class);

    private Context context;
    private Converter<String, String> preprocessor;

    public XMLElement2BeanConverter() {
        this(null);
    }

    public XMLElement2BeanConverter(Context context) {
        this(context, new NoOpConverter<String>());
    }

    public XMLElement2BeanConverter(Context context, Converter<String, String> preprocessor) {
        this.context = context;
        this.preprocessor = preprocessor;
    }
    
    // Converter interface ---------------------------------------------------------------------------------------------

    public Class<Object> getTargetType() {
        return Object.class;
    }

    public Object convert(Element element) throws ConversionException {
        return convert(element, context, preprocessor);
    }
    
    // utility methods -------------------------------------------------------------------------------------------------

    public static Object convert(Element element, Context context, Converter<String, String> preprocessor) throws ConversionException {
        if ("idref".equals(element.getNodeName())) {
            if (context != null) {
                String id = element.getAttribute("bean");
                Object result = context.get(id);
                if (result == null)
                    throw new IllegalStateException("Context does not contain an element of id '" + id + "'");
                return result;
            } else
                throw new IllegalArgumentException("'idref' without a Context");
        } else
            return convertBean(element, context, preprocessor);
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    private static Object convertBean(Element element, Context context, Converter<String, String> preprocessor) throws ConversionException {
        String className = element.getAttribute("class");
        logger.debug("instantiating class '" + className + "'");
        Class beanClass = BeanUtil.forName(className);
        NodeList propertyElements = element.getElementsByTagName("property");
        Map<String, Object> props = new HashMap<String, Object>(propertyElements.getLength());
        for (int i = 0; i < propertyElements.getLength(); i++) {
            Element propertyElement = (Element)propertyElements.item(i);
            String propertyName = propertyElement.getAttribute("name");
            Object propertyValue;
            if (propertyElement.hasAttribute("value")) {
                propertyValue = preprocessor.convert(propertyElement.getAttribute("value"));
            } else if (propertyElement.hasAttribute("ref")) {
                String ref = preprocessor.convert(propertyElement.getAttribute("ref"));
                propertyValue = context.get(ref);
            } else {
                NodeList childNodes = propertyElement.getChildNodes();
                List subElements = new ArrayList();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    if (!(node instanceof Element))
                        continue;
                    Element childElement = (Element) node;
                    subElements.add(convert(childElement, context, preprocessor));
                }
                if (subElements.size() == 0)
                    throw new ConfigurationError("No valid property spec in: " + XMLUtil.format(element));
                PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(beanClass, propertyName);
                Class<?> propertyType = propertyDescriptor.getPropertyType();
                propertyValue = AnyConverter.convert(subElements, propertyType);
            }
            props.put(propertyName, propertyValue);
        }
        return BeanFactory.newBean(className, props);
    }
    
}
