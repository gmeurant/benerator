/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.converter.FixedSourceTypeConverter;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * Maps element names to JavaBean class names and attributes to JavaBean properties.<br/>
 * <br/>
 * Created: 29.01.2008 14:35:40
 * @since 0.4.0
 * @author Volker Bergmann
 */
public class FlatXML2BeanConverter extends FixedSourceTypeConverter<Element, Object> {

    private Map<String, Class<? extends Object>> types;
    
    public FlatXML2BeanConverter() {
    	super(Element.class, Object.class);
        this.types = new HashMap<String, Class<? extends Object>>();
    }
    
    public void addMapping(String name, Class<? extends Object> type) {
        types.put(name, type);
    }

    public Object convert(Element element) {
        String elementName = element.getNodeName();
        Class<? extends Object> type = types.get(elementName);
        if (type == null)
            throw new ConfigurationError("Element type not mapped: " + elementName);
        Object bean = BeanUtil.newInstance(type);
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attribute = (Attr) attributes.item(i);
            String propertyName = attribute.getName();
            String propertyValue = attribute.getValue();
            BeanUtil.setPropertyValue(bean, propertyName, propertyValue, false);
        }
        return bean;
    }

}
