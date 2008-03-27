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
import java.lang.reflect.Modifier;

/**
 * Provides Collection-related utility methods.<br/>
 * <br/>
 * Created: 18.12.2006 06:46:24
 */
public final class CollectionUtil {

    public static boolean isEmpty(Collection<? extends Object> collection) {
        return (collection == null || collection.size() == 0);
    }

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

    /**
     * Adds the content of an array to a collection
     * @param target the collection to be extended
     * @param values the values to add
     */
    public static <T, C extends Collection<T>> C add(C target, T ... values) {
        for (T item : values)
            target.add(item);
        return target;
    }

    public static <T> List<T> copy(List<T> src, int offset, int length) {
        List<T> items = new ArrayList<T>(length);
        for (int i = 0; i < length; i++)
            items.add(src.get(offset + i));
        return items;
    }

    public static <T> T[] toArray(Collection<? extends T> source) {
        if (source.size() == 0)
            throw new IllegalArgumentException("For empty collections, a componentType needs to be specified.");
        Class<T> componentType = (Class<T>) source.iterator().next().getClass();
        T[] array = (T[]) Array.newInstance(componentType, source.size());
        return source.toArray(array);
    }

    public static <T> T[] toArray(Collection<? extends T> source, Class<T> componentType) {
        T[] array = (T[]) Array.newInstance(componentType, source.size());
        return source.toArray(array);
    }

    public static char[] toArray(Collection<Character> source) {
        char[] result = new char[source.size()];
        int i = 0;
        for (Character c : source)
            result[i++] = c;
        return result;
    }

    public static Map buildMap(Object ... keyValuePairs) {
        Map map = new HashMap();
        if (keyValuePairs.length % 2 != 0)
            throw new IllegalArgumentException("Invalid numer of arguments. " +
                    "It must be even to represent key-value-pairs");
        for (int i = 0; i < keyValuePairs.length; i += 2)
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        return map;
    }

    public static Map buildOrderedMap(Object ... keyValuePairs) {
        Map map = new OrderedMap();
        if (keyValuePairs.length % 2 != 0)
            throw new IllegalArgumentException("Invalid numer of arguments. " +
                    "It must be even to represent key-value-pairs");
        for (int i = 0; i < keyValuePairs.length; i += 2)
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        return map;
    }

    /** Creates a new instance of a Collection. Abstract interfaces are mapped to a default implementation class. */ 
    public static <T extends Collection> T newInstance(Class<T> collectionType) {
        if ((collectionType.getModifiers() & Modifier.ABSTRACT) == 0)
            return BeanUtil.newInstance(collectionType);
        else if (collectionType == Collection.class || collectionType == List.class)
            return (T)new ArrayList();
        else if (collectionType == SortedSet.class)
            return (T)new TreeSet();
        else if (collectionType == Set.class)
            return (T)new TreeSet();
        else
            throw new UnsupportedOperationException("Not a supported collection type: " + collectionType.getName());
    }

    /** Compares two lists for identical content, accepting different order. */
    public static boolean equalsIgnoreOrder(List a1, List a2) {
        if (a1 == a2)
            return true;
        if (a1 == null)
            return false;
        if (a1.size() != a2.size())
            return false;
        List l1 = new ArrayList(a1.size());
        for (Object item : a1)
            l1.add(item);
        for (int i = a1.size() - 1; i >= 0; i--)
            if (a2.contains(a1.get(i)))
                l1.remove(i);
            else
                return false;
        return l1.size() == 0;
    }
}
