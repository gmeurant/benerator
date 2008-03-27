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
import org.databene.commons.collection.MapEntry;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides stream operations.<br/>
 * <br/>
 * Created: 17.07.2006 22:17:42
 * @author Volker Bergmann
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
                stream = getFileOrResourceAsStream(uri.substring("file://".length()), false);
            return stream != null;
        } catch (FileNotFoundException e) {
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
            InputStream is = getInputStreamForURI(uri);
            PushbackInputStream in = new PushbackInputStream(is, 4);
            defaultEncoding = bomEncoding(in, defaultEncoding);
            return new BufferedReader(new InputStreamReader(in, defaultEncoding));
        }
    }

    private static String bomEncoding(PushbackInputStream in, String defaultEncoding) throws IOException {
        int b1 = in.read();
        if (b1 == -1)
            return defaultEncoding;
        if (b1 != 0xEF) {
            in.unread(b1);
            return defaultEncoding;
        }
        int b2 = in.read();
        if (b2 == -1) {
            in.unread(b1);
            return defaultEncoding;
        }
        if (b2 != 0xBB) {
            in.unread(b2);
            in.unread(b1);
            return defaultEncoding;
        }
        int b3 = in.read();
        if (b3 == -1) {
            in.unread(b2);
            in.unread(b1);
            return defaultEncoding;
        }
        if (b3 != 0xBF) {
            in.unread(b3);
            in.unread(b2);
            in.unread(b1);
            return defaultEncoding;
        }
        return "UTF-8";
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
            return getFileOrResourceAsStream(uri.substring("file://".length()), true);
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
        OutputStream out = new FileOutputStream(targetUri); // TODO v0.4.1 support other protocols, e.g. FTP upload
        IOUtil.transfer(in, out);
        out.close();
        in.close();
    }

    // Properties I/O --------------------------------------------------------------------------------------------------

    public static Map<String, String> readProperties(String filename) throws IOException {
        return readProperties(filename, SystemInfo.fileEncoding());
    }
    
    public static Map<String, String> readProperties(String filename, String encoding) throws IOException {
        return readProperties(new HashMap(), filename, null, encoding);
    }
    
    public static <V> Map<String, V> readProperties(
            String filename, Converter<Map.Entry, Map.Entry> converter) throws IOException {
        return readProperties(filename, converter, SystemInfo.fileEncoding());
    }
    
    public static <V> Map<String, V> readProperties(
            String filename, Converter<Map.Entry, Map.Entry> converter, String encoding) throws IOException {
        return readProperties(new HashMap<String, V>(), filename, converter, encoding);
    }

    private static <M extends Map> M readProperties(M target, String filename, 
            Converter<Map.Entry, Map.Entry> converter, String encoding) throws IOException {
        Reader reader = null;
        ReaderLineIterator iterator = null;
        try {
            reader = IOUtil.getReaderForURI(filename, encoding);
            iterator = new ReaderLineIterator(reader);
            String key = null;
            String value = "";
            while (iterator.hasNext()) {
                String line = iterator.next();
                line = line.trim();
                if (line.startsWith("#"))
                    continue;
                if (key != null) {
                    value += normalizeLine(line);
                } else {
                    String[] assignment = ParseUtil.parseAssignment(line, "=");
                    if (assignment != null && assignment[1] != null) {
                        key = assignment[0];
                        value = normalizeLine(assignment[1]);
                    } else
                        continue;
                }
                if (!line.endsWith("\\")) {
                    if (converter != null) {
                        Map.Entry entry = new MapEntry(key, value);
                        entry = converter.convert(entry);
                        target.put(entry.getKey(), entry.getValue());
                    } else
                        target.put(key, value);
                    key = null;
                    value = "";
                }
            }
        } finally {
            if (iterator != null)
                iterator.close();
            else
                IOUtil.close(reader);
        }
        return target;
    }

    private static String normalizeLine(String line) {
        return (line.endsWith("\\") ? line.substring(0, line.length() - 1) : line);
    }

    public static void writeProperties(Properties properties, String filename) throws IOException {
        writeProperties(properties, filename, SystemInfo.fileEncoding());
    }

    public static void writeProperties(Properties properties, String filename, String encoding) throws IOException {
        PrintWriter stream = null;
        try {
            stream = IOUtil.getPrinterForURI(filename, encoding);
            for (Map.Entry<Object, Object> entry : properties.entrySet())
                stream.println(entry.getKey() + "=" + entry.getValue());
        } finally {
            IOUtil.close(stream);
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
    private static InputStream getFileOrResourceAsStream(String filename, boolean required) throws FileNotFoundException {
        File file = new File(filename);
        if (file.exists()) {
            return new FileInputStream(filename);
        } else
            return getResourceAsStream(filename, required);
    }

    /**
     * Returns an InputStream to a resource on the class path.
     * @param name the file's name
     * @return an InputStream to the resource
     */
    private static InputStream getResourceAsStream(String name, boolean required) {
        if (!name.startsWith("/"))
            name = "/" + name;
        InputStream stream = IOUtil.class.getResourceAsStream(name);
        if (required && stream == null)
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

    public static byte[] getBinaryContentOfUri(String uri) throws IOException {
        InputStream in = getInputStreamForURI(uri);
        ByteArrayOutputStream out = new ByteArrayOutputStream(25000);
        transfer(in, out);
        return out.toByteArray();
    }
}
