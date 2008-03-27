package org.databene.commons;

import java.lang.reflect.Array;

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

    public ArrayBuilder<E> append(E element) {
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
    
    public E[] toArray() {
        E[] result = ArrayUtil.newInstance(componentType, elementCount);
        System.arraycopy(buffer, 0, result, 0, elementCount);
        elementCount = 0;
        buffer = null;
        return result;
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    private E[] createBuffer(int initialCapacity) {
        return (E[])Array.newInstance(componentType, initialCapacity);
    }
}
