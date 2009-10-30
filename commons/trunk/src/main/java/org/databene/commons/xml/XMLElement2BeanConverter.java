/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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
import org.databene.commons.bean.ClassProvider;
import org.databene.commons.bean.DefaultClassProvider;
import org.databene.commons.converter.FixedSourceTypeConverter;
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.NoOpConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.List;
import java.util.ArrayList;
import java.beans.PropertyDescriptor;

/**
 * Instantiates a JavaBean from a description in Spring-like XML format.<br/>
 * <br/>
 * Created: 19.08.2007 15:12:40
 * @author Volker Bergmann
 */
public class XMLElement2BeanConverter extends FixedSourceTypeConverter<Element, Object> { // TODO remove?

    private static final Logger logger = LoggerFactory.getLogger(XMLElement2BeanConverter.class);

	private static final ClassProvider DEFAULT_CLASS_PROVIDER = new DefaultClassProvider();

    private Context context;
    private Converter<? super String, Object> preprocessor;

    public XMLElement2BeanConverter() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public XMLElement2BeanConverter(Context context) {
        this(context, new NoOpConverter());
    }

    public XMLElement2BeanConverter(Context context, Converter<? super String, Object> preprocessor) {
    	super(Element.class, Object.class);
        this.context = context;
        this.preprocessor = preprocessor;
    }
    
    // Converter interface ---------------------------------------------------------------------------------------------

    public Object convert(Element element) throws ConversionException {
        return convert(element, context, preprocessor);
    }
    
    // utility methods -------------------------------------------------------------------------------------------------

    public static Object convert(Element element, Context context, Converter<? super String, Object> preprocessor) throws ConversionException {
    	return convert(element, context, preprocessor, DEFAULT_CLASS_PROVIDER);
    }
    
    public static Object convert(Element element, Context context, Converter<? super String, Object> preprocessor, ClassProvider factory) throws ConversionException {
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
            return convertBean(element, context, preprocessor, factory);
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    @SuppressWarnings("unchecked")
	private static Object convertBean(Element element, Context context, Converter<? super String, Object> preprocessor, ClassProvider factory) throws ConversionException {
        String className = element.getAttribute("class");
        logger.debug("instantiating class '" + className + "'");
        Class beanClass = factory.forName(className);
        Object bean = BeanUtil.newInstance(beanClass);
        mapPropertyElements(element, context, preprocessor, factory, bean);
        return bean;
    }

	public static void mapPropertyElements(Element element, Context context,
            Converter<? super String, Object> preprocessor, ClassProvider factory, Object bean) {
	    Element[] propertyElements = XMLUtil.getChildElements(element, false, "property");
        for (Element propertyElement : propertyElements) {
            String propertyName = propertyElement.getAttribute("name");
            Object propertyValue;
            if (propertyElement.hasAttribute("value")) {
                propertyValue = preprocessor.convert(propertyElement.getAttribute("value"));
            } else if (propertyElement.hasAttribute("ref")) {
                String ref = String.valueOf(preprocessor.convert(propertyElement.getAttribute("ref")));
                propertyValue = context.get(ref);
            } else { // map child elements to a collection or array
                Element[] childElements = XMLUtil.getChildElements(propertyElement);
                List<Object> subElements = new ArrayList<Object>(childElements.length);
                for (Element childElement : childElements)
                    subElements.add(convert(childElement, context, preprocessor, factory)); // TODO this fails if nested beans have a spec="new ..."
                if (subElements.size() == 0)
                    throw new ConfigurationError("No valid property spec in: " + XMLUtil.format(element));
                PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(bean.getClass(), propertyName, true);
                Class<?> propertyType = propertyDescriptor.getPropertyType();
                propertyValue = AnyConverter.convert(subElements, propertyType);
            }
            BeanUtil.setPropertyValue(bean, propertyName, propertyValue, true, true);
        }
    }
    
}
