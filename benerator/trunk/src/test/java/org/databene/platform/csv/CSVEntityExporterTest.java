/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.databene.commons.IOUtil;
import org.databene.commons.ReaderLineIterator;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.Entity;
import org.databene.model.data.PartDescriptor;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link CSVEntityExporter}.<br/>
 * <br/>
 * Created at 14.03.2009 06:10:37
 * @since 0.5.8
 * @author Volker Bergmann
 */

public class CSVEntityExporterTest {
	
	private static final File DEFAULT_FILE = new File("export.csv");
	
	private final File customFile = new File("target" + File.separator + getClass().getSimpleName() + ".csv");
	
	private ComplexTypeDescriptor descriptor;
	private Entity alice;
	private Entity bob;
	
	@Before
	public void setUp() {
		// create descriptor for 'Person' entities
		descriptor = new ComplexTypeDescriptor("Person", "entity");
		descriptor.addComponent(new PartDescriptor("name", "string"));
		descriptor.addComponent(new PartDescriptor("age", "int"));
		descriptor.addComponent(new PartDescriptor("score", "inst"));
		// create Person instances for testing
		alice = new Entity("Person", "name", "Alice", "age", 23, "score", 10);
		bob = new Entity("Person", "name", "Bob", "age", 34, "score", 3);
	}
	
	// tests -----------------------------------------------------------------------------------------------------------

	@Test
	public void testEmptyFile() throws Exception {
		if (DEFAULT_FILE.exists())
			DEFAULT_FILE.delete();
		try {
			CSVEntityExporter exporter = new CSVEntityExporter();
			exporter.close();
			assertTrue(DEFAULT_FILE.exists());
			assertEquals(0, DEFAULT_FILE.length());
		} finally {
			DEFAULT_FILE.delete();
		}
	}
	
	@Test
	public void testExplicitColumns() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter(customFile.getAbsolutePath(), "name");
			cosumeAndClose(exporter);
			assertEquals("name\r\nAlice\r\nBob", getContent(customFile));
		} finally {
			customFile.delete();
		}
	}

	@Test
	public void testEndWithNewLine() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter(customFile.getAbsolutePath(), "name");
			exporter.setEndWithNewLine(true);
			cosumeAndClose(exporter);
			assertEquals("name\r\nAlice\r\nBob\r\n", getContent(customFile));
		} finally {
			customFile.delete();
		}
	}

	@Test
	public void testHeadless() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter(customFile.getAbsolutePath(), "name");
			exporter.setHeadless(true);
			cosumeAndClose(exporter);
			assertEquals("Alice\r\nBob", getContent(customFile));
		} finally {
			customFile.delete();
		}
	}

	@Test
	public void testColumnsByDescriptor() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter(customFile.getAbsolutePath(), descriptor);
			cosumeAndClose(exporter);
			assertEquals("name,age,score\r\nAlice,23,10\r\nBob,34,3", getContent(customFile));
		} finally {
			customFile.delete();
		}
	}

	@Test
	public void testColumnsByInstance() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter();
			cosumeAndClose(exporter);
			assertEquals("name,age,score\r\nAlice,23,10\r\nBob,34,3", getContent(DEFAULT_FILE));
		} finally {
			DEFAULT_FILE.delete();
		}
	}
	
	@Test
	public void testDecimalFormat() throws Exception {
		try {
			CSVEntityExporter exporter = new CSVEntityExporter();
			exporter.setDecimalPattern("0.00");
			exporter.setDecimalSeparator('-');
			Entity entity = new Entity("test", "value", 1.);
			exporter.startConsuming(entity);
			exporter.finishConsuming(entity);
			exporter.close();
			assertEquals("value\r\n1-00", getContent(DEFAULT_FILE));
		} finally {
			DEFAULT_FILE.delete();
		}
	}
	
	@Test
	public void testMultiThreaded() throws Exception {
		try {
			ComplexTypeDescriptor type = new ComplexTypeDescriptor("testtype");
			type.addComponent(new PartDescriptor("a", "string"));
			type.addComponent(new PartDescriptor("b", "string"));
			type.addComponent(new PartDescriptor("c", "string"));
			final CSVEntityExporter exporter = new CSVEntityExporter(
					DEFAULT_FILE.getAbsolutePath(), type);
			final Entity entity = new Entity(type, "a", "0123456789", "b", "5555555555", "c", "9876543210");
			ExecutorService service = Executors.newCachedThreadPool();
			Runnable runner = new Runnable() {
				public void run() {
					for (int i = 0; i < 500; i++)
						exporter.startConsuming(entity);
					exporter.finishConsuming(entity);
	            }
			};
			for (int i = 0; i < 20; i++)
				service.execute(runner);
			service.shutdown();
			service.awaitTermination(2, TimeUnit.SECONDS);
			exporter.close();
			ReaderLineIterator iterator = new ReaderLineIterator(new FileReader(DEFAULT_FILE));
			assertEquals("a,b,c", iterator.next());
			String expectedContent = "0123456789,5555555555,9876543210";
			while (iterator.hasNext()) {
				String line = iterator.next();
				assertEquals(expectedContent, line);
			}
			iterator.close();
		} finally {
			DEFAULT_FILE.delete();
		}
	}
	
	// helper methods --------------------------------------------------------------------------------------------------

	private void cosumeAndClose(CSVEntityExporter exporter) {
	    exporter.startConsuming(alice);
        exporter.finishConsuming(alice);
	    exporter.startConsuming(bob);
        exporter.finishConsuming(alice);
	    exporter.close();
    }

	private String getContent(File file) throws IOException {
	    return IOUtil.getContentOfURI(file.getAbsolutePath());
    }

}
