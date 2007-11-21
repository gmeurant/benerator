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
import org.databene.model.ConversionException;
import org.databene.commons.ArrayUtil;

import java.lang.reflect.Array;

/**
 * Converts arrays from one component type to arrays of another component type.<br/>
 * <br/>
 * Created: 07.06.2007 14:35:18
 */
public class ArrayConverter<S, T> implements Converter<S[], T[]> {

    private Class<T> componentType;
    private Class<T[]> arrayType;
    private Converter<S, T>[] converters;

    public ArrayConverter(Class<T> targetClass, Converter<S, T> ... converters) {
        this.componentType = targetClass;
        this.arrayType = ArrayUtil.arrayTypeOfComponent(componentType);
        this.converters = converters;
    }

    public Class<T[]> getTargetType() {
        return arrayType;
    }

    public T[] convert(S[] sourceValues) throws ConversionException {
        if (converters.length == 1) {
            T[] result = (T[]) Array.newInstance(componentType, sourceValues.length);
            for (int i = 0; i < sourceValues.length; i++)
                result[i] = converters[0].convert(sourceValues[i]);
            return result;
        } else {
            T[] result = (T[]) Array.newInstance(componentType, converters.length);
            for (int i = 0; i < converters.length; i++)
                result[i] = converters[i].convert(sourceValues[i]);
            return result;
        }
    }
}
