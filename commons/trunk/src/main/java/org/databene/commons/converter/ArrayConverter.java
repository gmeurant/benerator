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

package org.databene.commons.converter;

import org.databene.commons.ArrayUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

/**
 * Converts arrays from one component type to arrays of another component type.<br/>
 * <br/>
 * Created: 07.06.2007 14:35:18
 * @author Volker Bergmann
 */
public class ArrayConverter<S, T> extends FixedSourceTypeConverter<S[], T[]> {

    private Class<T> componentType;
    private Class<T[]> arrayType;
    private Converter<S, T>[] converters;

    public ArrayConverter(Class<S> sourceComponentType, Class<T> targetComponentType, Converter<S, T> ... converters) {
    	super(ArrayUtil.arrayType(sourceComponentType), ArrayUtil.arrayType(targetComponentType));
        this.componentType = targetComponentType;
        this.arrayType = ArrayUtil.arrayType(componentType);
        this.converters = converters;
    }

    public T[] convert(S[] sourceValues) throws ConversionException {
        if (sourceValues == null)
            return null;
        if (converters.length == 1)
        	return convert(sourceValues, converters[0], componentType);
        else {
            T[] result = ArrayUtil.newInstance(componentType, converters.length);
            for (int i = 0; i < converters.length; i++)
                result[i] = converters[i].convert(sourceValues[i]);
            return result;
        }
    }

    public static <S, T> T[] convert(S[] sourceValues, Converter<S, T> converter, Class<T> componentType) throws ConversionException {
        T[] result = ArrayUtil.newInstance(componentType, sourceValues.length);
        for (int i = 0; i < sourceValues.length; i++)
            result[i] = (converter != null ? converter.convert(sourceValues[i]) : (T) sourceValues[i]);
        return result;
    }
}
