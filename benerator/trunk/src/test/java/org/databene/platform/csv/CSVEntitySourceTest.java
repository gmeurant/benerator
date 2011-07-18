/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.platform.csv;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.model.data.Entity;
import org.databene.model.data.ComplexTypeDescriptor;

import java.util.Iterator;

/**
 * Tests the {@link CSVEntitySource}.<br/><br/>
 * Created: 26.08.2007 12:45:17
 * @author Volker Bergmann
 */
public class CSVEntitySourceTest {

    private static final String PERSON_URI = "org/databene/platform/csv/person-bean.csv";
	private static final String PERSON_URI_WO_HEADERS = "string://Alice,23\nBob,34\nCharly,45";

    // test methods ----------------------------------------------------------------------------------------------------

    @Test
    public void testSingleRun() {
    	CSVEntitySource source = new CSVEntitySource(PERSON_URI, "Person");
    	source.setContext(new BeneratorContext());
        checkIteration(source.iterator(), "name", "age", false);
    }

    @Test
    public void testReset() {
    	CSVEntitySource source = new CSVEntitySource(PERSON_URI, "Person");
    	source.setContext(new BeneratorContext());
        checkIteration(source.iterator(), "name", "age", false);
        checkIteration(source.iterator(), "name", "age", false);
    }

    @Test
    public void testWithoutHeaders() {
    	CSVEntitySource source = new CSVEntitySource(PERSON_URI_WO_HEADERS, "Person");
    	source.setColumns(new String[] { "c1", "c2" });
    	source.setContext(new BeneratorContext());
        checkIteration(source.iterator(), "c1", "c2", false);
        checkIteration(source.iterator(), "c1", "c2", false);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void checkIteration(Iterator<Entity> iterator, String col1, String col2, boolean headersAsEntityExpected) {
        ComplexTypeDescriptor descriptor = new ComplexTypeDescriptor("Person");
        if (headersAsEntityExpected) {
            assertTrue(iterator.hasNext());
            assertEquals(new Entity(descriptor, col1, "name", col2, "age"), iterator.next());
        }
        assertTrue(iterator.hasNext());
        assertEquals(new Entity(descriptor, col1, "Alice",  col2, "23"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Entity(descriptor, col1, "Bob",    col2, "34"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(new Entity(descriptor, col1, "Charly", col2, "45"), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
}
