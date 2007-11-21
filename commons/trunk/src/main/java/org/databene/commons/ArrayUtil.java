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

import java.util.*;
import java.lang.reflect.Array;

/**
 * Provides array-related operations.
 * 
 * Created: 09.06.2006 21:31:49
 */
public final class ArrayUtil {

    /**
     * Converts an array into a list.
     * @param array the array to convert into a list.
     * @return a list containing all elements of the given array.
     */
    public static <T> List<T> toList(T ... array) {
        List<T> result = new ArrayList<T>(array.length);
        for (T item : array)
            result.add(item);
        return result;
    }

    /**
     * Creates a HashSet filled with the specified elements
     * @param elements the content of the Set
     * @return a HashSet with the elements
     */
    public static <T> Set<T> toSet(T ... elements) {
        HashSet<T> set = new HashSet<T>();
        for (T element : elements)
            set.add(element);
        return set;
    }

    public static <T> SortedSet<T> toSortedSet(T ... elements) {
        TreeSet<T> set = new TreeSet<T>();
        for (T element : elements)
            set.add(element);
        return set;
    }

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
    public static <T> boolean contains(T element, T[] array) {
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

    public static <T> List<T> commonElements(T[] a1, T[] a2) {
        List<T> commonElements = new ArrayList<T>();
        for (T element : a1)
            if (contains(element, a2))
                commonElements.add(element);
        return commonElements;
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
            if (contains(a1[i], a2))
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

    public static <T> T[] toArray(Class<T> componentType, T ... values) {
        T[] array = (T[]) Array.newInstance(componentType, values.length);
        System.arraycopy(values, 0, array, 0, values.length);
        return array;
    }

    public static <T> Class<T[]> arrayTypeOfComponent(Class<T> componentType) {
        return (Class<T[]>) toArray(componentType).getClass().getComponentType();
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
}
