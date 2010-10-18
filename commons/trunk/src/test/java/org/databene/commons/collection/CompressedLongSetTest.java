/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.commons.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.databene.commons.iterator.IteratorTestCase;
import org.junit.Test;

/**
 * Tests the {@link CompressedLongSet}.<br/><br/>
 * Created: 18.10.2010 08:35:59
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class CompressedLongSetTest extends IteratorTestCase {

	@Test
	public void test11() {
		CompressedLongSet set = new CompressedLongSet();
		assertTrue(set.isEmpty());
		set.add(1);
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		set.add(1);
		assertEquals(new LongRange(1L, 1L), set.numbers.get(1L));
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertEquals(1, set.size());
		expectNextElements(set.iterator(), 1L).withNoNext();
	}
	
	@Test
	public void test12() {
		CompressedLongSet set = new CompressedLongSet();
		assertTrue(set.isEmpty());
		
		set.add(1);
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertFalse(set.contains(2L));
		assertEquals(1, set.size());
		
		set.add(2);
		assertEquals(new LongRange(1L, 2L), set.numbers.get(1L));
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(2L));
		
		assertEquals(2, set.size());
		expectNextElements(set.iterator(), 1L, 2L).withNoNext();
	}

	@Test
	public void test21() {
		CompressedLongSet set = new CompressedLongSet();

		set.add(2L);
		assertFalse(set.isEmpty());
		assertFalse(set.contains(1L));
		assertTrue(set.contains(2L));
		assertEquals(1, set.size());
		
		set.add(1L);
		assertEquals(new LongRange(1L, 2L), set.numbers.get(1L));
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(2L));
		
		assertEquals(2, set.size());
		expectNextElements(set.iterator(), 1L, 2L).withNoNext();
	}

	@Test
	public void test13() {
		CompressedLongSet set = new CompressedLongSet();

		set.add(1);
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertFalse(set.contains(3L));
		
		set.add(3);
		assertEquals(new LongRange(1, 1), set.numbers.get(1L));
		assertEquals(new LongRange(3, 3), set.numbers.get(3L));
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(3L));
		
		assertEquals(2, set.size());
		expectNextElements(set.iterator(), 1L, 3L).withNoNext();
	}

	@Test
	public void test31() {
		CompressedLongSet set = new CompressedLongSet();

		set.add(3L);
		assertFalse(set.isEmpty());
		assertFalse(set.contains(1L));
		assertTrue(set.contains(3L));
		
		set.add(1);
		assertEquals(new LongRange(1, 1), set.numbers.get(1L));
		assertEquals(new LongRange(3, 3), set.numbers.get(3L));
		assertFalse(set.isEmpty());
		assertTrue(set.contains(1L));
		assertTrue(set.contains(3L));
		
		assertEquals(2, set.size());
		expectNextElements(set.iterator(), 1L, 3L).withNoNext();
	}

	@Test
	public void test132() {
		CompressedLongSet set = new CompressedLongSet();
		set.add(1L);
		set.add(3L);
		set.add(2L);
		assertEquals(new LongRange(1L, 3L), set.numbers.get(1L));
		assertEquals(3, set.size());
		expectNextElements(set.iterator(), 1L, 2L, 3L).withNoNext();
	}
	
	@Test
	public void test1to10() {
		CompressedLongSet set = new CompressedLongSet();
		set.add(1L);
		set.add(3L);
		set.add(6L);
		set.add(9L);
		set.add(2L);
		set.add(7L);
		set.add(10L);
		set.add(4L);
		set.add(8L);
		set.add(5L);
		assertEquals(new LongRange(1L, 10L), set.numbers.get(1L));
		assertEquals(10, set.size());
		expectNextElements(set.iterator(), 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L).withNoNext();
	}
	
}
