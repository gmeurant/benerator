package org.databene.commons;

import java.lang.reflect.Array;

import org.databene.commons.converter.ToStringConverter;

/**
 * Helper class for building arrays.
 * @param <E>
 * @author Volker Bergmann
 * @since 0.2.04
 */
public class ArrayBuilder<E> {
    
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    
    private Class<E> componentType;
    private E[] buffer;
    private int elementCount;

    public ArrayBuilder(Class<E> componentType) {
        this(componentType, DEFAULT_INITIAL_CAPACITY);
    }
    
    public ArrayBuilder(Class<E> componentType, int initialCapacity) {
        this.componentType = componentType;
        this.buffer = createBuffer(initialCapacity);
    }

    /** @deprecated replaced with add(Element) */
    @Deprecated
    public ArrayBuilder<E> append(E element) {
        return add(element); // TODO deprecation warning
    }
    
    public ArrayBuilder<E> add(E element) {
        if (buffer == null)
            throw new UnsupportedOperationException("ArrayBuilder cannot be reused after invoking toArray()");
        if (elementCount >= buffer.length - 1) {
            E[] newBuffer = createBuffer(buffer.length * 2);
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
        buffer[elementCount++] = element;
        return this;
    }
    
    public void addAll(E[] elements) {
	    for (E element : elements)
	    	add(element);
    }
    
    public E[] toArray() {
        E[] result = ArrayUtil.newInstance(componentType, elementCount);
        System.arraycopy(buffer, 0, result, 0, elementCount);
        elementCount = 0;
        buffer = null;
        return result;
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	for (int i = 0; i < elementCount; i++) {
    		if (i > 0)
    			builder.append(", ");
    		builder.append(ToStringConverter.convert(buffer[i], "[NULL]"));
    	}
    	return builder.toString();
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    @SuppressWarnings("unchecked")
    private E[] createBuffer(int initialCapacity) {
        return (E[]) Array.newInstance(componentType, initialCapacity);
    }

}
