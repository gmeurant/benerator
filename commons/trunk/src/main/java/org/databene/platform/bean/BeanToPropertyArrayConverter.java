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

package org.databene.platform.bean;

import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

/**
 * Converter implementation that extracts all property values of a JavaBean to a Properties object.<br/>
 * <br/>
 * Created: 07.06.2007 14:11:58
 * @author Volker Bergmann
 */
public class BeanToPropertyArrayConverter<E> implements Converter<E, Object[]> {

    private PropertyAccessor<E, ? extends Object>[] accessors;

    public BeanToPropertyArrayConverter(String ... propertyNames) {
        this(null, propertyNames);
    }

    public BeanToPropertyArrayConverter(Class<E> beanClass, String ... propertyNames) {
        this.accessors = new PropertyAccessor[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++)
            this.accessors[i] = PropertyAccessorFactory.getAccessor(beanClass, propertyNames[i]);
    }

    public Class<Object[]> getTargetType() {
        return Object[].class;
    }

    public Object[] convert(E bean) throws ConversionException {
        Object[] propertyValues = new Object[accessors.length];
        for (int i = 0; i < accessors.length; i++)
            propertyValues[i] = accessors[i].getValue(bean);
        return propertyValues;
    }
}
