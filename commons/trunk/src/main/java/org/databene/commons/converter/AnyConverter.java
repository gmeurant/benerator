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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Converts any source type to any target type. It also makes use of the ConverterManager.<br/>
 * <br/>
 * Created: 16.06.2007 11:34:42
 * @author Volker Bergmann
 */
public class AnyConverter<S, T> extends AbstractFormatter implements Converter<S, T> { // Can't we drop param S?

    private static final Logger logger = LoggerFactory.getLogger(AnyConverter.class);

    private Class<T> targetType;
    
    public AnyConverter(Class<T> targetType) {
        this(targetType, "yyyyMMdd");
    }

    public AnyConverter(Class<T> targetType, String datePattern) {
    	this.targetType = targetType;
        this.datePattern = datePattern;
    }

    public Class<T> getTargetType() {
	    return targetType;
    }

	public boolean canConvert(Object sourceValue) {
		return true;
	}

	public T convert(Object sourceValue) throws ConversionException {
        return convert(sourceValue, targetType, datePattern, timePattern, timestampPattern);
    }

    public static <TT> TT convert(Object source, Class<TT> targetType) throws ConversionException {
        return convert(source, targetType, null, null, null);
    }
    
    /**
     * Converts an object of a given type to an object of the target type.
     * @param source the object to convert
     * @param targetType the target type of the conversion
     * @return an object of the target type
     */
    @SuppressWarnings("unchecked")
    public static <TT> TT convert(Object source, Class<TT> targetType, String datePattern, 
    		String timePattern, String timestampPattern) throws ConversionException {
        if (logger.isDebugEnabled())
            logger.debug("Converting " + source + (source != null ? " (" + source.getClass().getName() + ")" : "") + " to " + targetType);
        // check preconditions
        if (targetType == null)
            throw new ConversionException("targetType must be specified");

        // trivial cases: no conversion necessary
        if (source == null)
            return null;
        if (targetType.isAssignableFrom(source.getClass()) && !targetType.isPrimitive())
            return (TT) source;

        // search for exact converters
        Converter converter = ConverterManager.getInstance().getConverter(source, targetType);
        if (converter != null)
                return (TT)converter.convert(source);

        // to string conversion
        if (String.class.equals(targetType))
            return (TT) ToStringConverter.convert(source, null);

        // from string conversion
        if (String.class.equals(source.getClass()))
            return StringConverter.convert((String) source, targetType);

        // from number conversion
        if (source instanceof Number)
            return NumberConverter.convert((Number) source, targetType);

        // from boolean conversion
        if (source instanceof Boolean)
            return convertBoolean((Boolean) source, targetType);

        if (targetType.isArray())
            return (TT) ToArrayConverter.convert(source, targetType.getComponentType());

        if (Collection.class.isAssignableFrom(targetType))
            return (TT) ToCollectionConverter.convert(source, targetType);

        return FactoryConverter.convert(source, targetType);
    }

    // private hepler methods ------------------------------------------------------------------------------------------

    /**
     * Converts a boolean to a target type.
     * @param src the object to convert
     * @param targetType the target type of the conversion
     * @return an object of the target type
     */
    @SuppressWarnings("unchecked")
    private static <TT> TT convertBoolean(Boolean src, Class<TT> targetType) {
        if (boolean.class.equals(targetType))
            return (TT)src;
        else if (Number.class.isAssignableFrom(targetType))
            return convert((src ? 1 : 0), targetType);
        else if (Number.class.isAssignableFrom(BeanUtil.getWrapper(targetType.getName())))
            return convert((src ? 1 : 0), targetType);
        else
            throw new UnsupportedOperationException("Can't convert " + src.getClass() + " to " + targetType);
    }

}
