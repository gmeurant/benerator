package org.databene.commons.iterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.*;

public abstract class IteratorTestCase {

    public static <T> void checkUniqueIteration(Iterator<T> iterator, int count) {
        Set<T> items = new HashSet<T>(count);
        for (int i = 0; i < count; i++) {
            assertTrue(iterator.hasNext());
            T item = iterator.next();
            assertFalse(items.contains(item));
            items.add(item);
        }
    }

	public static <T> NextHelper<T> expectNextElements(Iterator<T> iterator, T... elements) {
		for (T element : elements) {
			assertTrue(iterator.hasNext());
			assertEquals(element, iterator.next());
		}
		return new NextHelper<T>(iterator);
	}
	
	public static class NextHelper<T> {
		
		Iterator<T> iterator;

		public NextHelper(Iterator<T> iterator) {
			this.iterator = iterator;
		}
		
		public void withNext() {
			assertTrue(iterator.hasNext());
		}
		
		public void withNoNext() {
			assertFalse(iterator.hasNext());
		}
	}
	
}
