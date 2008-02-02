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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.UpdateFailedException;

import java.util.Properties;
import java.util.Map;

/**
 * Converter implementation that takes a Properties object as argument, 
 * instantiates a class of predefined name and sets all properties 
 * according to the entries of the Properties object.<br/>
 * <br/>
 * Created: 09.08.2007 19:28:36
 * @author Volker Bergmann
 */
public class Properties2BeanConverter<E> implements Converter<Properties, E> {

    private Class<E> beanClass;

    public Properties2BeanConverter(Class<E> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<E> getTargetType() {
        return beanClass;
    }

    public E convert(Properties sourceValue) throws ConversionException {
        return convert(sourceValue, beanClass);
    }

    public static <T> T convert(Properties props, Class<T> targetClass) throws ConversionException {
        try {
            T bean = BeanUtil.newInstance(targetClass);
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                String propertyName = (String) entry.getKey();
                if ("class".equals(propertyName))
                    continue;
                PropertyMutator propertyMutator = PropertyMutatorFactory.getPropertyMutator(targetClass, propertyName, false);
                propertyMutator.setValue(bean, entry.getValue());
            }
            return bean;
        } catch (UpdateFailedException e) {
            throw new ConversionException(e);
        }
    }
}
