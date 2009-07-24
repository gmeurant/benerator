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

package org.databene.commons.converter;

import org.databene.commons.ConversionException;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Converts Number objects of one type to another Number type.<br/>
 * <br/>
 * Created: 16.06.2007 11:51:14
 */
public class NumberToNumberConverter<S extends Number, T extends Number> extends FixedSourceTypeConverter<S, T> {

    public NumberToNumberConverter(Class<S> sourceType, Class<T> targetType) {
        super(sourceType, targetType);
    }

    public T convert(S sourceValue) throws ConversionException {
        return convert(sourceValue, targetType);
    }

    /**
     * Converts a number of one number type to another number type.
     * @param src the number to convert
     * @param targetType the target number type of the conversion
     * @return an object of the target number type
     */
    @SuppressWarnings("unchecked")
    public static <TT extends Number> TT convert(Number src, Class<TT> targetType) {
        if (BigInteger.class.equals(targetType))
            return (TT) BigInteger.valueOf(src.longValue());
        else if (BigDecimal.class.equals(targetType))
            return (TT) BigDecimal.valueOf(src.doubleValue());
        else {
            Class<? extends Number> primitiveClass = JavaType.getPrimitiveClass(targetType);
            return (TT) convertNumberToPrimitive(src, primitiveClass);
        }
    }

    /**
     * Converts a number object to an instance of a primitive class
     * (returned as the respective number wrapper).
     * @param src the object to convert
     * @param targetType the primitive target type of the conversion
     * @return an object of the primitive target type
     */
    private static Object convertNumberToPrimitive(Number src, Class<?> targetType) {
        if (int.class.equals(targetType))
            return src.intValue();
        else if (byte.class.equals(targetType))
            return src.byteValue();
        else if (short.class.equals(targetType))
            return src.shortValue();
        else if (long.class.equals(targetType))
            return src.longValue();
        else if (float.class.equals(targetType))
            return src.floatValue();
        else if (double.class.equals(targetType))
            return src.doubleValue();
        else
            throw new UnsupportedOperationException("Can't convert '" + src + "' to " + targetType);
    }

}
