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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Provides stream operations.<br/>
 * <br/>
 * Created: 17.07.2006 22:17:42
 */
public final class IOUtil {

    /** The logger. */
    private static Log logger = LogFactory.getLog(IOUtil.class);
    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; de-DE; rv:1.7.5) Gecko/20041122 Firefox/1.0";

    // convenience unchecked-exception operations ----------------------------------------------------------------------

    /**
     * Convenience method that closes a stream if it is not null
     * and logs possible exceptions without disturbing program execution.
     * @param stream the stream to close
     */
    public static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    /**
     * Convenience method that closes a stream if it is not null
     * and logs possible exceptions without disturbing program execution.
     * @param stream the stream to close
     */
    public static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    /**
     * Convenience method that closes a stream if it is not null
     * and logs possible exceptions without disturbing program execution.
     * @param writer the stream to close
     */
    public static void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    /**
     * Convenience method that closes a stream if it is not null
     * and logs possible exceptions without disturbing program execution.
     * @param reader the stream to close
     */
    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    public static void flush(OutputStream stream) {
        if (stream != null) {
            try {
                stream.flush();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    public static void flush(Writer writer) {
        if (writer != null) {
            try {
                writer.flush();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }

    // URI operations --------------------------------------------------------------------------------------------------

    public static String localFilename(String uri) {
        String[] path = uri.split("/");
        return path[path.length - 1];
    }

    public static boolean isURIAvailable(String uri) {
        InputStream stream = null;
        try {
            if (uri.startsWith("http://"))
                return httpUrlAvailable(uri);
            if (!uri.contains("://"))
                uri = "file://" + uri;
            if (uri.startsWith("file://"))
                stream = getFileOrResourceAsStream(uri.substring("file://".length()));
            else
                throw new ConfigurationError("Don't know how to handle URL " + uri);
            return stream != null;
        } catch (FileNotFoundException e) {
            return false;
        } catch (ConfigurationError e) { // TODO v0.3 this is necessary 'cause otherwise we'd get return exception when a file is not found
            return false;
        } finally {
            close(stream);
        }
    }

    public static String getContentOfURI(String uri) throws IOException {
        Reader reader = getReaderForURI(uri);
        StringWriter writer = new StringWriter();
        transfer(reader, writer);
        return writer.toString();
    }

    public static BufferedReader getReaderForURI(String uri) throws IOException {
        return getReaderForURI(uri, SystemInfo.fileEncoding());
    }

    public static BufferedReader getReaderForURI(String uri, String defaultEncoding) throws IOException {
        if (uri.startsWith("http://")) {
            try {
                URLConnection connection = getConnection(uri);
                connection.connect();
                String encoding = connection.getContentEncoding();
                if (encoding == null) {
                    String ct = connection.getHeaderField("Content-Type");
                    int i = ct.indexOf("charset");
                    if (i >= 0)
                        encoding = ct.substring(i + "charset".length() + 1);
                }
                if (encoding == null)
                    encoding = defaultEncoding;
                if (encoding == null)
                    encoding = SystemInfo.fileEncoding();
                InputStream inputStream = connection.getInputStream();
                return new BufferedReader(new InputStreamReader(inputStream, encoding));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            if (defaultEncoding == null)
                defaultEncoding = SystemInfo.fileEncoding();
            return new BufferedReader(new InputStreamReader(getInputStreamForURI(uri), defaultEncoding));
        }
    }

    /**
     * Creates an InputStream from a url in String representation.
     * @param uri the source url
     * @return an InputStream theat reads te url.
     * @throws IOException if the url cannot be read.
     */
    public static InputStream getInputStreamForURI(String uri) throws IOException {
        if (uri.startsWith("http://")) {
            try {
                URLConnection connection = getConnection(uri);
                return connection.getInputStream();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (!uri.contains("://"))
            uri = "file://" + uri;
        if (uri.startsWith("file://"))
            return getFileOrResourceAsStream(uri.substring("file://".length()));
        else
            throw new ConfigurationError("Don't know how to handle URL " + uri);
    }

    public static PrintWriter getPrinterForURI(String uri, String encoding)
            throws FileNotFoundException, UnsupportedEncodingException {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(uri), encoding));
    }

    // piping streams --------------------------------------------------------------------------------------------------

    public static int transfer(Reader reader, Writer writer) throws IOException {
        int totalChars = 0;
        char[] buffer = new char[16384];

        int charsRead;
        while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
            writer.write(buffer, 0, charsRead);
            totalChars += charsRead;
        }
        return totalChars;
    }

    public static int transfer(InputStream in, OutputStream out) throws IOException {
        int totalChars = 0;
        byte[] buffer = new byte[16384];

        int charsRead;
        while ((charsRead = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, charsRead);
            totalChars += charsRead;
        }
        return totalChars;
    }

    public static void copy(String srcUri, String targetUri) throws IOException {
        logger.info("downloading " + srcUri + " --> " + targetUri);
        InputStream in = IOUtil.getInputStreamForURI(srcUri);
        OutputStream out = new FileOutputStream(targetUri); // TODO support other protocols, e.g. FTP upload
        IOUtil.transfer(in, out);
        out.close();
        in.close();
    }

    // properties file im/export ---------------------------------------------------------------------------------------

    public static Properties readProperties(String filename) throws IOException {
        // TODO support file encodings that abviate from system default
        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = getFileOrResourceAsStream(filename);
            properties.load(stream);
        } finally {
            close(stream);
        }
        return properties;
    }

    public static void writeProperties(Properties properties, String filename) throws IOException {
        // TODO support file encodings that abviate from system default
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
            properties.store(stream, "");
        } finally {
            close(stream);
        }
    }

    public static Document parseXML(String uri) throws IOException {
        InputStream stream = null;
        try {
            stream = getInputStreamForURI(uri);
            return parseXML(stream);
        } finally {
            IOUtil.close(stream);
        }
    }

    public static Document parseXML(InputStream stream) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(stream);
        } catch (ParserConfigurationException e) {
            throw new ConfigurationError(e);
        } catch (SAXException e) {
            throw new ConfigurationError(e);
        }
    }


    // text file im/export ---------------------------------------------------------------------------------------------
/*
    public static String readTextResource(String filename) throws FileNotFoundException, IOException {
        InputStreamReader reader = new InputStreamReader(getFileOrResourceAsStream(filename));
        String text = read(reader);
        reader.close();
        return text;
    }

    public static String readTextFile(File file) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(file);
        return read(reader);
    }
*/

    public static void writeTextFile(String filename, String content) throws IOException {
        writeTextFile(filename, content, SystemInfo.fileEncoding());
    }

    public static void writeTextFile(String filename, String content, String encoding) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(filename), encoding);
            transfer(new StringReader(content), writer);
        } finally {
            close(writer);
        }
    }

    // private helpers -------------------------------------------------------------------------------------------------

    /**
     * Returns an InputStream that reads a file. The file is first searched on the disk directories
     * then in the class path.
     * @param filename the name of the file to be searched.
     * @return an InputStream that accesses the file.
     * @throws FileNotFoundException if the file cannot be found.
     */
    private static InputStream getFileOrResourceAsStream(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (file.exists()) {
            return new FileInputStream(filename);
        } else
            return getResourceAsStream(filename);
    }

    /**
     * Returns an InputStream to a resource on the class path.
     * @param name the file's name
     * @return an InputStream to the resource
     */
    private static InputStream getResourceAsStream(String name) {
        if (!name.startsWith("/"))
            name = "/" + name;
        InputStream stream = IOUtil.class.getResourceAsStream(name);
        if (stream == null)
            throw new ConfigurationError("Resource not found: " + name);
        return stream;
    }

    private static boolean httpUrlAvailable(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static URLConnection getConnection(String uri) throws IOException {
        URLConnection connection = new URL(uri).openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.connect();
        return connection;
    }

}
