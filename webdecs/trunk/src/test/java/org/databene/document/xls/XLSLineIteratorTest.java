/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.document.xls;

import java.io.IOException;
import java.util.Arrays;

import org.databene.commons.ArrayUtil;
import org.databene.commons.TimeUtil;
import org.databene.document.xls.XLSLineIterator;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.util.DataIteratorTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link XLSLineIterator}.<br/><br/>
 * Created at 28.01.2009 09:10:26
 * @since 0.4.8
 * @author Volker Bergmann
 */

public class XLSLineIteratorTest extends DataIteratorTestCase {

	private static final String PERSON_FILENAME = "org/databene/document/xls/person_lines.xls";
	private static final String VALUES_FILENAME = "org/databene/document/xls/types_and_values.xls";
	private static final String EMPTY_FILENAME = "org/databene/document/xls/empty.xls";

    @Test
	public void testDefaultSheetWithFormula() throws IOException {
		// test default sheet
		XLSLineIterator iterator = new XLSLineIterator(PERSON_FILENAME);
		try {
			// check headers
			assertArrayEquals(new Object[] {"name", "age"}, iterator.next(new DataContainer<Object[]>()).getData());
			// check normal row
			expectNext(iterator, "Alice", 23L);
			// test formula
			expectNext(iterator, "Bob", 34.); // TODO map to long (like "Alice", 23L)
			// check end of sheet
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void demoDefaultSheetWithFormula() throws IOException {
    	// print out default sheet content
		XLSLineIterator iterator = new XLSLineIterator(PERSON_FILENAME);
		try {
			DataContainer<Object[]> container = new DataContainer<Object[]>();
			while ((container = iterator.next(container)) != null) {
				Object[] row = container.getData();
				System.out.println(row[0] + ", " + row[1]);
			}
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testSheet1() throws IOException {
		// test sheet 1
		XLSLineIterator iterator = new XLSLineIterator(PERSON_FILENAME, 1);
		try {
			assertArrayEquals(new Object[] {"name", "age"}, iterator.next(new DataContainer<Object[]>()).getData());
			expectNext(iterator, "Otto", 89L);
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testWithoutHeader() throws IOException {
		// test sheet 1
		XLSLineIterator iterator = new XLSLineIterator(PERSON_FILENAME, 1);
		try {
			expectNext(iterator, "name", "age");
			expectNext(iterator, "Otto", 89L);
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testTypesAndValues() throws IOException {
		// test default sheet
		XLSLineIterator iterator = new XLSLineIterator(VALUES_FILENAME);
		try {
			// check headers
			Object[] expectedHeaders = new Object[] {
					"text", "emptyText", "null", "numberAsText", "number", "date"};
			assertArrayEquals(expectedHeaders, iterator.next(new DataContainer<Object[]>()).getData());
			// check data
			Object[] data = iterator.next(new DataContainer<Object[]>()).getData();
			assertEquals(expectedHeaders.length, data.length);
			assertEquals("Simple Text", data[0]);
			assertEquals("", data[1]);
			assertEquals(null, data[2]);
			assertEquals("123", data[3]);
			assertEquals(42L, data[4]);
			assertEquals(TimeUtil.date(2011, 1, 1), data[5]);
			// check end of sheet
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testAlternativeEmptyMarker() throws IOException {
		// test default sheet
		XLSLineIterator iterator = new XLSLineIterator(EMPTY_FILENAME);
		iterator.setEmptyMarker("\"\"");
		try {
			assertArrayEquals(new Object[] { "text", "empty" }, iterator.next(new DataContainer<Object[]>()).getData());
			assertArrayEquals(new Object[] { "X", "" }, iterator.next(new DataContainer<Object[]>()).getData());
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
	// private helpers ---------------------------------------------------------
	
	private void expectNext(XLSLineIterator iterator, Object cell1, Object cell2) {
		Object[] actual = ArrayUtil.copyOfRange(iterator.next(new DataContainer<Object[]>()).getData(), 0, 2);
		Object[] expected = new Object[] { cell1, cell2 };
		assertTrue(Arrays.equals(expected, actual));
	}
	
}
