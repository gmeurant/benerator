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

package org.databene.commons;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.converter.ArrayTypeConverter;
import org.databene.commons.converter.ToStringConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.Callable;
import java.beans.Introspector;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.PrintWriter;

/**
 * Bundles reflection and introspection related operations.<br/><br/>
 * Created: 01.07.2006 08:44:33
 * @since 0.1
 * @author Volker Bergmann
 */
public final class BeanUtil {

    private static final Log logger = LogFactory.getLog(BeanUtil.class);
    
    private static Escalator escalator = new LoggerEscalator();

    // (static) attributes ---------------------------------------------------------------------------------------------

    private static final Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<String, PropertyDescriptor>();

    /**
     * List of simple Java types.
     */
    private static final Class<?>[] simpleTypes = {
        String.class,
        long.class,       Long.class,
        int.class,        Integer.class,
        short.class,      Short.class,
        byte.class,       Byte.class,
        boolean.class,    Boolean.class,
        char.class,       Character.class,
        float.class,      Float.class,
        double.class,     Double.class,
        BigDecimal.class, BigInteger.class
    };

    private static final Class<?>[] integralNumberTypes = {
        long.class,       Long.class,
        int.class,        Integer.class,
        short.class,      Short.class,
        byte.class,       Byte.class,
        BigInteger.class
    };

    private static final Class<?>[] decimalNumberTypes = {
        float.class,      Float.class,
        double.class,     Double.class,
        BigDecimal.class
    };

    private static final PrimitiveTypeMapping[] primitiveNumberTypes = {
        new PrimitiveTypeMapping(long.class, Long.class),
        new PrimitiveTypeMapping(int.class, Integer.class),
        new PrimitiveTypeMapping(short.class, Short.class),
        new PrimitiveTypeMapping(byte.class, Byte.class),
        new PrimitiveTypeMapping(float.class, Float.class),
        new PrimitiveTypeMapping(double.class, Double.class)
    };

    private static final PrimitiveTypeMapping[] primitiveNonNumberTypes = {
        new PrimitiveTypeMapping(boolean.class, Boolean.class),
        new PrimitiveTypeMapping(char.class, Character.class),
    };

    /**
     * Map of integral Java number types
     */
    private static Map<String, Class<?>> integralNumberTypeMap;

    /**
     * Map of decimal Java number types
     */
    private static Map<String, Class<?>> decimalNumberTypeMap;

    /**
     * Map of simple Java types
     */
    private static Map<String, Class<?>> simpleTypeMap;

    /**
     * Map of primitive Java types
     */
    private static Map<String, Class<?>> primitiveTypeMap;

    /**
     * Map of primitive Java number types
     */
    private static Map<String, Class<?>> primitiveNumberTypeMap;

    // initialization --------------------------------------------------------------------------------------------------

    static {
        simpleTypeMap = map(simpleTypes);
        integralNumberTypeMap = map(integralNumberTypes);
        decimalNumberTypeMap = map(decimalNumberTypes);
        primitiveNumberTypeMap = new HashMap<String, Class<? extends Object>>();
        primitiveTypeMap = new HashMap<String, Class<? extends Object>>();
        for (PrimitiveTypeMapping mapping : primitiveNumberTypes) {
            primitiveNumberTypeMap.put(mapping.primitiveType.getName(), mapping.wrapperType);
            primitiveTypeMap.put(mapping.primitiveType.getName(), mapping.wrapperType);
        }
        for (PrimitiveTypeMapping mapping : primitiveNonNumberTypes)
            primitiveTypeMap.put(mapping.primitiveType.getName(), mapping.wrapperType);
    }

	private static Map<String, Class<?>> map(Class<?>[] array) {
		Map<String, Class<?>> result = new HashMap<String, Class<?>>();
        for (Class<? extends Object> type : simpleTypes)
        	result.put(type.getName(), type);
        return result;
    }

    /** Prevents instantiation of a BeanUtil object. */
    private BeanUtil() {
    }

    // type info methods -----------------------------------------------------------------------------------------------

    /**
     * Tells if the provided class name is the name of a simple Java type
     * @param className the name to check
     * @return true if it is a simple type, else false
     */
    public static boolean isSimpleType(String className) {
        return simpleTypeMap.containsKey(className);
    }

    public static boolean isPrimitive(String className) {
        return primitiveTypeMap.containsKey(className);
    }

    public static boolean isPrimitiveNumber(String className) {
        return primitiveNumberTypeMap.containsKey(className);
    }

    public static boolean isIntegralNumber(String className) {
        return integralNumberTypeMap.containsKey(className);
    }

    public static boolean isDecimalNumber(String className) {
        return decimalNumberTypeMap.containsKey(className);
    }

    public static Class<? extends Object> getWrapper(String primitiveClassName) {
        return primitiveTypeMap.get(primitiveClassName);
    }

    /**
     * Tells if the specified class is a collection type.
     * @param type the class to check
     * @return true if the class is a collection type, false otherwise
     */
    public static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    // field operations ------------------------------------------------------------------------------------------------

    /**
     * Returns an object's attribute value
     * @param obj the object to query
     * @param attributeName the name of the attribute
     * @return the attribute value
     */
    public static Object getAttributeValue(Object obj, String attributeName) {
        if (obj == null)
            throw new IllegalArgumentException("Object may not be null");
        Field field = getField(obj.getClass(), attributeName);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, field);
        }
    }

    /**
     * Sets an attribute value of an object.
     * @param obj the object to modify
     * @param fieldName the name of the attribute to set
     * @param value the value to assign to the field
     */
    public static void setAttributeValue(Object obj, String fieldName, Object value) {
        Field field = getField(obj.getClass(), fieldName);
        setAttributeValue(obj, field, value);
    }

    /**
     * Returns a class' static attribute value
     * @param objectType the class to query
     * @param attributeName the name of the attribute
     * @return the attribute value
     */
    public static Object getStaticAttributeValue(Class<? extends Object> objectType, String attributeName) {
        Field field = getField(objectType, attributeName);
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, field);
        }
    }

    /**
     * Sets a static attribute value of a class.
     * @param objectType the class to modify
     * @param fieldName the name of the attribute to set
     * @param value the value to assign to the field
     */
    public static void setStaticAttributeValue(Class<? extends Object> objectType, String fieldName, Object value) {
        Field field = getField(objectType, fieldName);
        setAttributeValue(null, field, value);
    }

    /**
     * Sets an attribute value of an object.
     * @param obj the object to modify
     * @param field the attribute to set
     * @param value the value to assign to the field
     */
    private static void setAttributeValue(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, field);
        }
    }

    /**
     * Returns the generic type information of an attribute.
     * @param field the field representation of the attribute.
     * @return an array of types that are used to parameterize the attribute.
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Object>[] getGenericTypes(Field field) {
        Type genericFieldType = field.getGenericType();
        if (!(genericFieldType instanceof ParameterizedType))
            return null; // type is not generic
        ParameterizedType pType = (ParameterizedType) genericFieldType;
        Type[] args = pType.getActualTypeArguments();
        Class<? extends Object>[] types = new Class[args.length];
        System.arraycopy(args, 0, types, 0, args.length);
        return types;
    }

    // instantiation ---------------------------------------------------------------------------------------------------

    /**
     * Instatiates the specified class.
     * @param name the name of the class to instantiate
     * @return the Class instance
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(String name) {
        Class type = simpleTypeMap.get(name);
        if (type != null)
            return type;
        else {
            try {
                 return (Class<T>) getContextClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
                throw ExceptionMapper.configurationException(e, name);
            } catch (NullPointerException e) {
            	// this is raised by the Eclipse BundleLoader if it does not find the class
                throw ExceptionMapper.configurationException(e, name);
            }
        }
    }

	public static ClassLoader getContextClassLoader() {
		ClassLoader result = Thread.currentThread().getContextClassLoader();
		if (result == null)
			result = BeanUtil.class.getClassLoader();
		return result;
	}

	public static ClassLoader createJarClassLoader(File jarFile) throws MalformedURLException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (jarFile != null)
			classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() }, classLoader);
		return classLoader;
	}
	
	public static void executeInJarClassLoader(File jarFile, Runnable action) throws MalformedURLException {
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(createJarClassLoader(jarFile));
			action.run();
        } finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}
    
	public static <T> T executeInJarClassLoader(File jarFile, Callable<T> action) throws Exception {
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(createJarClassLoader(jarFile));
			return action.call();
        } finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}
    
    /**
     * Instantiates a class by the default constructor.
     * @param className the name of the class to instantiate
     * @return an instance of the class
     */
    public static Object newInstance(String className) {
        Class<? extends Object> type = BeanUtil.forName(className);
        return newInstanceFromDefaultConstructor(type);
    }

    /**
     * Creates an object of the specified type.
     * @param type the class to instantiate
     * @param parameters the constructor parameters
     * @return an object of the specified class
     */
    public static <T> T newInstance(Class<T> type, Object ... parameters) {
    	return newInstance(type, true, parameters);
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> type, boolean strict, Object ... parameters) {
        if (parameters.length == 0)
            return newInstanceFromDefaultConstructor(type);
        Constructor<T> constructorToUse = null;
        try {
            Constructor<T>[] constructors = (Constructor<T>[]) type.getConstructors();
    		List<Constructor<T>> candidates = new ArrayList<Constructor<T>>(constructors.length);
    		int paramCount = parameters.length;
    		for (Constructor<T> constructor : constructors)
    			if (constructor.getParameterTypes().length == paramCount)
    				candidates.add(constructor);
    		if (candidates.size() == 1)
    			constructorToUse = candidates.get(0);
    		else if (candidates.size() == 0)
    			throw new ConfigurationError("No constructor with " + paramCount + " parameters found for " + type);
            else {
            	// there are several candidates - find the first one with matching types
                Class<? extends Object>[] paramTypes = new Class[parameters.length];
                for (int i = 0; i < parameters.length; i++)
                    paramTypes[i] = parameters[i].getClass();
                for (Constructor c : type.getConstructors()) {
                    if (typesMatch(c.getParameterTypes(), paramTypes)) {
                        constructorToUse = c;
                        break;
                    }
                }
             // there is no ideal match
                if (constructorToUse == null) { 
                	if (strict)
                		throw new NoSuchMethodException("No appropriate constructor found: " + type + '(' + ArrayFormat.format(", ", paramTypes) + ')');
                	for (Constructor<T> candidate : candidates) {
                		try {
                			return newInstance(constructorToUse, strict, parameters);
                		} catch (Exception e) {
                			if (logger.isDebugEnabled())
                				logger.debug("Exception in constructor call: " + candidate, e);
                			continue; // ignore exception and try next constructor
                		}
                	}
                	// no constructor could be called without exception
                	throw new ConfigurationError("None of these constructors could be called without exception: " + candidates);
                }
            }
    		if (!strict)
    			parameters = convertArray(parameters, constructorToUse.getParameterTypes());
            return newInstance(constructorToUse, parameters);
        } catch (SecurityException e) {
            throw ExceptionMapper.configurationException(e, constructorToUse);
        } catch (NoSuchMethodException e) {
            throw ExceptionMapper.configurationException(e, type);
        }
    }

    /**
     * Creates a new instance of a Class.
     * @param constructor
     * @param params
     * @return a new instance of the class
     */
    public static <T> T newInstance(Constructor<T> constructor, Object ... params) {
        return newInstance(constructor, true, params);
    }

    public static <T> T newInstance(Constructor<T> constructor, boolean strict, Object ... parameters) {
		if (!strict)
			parameters = convertArray(parameters, constructor.getParameterTypes());
        Class<T> type = constructor.getDeclaringClass();
        if (deprecated(type))
            escalator.escalate("Instantiating a deprecated class: " + type.getName(), BeanUtil.class, null);
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException e) {
            throw ExceptionMapper.configurationException(e, type);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, type);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, type);
        }
    }

    // method operations -----------------------------------------------------------------------------------------------

    /**
     * Finds a method by reflection. This iterates all methods of the class, comparing names and parameter types.
     * Unlike the method Class.getMethod(String, Class ...), this method is able to match primitive and wrapper types.
     * If no appropriate method is found, a ConfigurationError is raised.
     * @param type
     * @param methodName
     * @param paramTypes
     * @return a method with matching names and parameters
     */
    public static Method getMethod(Class<? extends Object> type, String methodName, Class<? extends Object> ... paramTypes) {
        Method method = findMethod(type, methodName, paramTypes);
        if (method == null)
            throw new ConfigurationError("method not found: " + methodName 
                    + '(' + ArrayFormat.format(paramTypes) + ')');
        return method;
    }

    /**
     * Finds a method by reflection. This iterates all methods of the class, comparing names and parameter types.
     * Unlike the method Class.getMethod(String, Class ...), this method is able to match primitive and wrapper types.
     * If no appropriate method is found, 'null' is returned
     * @param type
     * @param methodName
     * @param paramTypes
     * @return a method with matching names and parameters
     */
    public static Method findMethod(Class<? extends Object> type, String methodName, Class<? extends Object> ... paramTypes) {
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName()) && typesMatch(paramTypes, method.getParameterTypes()))
                return method;
        }
        return null;
    }

    public static Method[] findMethodsByName(Class<? extends Object> type, String methodName) {
        ArrayBuilder<Method> builder = new ArrayBuilder<Method>(Method.class);
        for (Method method : type.getMethods()) {
            if (methodName.equals(method.getName()))
                builder.append(method);
        }
        return builder.toArray();
    }

    /**
     * Invokes a method on a bean.
     * @param target
     * @param methodName
     * @param args
     * @return the invoked method's return value.
     */
    @SuppressWarnings("unchecked")
    public static Object invoke(Object target, String methodName, Object ... args) {

    	if (target == null)
            throw new IllegalArgumentException("target is null");
        Class[] argTypes = null;
        if (args != null) {
        	argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++)
                argTypes[i] = (args[i] != null ? args[i].getClass() : null);
        }
        Method method = getMethod(target.getClass(), methodName, argTypes);
        return invoke(target, method, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeStatic(Class<? extends Object> targetClass, String methodName, Object ... args) {
        if (targetClass == null)
            throw new IllegalArgumentException("target is null");
        Class<? extends Object>[] argClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            argClasses[i] = (args[i] != null ? args[i].getClass() : null);
        Method method = getMethod(targetClass, methodName, argClasses);
        return (T) invoke(null, method, args);
    }

    /**
     * Invokes a method on a bean
     * @param target
     * @param method
     * @param args
     * @return the invoked method's return value.
     */
    public static Object invoke(Object target, Method method, Object ... args) {
        return invoke(target, method, true, args);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object target, Method method, boolean strict, Object ... args) {
        try {
            Object[] params = (strict ? args : ArrayTypeConverter.convert(args, method.getParameterTypes()));
            return (T) method.invoke(target, params);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, method);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, method);
        }
    }

    public static boolean typesMatch(Class<? extends Object>[] foundTypes, Class<? extends Object>[] expectedTypes) {
    	if (foundTypes == null)
    		return (expectedTypes == null || expectedTypes.length == 0);
        if (foundTypes.length != expectedTypes.length)
            return false;
        if (expectedTypes.length == 0)
            return true;
        for (int i = 0; i < foundTypes.length; i++) {
            Class<? extends Object> expectedType = expectedTypes[i];
            Class<? extends Object> foundType = foundTypes[i];
            if (expectedType.isAssignableFrom(foundType))
                return true;
            if (isPrimitive(expectedType.getName()) &&
                    foundType.equals(getWrapper(expectedType.getName())))
                return true;
            if (isPrimitive(foundType.getName()) &&
                    expectedType.equals(getWrapper(foundType.getName())))
                return true;
        }
        return false;
    }


    // JavaBean operations ---------------------------------------------------------------------------------------------

    /**
     * Returns the bean property descriptor of an attribute
     * @param beanClass the class that holds the attribute
     * @param propertyName the name of the property
     * @return the attribute's property descriptor
     */
    public static PropertyDescriptor getPropertyDescriptor(Class<? extends Object> beanClass, String propertyName) {
        if (beanClass == null)
            throw new IllegalArgumentException("beanClass is null");
        String propertyId = beanClass.getName() + '#' + propertyName;
        PropertyDescriptor result = propertyDescriptors.get(propertyId);
        if (result != null)
            return result;
        // descriptor is new

        int separatorIndex = propertyName.indexOf('.');
        if (separatorIndex >= 0) {
            String localProperty = propertyName.substring(0, separatorIndex);
            String remoteProperty = propertyName.substring(separatorIndex + 1);
            Class<? extends Object> localPropertyType = getPropertyDescriptor(beanClass, localProperty).getPropertyType();
            result = getPropertyDescriptor(localPropertyType, remoteProperty);
        } else {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor descriptor : descriptors) {
                    String name = descriptor.getName();
                    if (name.equals(propertyName)) {
                        result = descriptor;
                        break;
                    }
                }
            } catch (IntrospectionException e) {
                throw ExceptionMapper.configurationException(e, propertyName);
            }
        }
        propertyDescriptors.put(propertyId, result);
        return result;
    }

    public static boolean hasProperty(Class<? extends Object> beanClass, String propertyName) {
        return (getPropertyDescriptor(beanClass, propertyName) != null);
    }

    /**
     * returns the name of a property read method.
     * @param propertyName the name of the property
     * @param propertyType the type of the property
     * @return the name of the property read method
     */
    public static String readMethodName(String propertyName, Class<? extends Object> propertyType) {
        if (boolean.class.equals(propertyType) || Boolean.class.equals(propertyType))
            return "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        else
            return "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    }

    /**
     * returns the name of a property write method.
     * @param propertyName the name of the property
     * @return the name of the property write method
     */
    public static String writeMethodName(String propertyName) {
        return "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    }

    /**
     * Copies a Map's values to the properties of a JavaBean,
     * using the Map entries' key values as bean property names.
     */
/*
    public static void mapProperties(Map properties, Object bean) {
        for (Map.Entry entry : properties.entrySet()) {
            String propertyName = (String) entry.getKey();
            Object propertyValue = entry.getValue();
            setProperty(bean, propertyName, propertyValue);
        }
    }
*/
    /*
    public static void mapProperties(Map<String, Object> properties, Object bean) {
        Class<?> beanClass = bean.getClass();
        Method writeMethod = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                Object value = properties.get(name);
                if (value != null) {
                    writeMethod = propertyDescriptor.getWriteMethod();
                    Object targetTypeObject = ConverterUtil.convert(value, propertyDescriptor.getPropertyType());
                    writeMethod.invoke(bean, targetTypeObject);
                }
            }
        } catch (IntrospectionException e) {
            throw ExceptionMapper.configurationException(e, beanClass);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        }
    }
    */

    /**
     * Finds all property descriptors of a bean class
     * @param type the class to check
     * @return all found property descriptors
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class<? extends Object> type) {
        try {
            return Introspector.getBeanInfo(type).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
/*
    public static Class getPropertyType(Class beanClass, String propertyName) {
        PropertyDescriptor descriptor = getPropertyDescriptor(beanClass, propertyName);
        return (descriptor != null ? descriptor.getPropertyType() : null);
    }
*/

    /**
     * Queries a property value on a JavaBean instance
     * @param bean
     * @param propertyName
     * @return the property value
     */
    public static Object getPropertyValue(Object bean, String propertyName) {
        return getPropertyValue(bean, propertyName, true);
    }

    public static Object getPropertyValue(Object bean, String propertyName, boolean strict) {
        Method readMethod = null;
        try {
            PropertyDescriptor descriptor = getPropertyDescriptor(bean.getClass(), propertyName);
            if (descriptor == null) {
            	if (strict)
            		throw new ConfigurationError("Property '" + propertyName + "' not found in class " + bean.getClass());
            	else 
            		return null;
            }
			readMethod = descriptor.getReadMethod();
            return readMethod.invoke(bean);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, readMethod);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, readMethod);
        }
    }

    /**
     * sets a property value on a JavaBean instance.
     * @param bean
     * @param propertyName
     * @param propertyValue
     */
    public static void setPropertyValue(Object bean, String propertyName, Object propertyValue) {
        setPropertyValue(bean, propertyName, propertyValue, true);
    }

    public static void setPropertyValue(Object bean, String propertyName, Object propertyValue, boolean strict) {
    	setPropertyValue(bean, propertyName, propertyValue, strict, !strict);
    }

    public static void setPropertyValue(Object bean, String propertyName, Object argument, boolean required, boolean autoConvert) {
        Method writeMethod = null;
        try {
            Class<? extends Object> beanClass = bean.getClass();
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);
            if (propertyDescriptor == null)
                if (required)
                    throw new ConfigurationError(beanClass + " does not have a property '" + propertyName + "'");
                else
                    return;
            writeMethod = propertyDescriptor.getWriteMethod();
            Class<?> propertyType = propertyDescriptor.getPropertyType();
			if (argument != null) {
	            Class<? extends Object> argType = argument.getClass();
	            if (!propertyType.isAssignableFrom(argType) && !isWrapperTypeOf(propertyType, argument) && !autoConvert)
                    throw new IllegalArgumentException("ArgumentType mismatch: expected " 
                            + propertyType.getName() + ", found " + argument.getClass().getName());
                else
                    argument = AnyConverter.convert(argument, propertyType);
			}
            writeMethod.invoke(bean, argument);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        }
    }

	private static boolean isWrapperTypeOf(Class<?> propertyType,
			Object propertyValue) {
		String propertyTypeName = propertyType.getName();
		return (isPrimitive(propertyTypeName) && getWrapper(propertyType.getName()) == propertyValue.getClass());
	}
    
    @SuppressWarnings("unchecked")
    public static <BEAN, PROP_TYPE> List<PROP_TYPE> extractProperties(Collection<BEAN> beans, String propertyName) {
        List<PROP_TYPE> result = new ArrayList<PROP_TYPE>(beans.size());
        for (BEAN bean : beans)
            result.add((PROP_TYPE) getPropertyValue(bean, propertyName));
        return result;
    }

    // class operations ------------------------------------------------------------------------------------------------

    /**
     * Prints information about a class' parents and methods to a PrintWriter
     * @param object
     * @param printer
     */
    public static void printClassInfo(Object object, PrintWriter printer) {
        if (object == null) {
            printer.println("null");
            return;
        }
        Class<? extends Object> type = object.getClass();
        printer.println(type);
        if (type.getSuperclass() != null)
            printer.println("extends " + type.getSuperclass());
        for (Class<?> interf : type.getInterfaces())
            printer.println("implements " + interf);
        for (Method method : type.getMethods()) {
            printer.println(method);
        }
    }

    /**
     * Checks if a class fulfills the JavaBeans contract.
     * @param cls the class to check
     */
    public static void checkJavaBean(Class<? extends Object> cls) {
        try {
            Constructor<? extends Object> constructor = cls.getDeclaredConstructor();
            int classModifiers = cls.getModifiers();
            if (Modifier.isInterface(classModifiers))
                throw new RuntimeException(cls.getName() + " is an interface");
            if (Modifier.isAbstract(classModifiers))
                throw new RuntimeException(cls.getName() + " cannot be instantiated - it is an abstract class");
            int modifiers = constructor.getModifiers();
            if (!Modifier.isPublic(modifiers))
                throw new RuntimeException("No public default constructor in " + cls);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor in class " + cls);
        } catch (SecurityException e) {
            logger.error("I am not allowed to check the class by using reflection, " +
                    "so I just can hope the class is alright and go on: ", e);
        }
    }

    /**
     * Tells if a class is deprecated.
     * @param type the class to check for deprecation
     * @return true if the class is deprecated, else false
     * @since 0.2.05
     */
    public static boolean deprecated(Class<? extends Object> type) {
        Annotation[] annotations = type.getDeclaredAnnotations();
        for (Annotation annotation : annotations)
            if (annotation instanceof Deprecated)
                return true;
        return false;
    }
    
    // invisible helpers -----------------------------------------------------------------------------------------------

    /**
     * Creates an instance of the class using the default constructor.
     * @since 0.2.06
     */
    @SuppressWarnings("cast")
    private static <T> T newInstanceFromDefaultConstructor(Class<T> type) {
        if (type == null)
            return null;
        if (logger.isDebugEnabled())
            logger.debug("Instantiating " + type.getSimpleName());
        if (deprecated(type))
            escalator.escalate("Instantiating a deprecated class: " + type.getName(), BeanUtil.class, null);
        try {
            return (T) type.newInstance();
        } catch (InstantiationException e) {
            throw ExceptionMapper.configurationException(e, type);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, type);
        }
    }

    /**
     * Returns a Field object that represents an attribute of a class
     * @param type the class that holds the attribute
     * @param name the name of the attribute
     * @return a Field object that represents the attribute
     */
    private static Field getField(Class<? extends Object> type, String name) {
        try {
            return type.getField(name);
        } catch (NoSuchFieldException e) {
            throw ExceptionMapper.configurationException(e, type.getName() + '.' + name);
        }
    }

    /**
     * Represents a primitive-to-wrapper mapping.
     */
    private static final class PrimitiveTypeMapping {
        public Class<? extends Object> primitiveType;
        public Class<? extends Object> wrapperType;

        public PrimitiveTypeMapping(Class<? extends Object> primitiveType, Class<? extends Object> wrapperType) {
            this.primitiveType = primitiveType;
            this.wrapperType = wrapperType;
        }
    }

    public static Method[] findMethodsByAnnotation(
            Class<? extends Object> owner, Class<? extends Annotation> annotationClass) {
        Method[] methods = owner.getMethods();
        ArrayBuilder<Method> builder = new ArrayBuilder<Method>(Method.class);
        for (Method method : methods)
            if (method.getAnnotation(annotationClass) != null)
                builder.append(method);
        return builder.toArray();
    }
    
    public static <C, I> Type[] getGenericInterfaceParams(Class<C> checkedClass, Class<I> searchedInterface) {
        for (Type type : checkedClass.getGenericInterfaces()) {
            ParameterizedType pt = (ParameterizedType) type;
            if (searchedInterface.equals(pt.getRawType())) {
                ParameterizedType pType = ((ParameterizedType) type);
                return pType.getActualTypeArguments();
            }
        }
        if (!Object.class.equals(checkedClass.getSuperclass()))
            return getGenericInterfaceParams(checkedClass.getSuperclass(), searchedInterface);
        throw new ConfigurationError(checkedClass + " does not implement interface with generic parameters: " + searchedInterface);
    }

	public static String toString(Object bean) {
		if (bean == null)
			return null;
		StringBuilder builder = new StringBuilder(bean.getClass().getName());
		PropertyDescriptor[] descriptors = getPropertyDescriptors(bean.getClass());
		boolean first = true;
		for (PropertyDescriptor descriptor : descriptors) {
			String propertyName = descriptor.getName();
			if (!"class".equals(propertyName)) {
				if (first)
					builder.append('[');
				else
					builder.append(", ");
				
				Object value = getPropertyValue(bean, propertyName);
				String valueString = ToStringConverter.convert(value, "null");
				builder.append(propertyName).append("=").append(valueString);
				first = false;
			}
		}
		if (!first)
			builder.append(']');
		return builder.toString();
	}

	public static <T> String simpleName(Class<T> type) {
		return (type != null ? type.getSimpleName() : null);
	}
	
	// private helpers -------------------------------------------------------------------------------------------------

	private static Object[] convertArray(Object[] values, Class<?>[] targetTypes) {
		Object[] result = new Object[values.length];
		for (int i = 0; i < values.length; i++)
			result[i] = AnyConverter.convert(values[i], targetTypes[i]);
		return result;
	}

}
