package org.databene.commons.iterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.*;

public abstract class IteratorTestCase {

    public static <T> void checkUniqueIteration(Iterator<T> iterator, int count) {
        Set<T> items = new HashSet<T>(count);
        for (int i = 0; i < count; i++) {
            assertTrue("Iterator is expected to be available", iterator.hasNext());
            T item = iterator.next();
            assertFalse("Item not unique: " + item, items.contains(item));
            items.add(item);
        }
    }

	public static <T> NextHelper expectNextElements(Iterator<?> iterator, T... elements) {
		for (T element : elements) {
			assertTrue("Iterator is expected to be available", iterator.hasNext());
			Object next = iterator.next();
			assertEquals("Expected " + element + " but was: " + next, element, next);
		}
		return new NextHelper(iterator);
	}
	
	public static class NextHelper {
		
		Iterator<?> iterator;

		public NextHelper(Iterator<?> iterator) {
			this.iterator = iterator;
		}
		
		public void withNext() {
			assertTrue("Iterator is expected to be still available", iterator.hasNext());
		}
		
		public void withNoNext() {
			assertFalse("Iterator is not expected to be available any more", iterator.hasNext());
		}
	}
	
}
