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

import org.databene.model.UpdateFailedException;
import org.databene.model.ConversionException;
import org.databene.model.converter.AnyConverter;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;

import java.lang.reflect.Method;

/**
 * Mutates the value of a property on a JavaBean target object.<br/>
 * <br/>
 * Created: 21.07.2007 09:01:19
 */
class UntypedPropertyMutator extends AbstractPropertyMutator{

    private boolean strict;

    public UntypedPropertyMutator(String propertyName, boolean strict) {
        super(propertyName);
        this.strict = strict;
    }

    public void setValue(Object target, Object value) throws UpdateFailedException {
        setValue(target, value, this.strict);
    }

    public void setValue(Object bean, Object propertyValue, boolean strict) {
        if (bean == null)
            if (strict)
                throw new IllegalArgumentException("Cannot set a property on null");
            else
                return;
        Method writeMethod = BeanUtil.getPropertyDescriptor(bean.getClass(), propertyName).getWriteMethod();
        if (writeMethod == null) {
            if (strict)
                throw new ConfigurationError("No write method found for property '" + propertyName + "' in class " + bean.getClass());
            else
                return;
        }
        if (!strict && propertyValue != null) {
            Class sourceType = propertyValue.getClass();
            Class targetType = writeMethod.getParameterTypes()[0];
            try {
                if (!targetType.isAssignableFrom(sourceType))
                    propertyValue = AnyConverter.convert(propertyValue, targetType);
            } catch (ConversionException e) {
                throw new ConfigurationError(e);
            }
        }
        BeanUtil.invoke(bean, writeMethod, propertyValue);
    }
}
