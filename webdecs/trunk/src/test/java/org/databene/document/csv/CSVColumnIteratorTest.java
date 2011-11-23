/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.document.csv;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;

import org.databene.webdecs.DataContainer;
import org.junit.Test;

/**
 * Tests the {@link CSVColumnIterator}.<br/><br/>
 * Created: 23.11.2011 09:18:09
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class CSVColumnIteratorTest {

	@Test
    public void testNormalCase() throws Exception {
        CSVColumnIterator iterator = new CSVColumnIterator("file://org/databene/csv/columns.csv");
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] { "name" , "age" }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Alice", "23"  }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Bob"  , "34"  }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { null   , "45"  }, iterator.next(container).getData()));
        assertNull(iterator.next(container));
        iterator.close();
    }
	
	@Test
    public void testEmptyFile() throws Exception {
		CSVColumnIterator iterator = new CSVColumnIterator("string://");
        assertNull(iterator.next(new DataContainer<String[]>()));
        iterator.close();
    }
	
}
