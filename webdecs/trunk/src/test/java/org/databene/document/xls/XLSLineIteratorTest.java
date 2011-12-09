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

	private static final String XLS_FILENAME = "org/databene/document/xls/person_lines.xls";

    @Test
	public void testDefaultSheetWithFormula() throws IOException {
		// test default sheet
		XLSLineIterator iterator = new XLSLineIterator(XLS_FILENAME);
		try {
			// check headers
			assertArrayEquals(new Object[] {"name", "age"}, iterator.next(new DataContainer<Object[]>()).getData());
			// check normal row
			expectNext(iterator, "Alice", 23.0);
			// test formula
			expectNext(iterator, "Bob", 34.0);
			// check end of sheet
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testSheet1() throws IOException {
		// test sheet 1
		XLSLineIterator iterator = new XLSLineIterator(XLS_FILENAME, 1);
		try {
			assertArrayEquals(new Object[] {"name", "age"}, iterator.next(new DataContainer<Object[]>()).getData());
			expectNext(iterator, "Otto", 89.0);
			expectUnavailable(iterator);
		} finally {
			iterator.close();
		}
	}
	
    @Test
	public void testWithoutHeader() throws IOException {
		// test sheet 1
		XLSLineIterator iterator = new XLSLineIterator(XLS_FILENAME, 1);
		try {
			expectNext(iterator, "name", "age");
			expectNext(iterator, "Otto", 89.0);
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
