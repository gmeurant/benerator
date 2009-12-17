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

	public static <T> NextHelper<T> expectNextElements(Iterator<T> iterator, T... elements) {
		for (T element : elements) {
			assertTrue("Iterator is expected to be available", iterator.hasNext());
			T next = iterator.next();
			assertEquals("Expected " + element + " but was: " + next, element, next);
		}
		return new NextHelper<T>(iterator);
	}
	
	public static class NextHelper<T> {
		
		Iterator<T> iterator;

		public NextHelper(Iterator<T> iterator) {
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
