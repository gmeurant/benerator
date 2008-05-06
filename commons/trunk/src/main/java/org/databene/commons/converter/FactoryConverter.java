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
import org.databene.commons.Converter;
import org.databene.commons.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Tries heuristic type conversion by the following strategies:
 * <ol>
 *   <li>method TargetType.valueOf(SourceType)</li>
 *   <li>method TargetType.getInstance(SourceType)</li>
 *   <li>constructor TargetType(SourceType)</li>
 *   <li>method SourceType.targetTypeValue</li>
 * </ol>
 * <br/>
 * <br/>
 * Created: 03.09.2007 20:17:16
 * @author Volker Bergmann
 */
public class FactoryConverter<S, T> implements Converter<S, T> {

    private static final Log logger = LogFactory.getLog(FactoryConverter.class);

    private Class<T> targetType;

    public FactoryConverter(Class<T> targetType) {
        this.targetType = targetType;
    }

    public Class<T> getTargetType() {
        return targetType;
    }

    public T convert(S src) throws ConversionException {
        return convert(src, targetType);
    }

    public static <TT> TT convert(Object src, Class<TT> targetType) throws ConversionException {
        if (src == null)
            return null;
        if (targetType.isAssignableFrom(src.getClass()))
            return (TT) src;
        TT result = tryToConstructByValueOfMethod(src, targetType);
        if (result == null)
            result = tryToConstructByGetInstanceMethod(src, targetType);
        if (result == null)
            result = tryToConstructWithSourceParameter(src, targetType);
        if (result == null)
            result = tryToConstructByTypeValueMethod(src, targetType);
        if (result == null)
            throw new ConversionException("Don't know how to convert '" + src + "' to " + targetType);
        return result;
    }

    /**
     * Tries to create an object from a String parameter
     */
    private static <T> T tryToConstructWithSourceParameter(Object src, Class<T> targetType) {
        try {
            Constructor<T> constructor = targetType.getConstructor(src.getClass());
            return constructor.newInstance(src);
        } catch (NoSuchMethodException e) {
            // no such constructor exists
            if (logger.isDebugEnabled())
                logger.debug("No appropriate constructor found in " + src.getClass());
        } catch (InstantiationException e) {
            logger.debug("Failed to instantiate " + targetType + " by constructor " + targetType.getSimpleName() + '(' + src.getClass().getName() + ')' + e);
        } catch (IllegalAccessException e) {
            // obviously, the constructor is not intended to be invoked from foreign classes/packages
            if (logger.isDebugEnabled())
                logger.debug("Constructor not visible");
        } catch (InvocationTargetException e) {
            logger.debug("Failed to instantiate " + targetType + " by constructor " + targetType.getSimpleName() + '(' + src.getClass().getName() + ')' + e);
        }
        return null;
    }

    /** Tries to create an object by a static valueOf() method of the target class */
    private static <T> T tryToConstructByValueOfMethod(Object src, Class<T> targetType) {
        try {
            Method valueOfMethod = targetType.getMethod("valueOf", src.getClass());
            if ((valueOfMethod.getModifiers() & Modifier.STATIC) == 0)
                return null;
            return (T) valueOfMethod.invoke(null, src);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /** Tries to create an object by a static valueOf() method of the target class */
    private static <T> T tryToConstructByGetInstanceMethod(Object src, Class<T> targetType) {
        try {
            Method valueOfMethod = targetType.getMethod("getInstance", src.getClass());
            if ((valueOfMethod.getModifiers() & Modifier.STATIC) == 0)
                return null;
            return (T) valueOfMethod.invoke(null, src);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T tryToConstructByTypeValueMethod(Object src, Class<T> targetType) {
        try {
            String methodName = StringUtil.uncapitalize(targetType.getSimpleName()) + "Value";
            Method typeValueMethod = src.getClass().getMethod(methodName);
            if ((typeValueMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
                return null;
            return (T) typeValueMethod.invoke(src);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
