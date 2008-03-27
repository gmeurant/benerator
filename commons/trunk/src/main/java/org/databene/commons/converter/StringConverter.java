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

import org.databene.commons.ArrayFormat;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

import java.util.Date;

/**
 * Converts Strings to anything else.<br/>
 * <br/>
 * Created: 16.06.2007 11:24:08
 * @author Volker Bergmann
 */
public class StringConverter<T> implements Converter<String, T> {

    public Class<T> targetType;

    public StringConverter(Class<T> targetType) {
        this.targetType = targetType;
    }

    public Class<T> getTargetType() {
        return targetType;
    }

    public T convert(String sourceValue) throws ConversionException {
        return convert(sourceValue, targetType);
    }

    /**
     * Converts a String to an arbitrary type.
     */
    public static <T> T convert(String src, Class<T> targetType) throws ConversionException{
        if (src.length() == 0 && Number.class.isAssignableFrom(targetType))
            return null; // map empty Strings to null Numbers
        if (targetType == char.class || targetType == Character.class)
            if (src.length() == 1)
                return (T) Character.valueOf(src.charAt(0));
            else
                throw new ConversionException("'" + src + "' cannot be converted to a character");
        if (targetType.getEnumConstants() != null)
            return (T) String2EnumConverter.convert(src, (Class<Enum>) targetType);
        if (Date.class.isAssignableFrom(targetType))
            return (T) String2DateConverter.convert(src, (Class<? extends Date>) targetType);
        if (targetType.isArray())
            return (T) ArrayFormat.parse(src, ",", targetType.getComponentType());
        // Try to convert to wrapper class
        Class<? extends Number> wrapperClass = JavaType.getWrapperClass(targetType);
        T result = null;
        if (wrapperClass != null)
            result = (T) FactoryConverter.convert(src, wrapperClass);
        if (result != null)
            return result;
        result = FactoryConverter.convert(src, targetType);
        if (result != null)
            return result;
        throw new UnsupportedOperationException("Don't know how to convert '" + src + "' to " + targetType);
    }


}
