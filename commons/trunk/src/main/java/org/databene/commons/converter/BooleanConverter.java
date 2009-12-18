/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConversionException;

/**
 * Converts booleans to other types.<br/><br/>
 * Created: 31.10.2009 07:58:37
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class BooleanConverter<E> extends FixedSourceTypeConverter<Boolean, E> {

    public BooleanConverter(Class<E> targetType) {
	    super(Boolean.class, targetType);
    }

	public E convert(Boolean sourceValue) throws ConversionException {
	    return convert(sourceValue, targetType);
    }

	@SuppressWarnings("unchecked")
    public static <T> T convert(Boolean sourceValue, Class<T> targetType) throws ConversionException {
		if (sourceValue == null || Boolean.class == targetType || boolean.class == targetType)
            return (T) sourceValue;
		else if (targetType == String.class)
			return (T) sourceValue.toString();
        else if (Number.class.isAssignableFrom(targetType))
            return NumberConverter.convert((sourceValue ? 1 : 0), targetType);
        else {
        	Class<?> wrapper = BeanUtil.getWrapper(targetType.getName());
			if (wrapper != null && Number.class.isAssignableFrom(wrapper))
	            return NumberConverter.convert((sourceValue ? 1 : 0), targetType);
	        else // TODO support configured and heuristic conversion
	            throw new UnsupportedOperationException("Can't convert " + sourceValue.getClass() + 
	            		" to " + targetType);
        }
    }

}
