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

package org.databene.model.converter;

import org.databene.model.Converter;
import org.databene.commons.ArrayUtil;
import org.databene.commons.BeanUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Converts arrays and collections to arrays and other object to an array of size 1.<br/>
 * <br/>
 * Created: 26.08.2007 16:01:38
 */
public class ToArrayConverter implements Converter {

    private Class componentType;
    private Class arrayType;

    public ToArrayConverter() {
        this(Object.class);
    }

    public ToArrayConverter(Class componentType) {
        this.componentType = componentType;
        this.arrayType = ArrayUtil.arrayTypeOfComponent(componentType);
    }

    public Class getTargetType() {
        return arrayType;
    }

    public Object convert(Object sourceValue) {
        return convert(sourceValue, componentType);
    }

    public static Object convert(Object sourceValue, Class componentType) {
        if (sourceValue instanceof Collection) {
            Collection col = (Collection) sourceValue;
            Object[] array = (Object[]) Array.newInstance(componentType, col.size());
            int count = 0;
            for (Object item : col)
                array[count++] = item;
            return array;
        } else if (componentType == byte.class) {
            Method method = BeanUtil.getMethod(sourceValue.getClass(), "getBytes");
            if (method != null)
                return BeanUtil.invoke(sourceValue, method);
            else
                throw new UnsupportedOperationException("Conversion not supported: " + sourceValue.getClass() + " -> " + componentType + "[]");
        } else
            return ArrayUtil.toArray(componentType, sourceValue);
    }

}
