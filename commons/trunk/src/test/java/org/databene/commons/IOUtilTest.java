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

import junit.framework.TestCase;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Arrays;

/**
 * Created: 21.06.2007 08:31:28
 */
public class IOUtilTest  extends TestCase {

    public void testClose() {
        IOUtil.close(new ByteArrayInputStream(new byte[0]));
        IOUtil.close(new ByteArrayOutputStream());
        IOUtil.close(new StringWriter());
        IOUtil.close(new StringReader("abc"));
    }

    public void testFlush() {
        IOUtil.flush(new StringWriter());
    }

    public void testLocalFilename() throws MalformedURLException {
        assertEquals("product-info.jsp", IOUtil.localFilename("http://localhost:80/shop/product-info.jsp"));
    }

    public void testIsURIAvaliable() throws IOException, UnsupportedEncodingException {
        assertTrue(IOUtil.isURIAvailable("org/databene/commons/names.csv"));
    }

    public void testGetContentOfURI() throws IOException {
        assertEquals("Alice,Bob\r\nCharly", IOUtil.getContentOfURI("file://org/databene/commons/names.csv"));
        assertEquals("Alice,Bob\r\nCharly", IOUtil.getContentOfURI("org/databene/commons/names.csv"));
    }

    public void testGetInputStreamForURI() throws IOException, UnsupportedEncodingException {
        InputStream stream = IOUtil.getInputStreamForURI("org/databene/commons/names.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "cp1252"));
        assertEquals("Alice,Bob", reader.readLine());
        assertEquals("Charly", reader.readLine());
        assertNull(reader.readLine());
        reader.close();
    }

    public void testGetReaderForURI() throws IOException, UnsupportedEncodingException {
        BufferedReader reader = IOUtil.getReaderForURI("org/databene/commons/names.csv");
        assertEquals("Alice,Bob", reader.readLine());
        assertEquals("Charly", reader.readLine());
        assertNull(reader.readLine());
        reader.close();
    }

    public void testTransferStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("abcdefg".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.transfer(in, out);
        assertTrue(Arrays.equals("abcdefg".getBytes(), out.toByteArray()));
    }

    public void testTransferReaderWriter() throws IOException {
        StringReader in = new StringReader("abcdefg");
        StringWriter out = new StringWriter();
        IOUtil.transfer(in, out);
        assertEquals("abcdefg", out.toString());
    }

    public void testReadProperties() throws IOException {
        Properties properties = IOUtil.readProperties("org/databene/commons/test.properties");
        assertEquals(2, properties.size());
        assertEquals("b", properties.getProperty("a"));
        assertEquals("z", properties.getProperty("x.y"));
    }

    public void testWriteProperties() {
        // TODO v0.3 implement
    }

    public void testWriteTestFile() {
        // TODO v0.3 implement
    }

    public void parseXMLTest() {
        // TODO v0.3 implement
    }
}
