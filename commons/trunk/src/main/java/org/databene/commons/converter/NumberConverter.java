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

import org.databene.commons.ConversionException;

/**
 * Converts Numbers to other types.<br/>
 * <br/>
 * Created: 16.06.2007 11:31:43
 * @since 0.2
 * @author Volker Bergmann
 */
public class NumberConverter<T> extends FixedSourceTypeConverter<Number, T> {

    public NumberConverter(Class<T> targetType) {
        super(Number.class, targetType);
    }

    public T convert(Number sourceValue) throws ConversionException {
        return convert(sourceValue, targetType);
    }

    /**
     * Converts a number object to a target type.
     * @param src the number to convert
     * @param targetType the target type of the conversion
     * @return an object of the target type
     */
    @SuppressWarnings("unchecked")
    public static <TT> TT convert(Number src, Class<TT> targetType) {
    	if (src == null)
    		return null;
        if (String.class.equals(targetType))
            return (TT) ToStringConverter.convert(src, null);
        else if (Number.class.isAssignableFrom(targetType) || targetType.isPrimitive())
            return (TT) NumberToNumberConverter.convert(src, (Class<Number>) targetType);
        else
            throw new UnsupportedOperationException("Can't convert " + src.getClass() + " to " + targetType);
    }

}
