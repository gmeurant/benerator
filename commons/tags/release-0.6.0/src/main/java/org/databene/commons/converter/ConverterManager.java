/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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
import org.databene.commons.ConfigurationError;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.IOUtil;
import org.databene.commons.BeanUtil;
import org.databene.commons.LogCategories;
import org.databene.commons.OrderedMap;
import org.databene.commons.Patterns;
import org.databene.commons.ReaderLineIterator;
import org.databene.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Manages converters. A default configuration is provided and can be overwritten by a local file 'converters.txt',
 * that lists each converter's class name, one name per line, e.g.
 * <pre>
 *     com.my.MyString2ThingConverter
 *     com.my.MyString2ComplexConverter
 * </pre
 * <br/>
 * Created: 04.08.2007 19:43:17
 * @author Volker Bergmann
 */
@SuppressWarnings("unchecked")
public class ConverterManager {

	private static final Logger configLogger = LoggerFactory.getLogger(LogCategories.CONFIG);

    private static final String DEFAULT_SETUP_FILENAME = "org/databene/commons/converter/converters.txt";
    private static final String CUSTOM_SETUP_FILENAME = "converters.txt";

    private static ConverterManager instance;

    private OrderedMap<ConversionTypes, Class<? extends Converter>> configuredConverterClasses;
    
    private Map<ConversionTypes, Converter> converterPrototypes;

    private ConverterManager() {
        this.configuredConverterClasses = new OrderedMap<ConversionTypes, Class<? extends Converter>>();
        this.converterPrototypes = new HashMap<ConversionTypes, Converter>();
        try {
            if (IOUtil.isURIAvailable(CUSTOM_SETUP_FILENAME))
                readConfigFile(CUSTOM_SETUP_FILENAME);
            else
            	configLogger.debug("No custom converter setup '" + CUSTOM_SETUP_FILENAME + "' found; using defaults.");
            readConfigFile(DEFAULT_SETUP_FILENAME);
        } catch (IOException e) {
            throw new ConfigurationError("Error reading setup file: " + DEFAULT_SETUP_FILENAME);
        }
    }

    public static ConverterManager getInstance() {
        if (instance == null)
            instance = new ConverterManager();
        return instance;
    }

    public <S, T> Converter<S, T> createConverter(Class<S> sourceType, Class<T> targetType) {
        // check preconditions
        if (targetType == null)
            throw new ConversionException("targetType must be specified");

        // check if we already know how to do this conversion
    	ConversionTypes conversionTypes = new ConversionTypes(sourceType, targetType);
		Converter result = converterPrototypes.get(conversionTypes);
    	if (result != null)
    		return cloneIfSupported(result);

    	// we need to investigate...
        result = searchAppropriateConverter(sourceType, targetType);
        // ...and cache the result for future requests
        if (result != null && result.isParallelizable())
        	converterPrototypes.put(conversionTypes, result);
        
        // done
        return result;
    }

	private Converter searchAppropriateConverter(Class sourceType, Class targetType) {
		
		// catch primitive types
        Class<?> wrapperClass = BeanUtil.getWrapper(targetType.getName());
        if (wrapperClass != null)
        	return createConverter(sourceType, wrapperClass);

        Converter result;
	    if (targetType.isAssignableFrom(sourceType) && !targetType.isPrimitive())
            return new NoOpConverter();

        // to string conversion
        if (String.class.equals(targetType)) {
            result = createToStringConverter(sourceType);
            if (result != null)
            	return result;
        }

        // from string conversion
        if (String.class.equals(sourceType)) {
        	result = tryToCreateStringConverter(targetType);
        	if (result != null)
        		return result;
        }

        // from number conversion
        if (Number.class.isAssignableFrom(sourceType) && Number.class.isAssignableFrom(targetType))
            return new NumberToNumberConverter(sourceType, targetType);

        // from boolean conversion
        if (Boolean.class.isAssignableFrom(sourceType)) {
        	result = tryToCreateBooleanConverter(targetType);
            if (result != null)
            	return result;
        }

        if (targetType.isArray())
            return new ToArrayConverter(targetType.getComponentType());

        if (Collection.class.isAssignableFrom(targetType))
            return new ToCollectionConverter(targetType);

        result = tryToCreateFactoryConverter(sourceType, targetType);
        if (result != null)
        	return result;
        else
        	throw new ConversionException("Cannot convert " + sourceType.getName() + 
        			" to " + targetType.getName());
    }

	@SuppressWarnings("cast")
    private Converter tryToCreateStringConverter(Class targetType) {
        if (targetType.getEnumConstants() != null)
            return new String2EnumConverter(targetType);
        else if (targetType == Boolean.class)
            return new String2BooleanConverter(targetType);
        else if (Number.class.isAssignableFrom(targetType))
        	return new String2NumberConverter((Class<Number>) targetType);
        else if (targetType.isArray()) {
        	Class componentType = targetType.getComponentType();
        	if (componentType == byte.class)
        		return new String2ByteArrayConverter();
        	else
        		return new CommaSeparatedListConverter(componentType);
        }
        return null;
    }

	private <T> Converter<?, T> tryToCreateBooleanConverter(Class targetType) {
        if (Number.class.isAssignableFrom(targetType))
            return new Boolean2NumberConverter(targetType);
    	Class<?> wrapperClass = BeanUtil.getWrapper(targetType.getName());
		if (wrapperClass != null && Number.class.isAssignableFrom(wrapperClass))
            return new Boolean2NumberConverter(wrapperClass);
		return null;
    }

	private Converter<?, String> createToStringConverter(Class<?> sourceType) throws ConversionException {
        if (sourceType.isArray()) {
        	Class<?> componentType = sourceType.getComponentType();
			if (componentType == byte.class)
        		return new ByteArrayToBase64Converter();
        	else if (componentType == char.class)
        		return new CharArray2StringConverter();
        	else
        		return new FormatFormatConverter(sourceType, new ArrayFormat(), true);
        } else if (sourceType == Time.class) {
            return new FormatFormatConverter<Time>(Time.class, new SimpleDateFormat(Patterns.DEFAULT_TIME_PATTERN), false);
        } else if (sourceType == Timestamp.class) {
            return new TimestampFormatter();
        } else if (sourceType == Date.class) {
            return new FormatFormatConverter<Time>(Time.class, new SimpleDateFormat(Patterns.DEFAULT_DATETIME_PATTERN), false);
        } else if (sourceType == Class.class) {
            return new Class2StringConverter();
        } else
            return new ToStringMethodInvoker(sourceType);
    }

    private Converter tryToCreateFactoryConverter(Class sourceType, Class targetType) {
    	
    	// find static valueOf() method in target type
        Method valueOfMethod = BeanUtil.findMethod(targetType, "valueOf", sourceType);
        if (valueOfMethod != null && (valueOfMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
            return new StaticTargetClassMethodInvoker(sourceType, targetType, valueOfMethod);

    	// find static getInstance() method in target type
        Method getInstanceMethod = BeanUtil.findMethod(targetType, "getInstance", sourceType);
        if (getInstanceMethod != null && (getInstanceMethod.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
            return new StaticTargetClassMethodInvoker(sourceType, targetType, valueOfMethod);

    	// find target type constructor which takes source type argument
        Constructor constructor = BeanUtil.findConstructor(targetType, sourceType);
        if (constructor != null)
        	return new ConstructorInvoker(sourceType, constructor);

        // find instance method <targetType>Value() in source type
        String methodName = StringUtil.uncapitalize(targetType.getSimpleName()) + "Value";
        Method typeValueMethod = BeanUtil.findMethod(sourceType, methodName, new Class[0]);
        if (typeValueMethod != null && (typeValueMethod.getModifiers() & Modifier.STATIC) == 0)
            return new SourceClassMethodInvoker(sourceType, targetType, valueOfMethod);

        return findPoorConfiguredMatch(sourceType, targetType);
    }

	private Converter findPoorConfiguredMatch(Class<?> srcType, Class dstType) {
        if (srcType == dstType || (dstType.isAssignableFrom(srcType) && !dstType.isPrimitive()))
            return new NoOpConverter();
        for (Map.Entry<ConversionTypes, Class<? extends Converter>> entry : configuredConverterClasses.entrySet()) {
        	ConversionTypes types = entry.getKey();
            if (types.sourceType == srcType && dstType.isAssignableFrom(types.targetType))
            	return BeanUtil.newInstance(entry.getValue());
        }
        return null;
    }

    public void registerConverterClass(Class<? extends Converter> converterClass) {
    	Converter converter = BeanUtil.newInstance(converterClass);
        ConversionTypes types = new ConversionTypes(converter);
        configuredConverterClasses.put(types, converterClass);
        if (converter.isParallelizable())
	        converterPrototypes.put(types, converter);
    }
    
    public static <S, T> Object convertAll(S[] input, Converter<S, T> converter, Class componentType) {
        Object output = Array.newInstance(componentType, input.length);
        for (int i = 0; i < input.length; i++)
            Array.set(output, i, converter.convert(input[i]));
        return output;
    }
    
    public static <SS, TT> Converter<SS, TT>[] cloneIfSupported(Converter<SS, TT>[] prototypes) {
    	Converter[] result = new Converter[prototypes.length];
    	for (int i = 0; i < prototypes.length; i++)
    		result[i] = cloneIfSupported(prototypes[i]);
    	return result;
    }

    public static <SS, TT> Converter<SS, TT> cloneIfSupported(Converter<SS, TT> prototype) {
    	if (prototype.isParallelizable())
    		return BeanUtil.clone(prototype);
    	else if (prototype.isThreadSafe())
    		return prototype;
    	else
    		return new SynchronizedConverterProxy(prototype);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void readConfigFile(String filename) throws IOException {
        ReaderLineIterator iterator = new ReaderLineIterator(IOUtil.getReaderForURI(filename));
        try {
	        while (iterator.hasNext()) {
	            String className = iterator.next();
	            registerConverterClass((Class<? extends Converter>) Class.forName(className));
	        }
        } catch (ClassNotFoundException e) {
	        throw new ConfigurationError(e);
        } finally {
	        iterator.close();
        }
    }

}
