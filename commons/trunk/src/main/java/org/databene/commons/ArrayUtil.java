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

package org.databene.commons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides array-related operations.
 * Created: 09.06.2006 21:31:49
 * @author Volker Bergmann
 */
public final class ArrayUtil {

    public static <T> T[] copyOfRange(T[] array, int offset, int length) {
        Class<T[]> resultType = (Class<T[]>) array.getClass();
        Class<T> componentType = (Class<T>) resultType.getComponentType();
        T[] result = (T[]) Array.newInstance(componentType, length);
        System.arraycopy(array, offset, result, 0, length);
        return result;
    }

    public static <T> T[] remove(T[] array, int indexToRemove) {
        Class<T[]> resultType = (Class<T[]>) array.getClass();
        Class<T> componentType = (Class<T>) resultType.getComponentType();
        T[] result = (T[]) Array.newInstance(componentType, array.length - 1);
        if (indexToRemove > 0)
            System.arraycopy(array, 0, result, 0, indexToRemove);
        System.arraycopy(array, indexToRemove + 1, result, indexToRemove, array.length - indexToRemove - 1);
        return result;
    }

    // containment check -----------------------------------------------------------------------------------------------

    /**
     * Tells if an array contains a specific element
     * @param element the element to search
     * @param array the array to scan
     * @return true if the element was found, else false
     */
    public static <T> boolean contains(T[] array, T element) {
        for (T o : array)
            if (NullSafeComparator.equals(o, element))
                return true;
        return false;
    }

    /**
     * Tells if an array ends with a specified sub array
     * @param candidates the array to scan
     * @param searched the sub array that is searched
     * @return true if the array ands with or equals the searched sub array
     */
    public static <T> boolean endsWithSequence(T[] candidates, T[] searched) {
        if (searched.length > candidates.length)
            return false;
        for (int i = 0; i < searched.length; i++) {
            if (!candidates[candidates.length - searched.length + i].equals(searched[i]))
                return false;
        }
        return true;
    }

    /**
     * Finds the elements that are contained in both array.
     * @param a1 the first array to compare
     * @param a2 the first array to compare
     * @return the elements that are contained in both array.
     */
/*
    public static <T> List<T> commonElements(T[] a1, T[] a2) {
        List<T> commonElements = new ArrayList<T>();
        for (T element : a1)
            if (contains(element, a2))
                commonElements.add(element);
        return commonElements;
    }
*/    

    public static <T> T[] commonElements(T[]... sources) {
        Class<T> componentType = null;
        for (int arrayNumber = 0; arrayNumber < sources.length && componentType == null; arrayNumber++) {
            T[] source = sources[arrayNumber];
            for (int index = 0; index < source.length && componentType == null; index++)
                if (source[index] != null)
                    componentType = (Class<T>) source[index].getClass();
        }
        return commonElements(componentType, sources);
    }

    public static <T> T[] commonElements(Class<T> componentType, T[]... sources) {
        ArrayBuilder<T> builder = new ArrayBuilder<T>(componentType);
        T[] firstArray = sources[0];
        for (T element : firstArray) {
            boolean common = true;
            for (int i = 1; i < sources.length; i++)
                if (!ArrayUtil.contains(sources[i], element)) {
                    common = false;
                    break;
                }
            if (common)
                builder.append(element);
        }
        return builder.toArray();
    }


    // identity checks -------------------------------------------------------------------------------------------------

    /**
     * Tells if two arrays have the same content, independent of the ordering
     * @param a1 the first array to compare
     * @param a2 the first array to compare
     * @return true if the array have the same content, independant of the ordering
     */
    public static <T> boolean equalsIgnoreOrder(T[] a1, T[] a2) {
        if (a1 == a2)
            return true;
        if (a1 == null)
            return false;
        if (a1.length != a2.length)
            return false;
        List<T> l1 = new ArrayList<T>(a1.length);
        for (T item : a1)
            l1.add(item);
        for (int i = a1.length - 1; i >= 0; i--)
            if (contains(a2, a1[i]))
                l1.remove(i);
            else
                return false;
        return l1.size() == 0;
    }

    /**
     * Tells the first index under which an item is found in an array.
     * @param searchedItem
     * @param array
     * @return the index of the searched item
     */
    public static <T> int indexOf(T searchedItem, T[] array) {
        for (int i = 0; i < array.length; i++) {
            T candidate = array[i];
            if (NullSafeComparator.equals(candidate, searchedItem))
                return i;
        }
        return -1;
    }

    public static <T> T[] toArray(T ... values) {
    	Class<T> componentType = (Class<T>) (values.length > 0 ? values[0].getClass() : Object.class);
    	return buildArrayOfType(componentType, values);
    }
/*
    @Deprecated
    public static <T> T[] toArray(Class<T> componentType, T ... values) {
        return buildArrayOfType(componentType, values);
    }
*/
    public static <T> T[] buildArrayOfType(Class<T> componentType, T ... values) {
        T[] array = (T[]) Array.newInstance(componentType, values.length);
        System.arraycopy(values, 0, array, 0, values.length);
        return array;
    }

    public static <T> T[] revert(T[] array) {
        for (int i = (array.length >> 1) - 1 ; i >= 0; i--) {
            T tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
        return array;
    }

    public static char[] revert(char[] array) {
        for (int i = (array.length >> 1) - 1; i >= 0; i--) {
            char tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
        return array;
    }
    
    public static <T> Class<T[]> arrayType(Class<T> componentType) {
        T[] array = (T[]) Array.newInstance(componentType, 0);
        return (Class<T[]>) array.getClass();
    }

    public static <T> T[] newInstance(Class<T> componentType, int length) {
        return (T[])Array.newInstance(componentType, length);
    }

    public static <T> T[] append(T[] array, T value) {
        if (array == null) {
            return toArray(value);
        } else {
            Class<T> componentType = (Class<T>) array.getClass().getComponentType();
            T[] newArray = newInstance(componentType, array.length + 1);
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = value;
            return newArray;
        }
    }

    public static boolean isEmpty(Object[] values) {
        return (values == null || values.length == 0);
    }

    public static boolean equals(Object[] a1, Object[] a2) {
        if (a1 == a2)
            return true;
        if (a1 == null)
            return a2 == null;
        if (a2 == null)
            return false;
        if (a1.length != a2.length)
            return false;
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] == a2[i])
                continue;
            if (a1[i] == null)
                return false;
            if (a1[i].getClass().isArray()) {
                if (!a2[i].getClass().isArray())
                    return false;
                if (!equals((Object[]) a1[i], (Object[]) a2[i]))
                    return false;
            } else
                if (!a1[i].equals(a2[i]))
                    return false;
                
        }
        return true;
    }

}
