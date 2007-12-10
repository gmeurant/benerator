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
    private int itemCount;

    public ArrayBuilder(Class<E> componentType) {
        this(componentType, DEFAULT_INITIAL_CAPACITY);
    }
    
    public ArrayBuilder(Class<E> componentType, int initialCapacity) {
        this.componentType = componentType;
        this.buffer = createBuffer(initialCapacity);
    }

    public ArrayBuilder<E> append(E item) {
        if (buffer == null)
            throw new UnsupportedOperationException("ArrayBuilder cannot be reused after invoking toArray()");
        if (itemCount >= buffer.length - 1) {
            E[] newBuffer = createBuffer(buffer.length * 2);
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        }
        buffer[itemCount++] = item;
        return this;
    }
    
    public E[] toArray() {
        E[] result = ArrayUtil.newInstance(componentType, itemCount);
        System.arraycopy(buffer, 0, result, 0, itemCount);
        itemCount = 0;
        buffer = null;
        return result;
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    private E[] createBuffer(int initialCapacity) {
        return (E[])Array.newInstance(componentType, initialCapacity);
    }
}
