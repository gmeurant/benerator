/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.collection.MapEntry;
import org.databene.commons.file.FileByNameFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Provides stream operations.<br/>
 * <br/>
 * Created: 17.07.2006 22:17:42
 * @since 0.1
 * @author Volker Bergmann
 */
public final class IOUtil {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; de-DE; rv:1.7.5) Gecko/20041122 Firefox/1.0";

    // convenience unchecked-exception operations ----------------------------------------------------------------------

    /**
     * Convenience method that closes a {@link Closeable} if it is not null
     * and logs possible exceptions without disturbing program execution.
     * @param closeable the stream to close
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error("Error closing " + closeable, e);
            }
        }
    }

    public static <T extends Collection<? extends Closeable>> void closeAll(T closeables) {
        if (closeables != null) {
        	Throwable t = null;
        	for (Closeable closeable : closeables) {
	            try {
	                closeable.close();
	            } catch (IOException e) {
	                logger.error("Error closing " + closeable, e);
	            } catch (Throwable e) {
	                t = e;
	            }
        	}
        	if (t != null)
        		throw new RuntimeException("Error closing resources", t);
        }
    }

    public static void flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (IOException e) {
                logger.error("Error flushing " + flushable, e);
            }
        }
    }

    // URI operations --------------------------------------------------------------------------------------------------

    public static String localFilename(String uri) {
        String[] path = uri.split("/");
        return path[path.length - 1];
    }

    public static boolean isURIAvailable(String uri) {
    	if (logger.isDebugEnabled())
    		logger.debug("isURIAvailable(" + uri + ')');
        InputStream stream = null;
        try {
            if (uri.startsWith("string://"))
                return true;
            if (uri.startsWith("http://"))
                return httpUrlAvailable(uri);
            if (uri.startsWith("file:") && !uri.startsWith("file://"))
                stream = getFileOrResourceAsStream(uri.substring("file:".length()), false);
            else {
	            if (!uri.contains("://"))
	                uri = "file://" + uri;
	            if (uri.startsWith("file://"))
	                stream = getFileOrResourceAsStream(uri.substring("file://".length()), false);
            }
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

    public static String[] readTextLines(String uri, boolean includeEmptyLines) throws IOException {
        ArrayBuilder<String> builder = new ArrayBuilder<String>(String.class, 100);
        BufferedReader reader = getReaderForURI(uri);
        String line;
        while ((line = reader.readLine()) != null)
        	if (line.length() > 0 || includeEmptyLines)
        		builder.add(line.trim());
        return builder.toArray();
    }

    public static BufferedReader getReaderForURI(String uri) throws IOException {
        return getReaderForURI(uri, SystemInfo.getFileEncoding());
    }

    public static BufferedReader getReaderForURI(String uri, String defaultEncoding) throws IOException {
        if (uri.startsWith("string://"))
            return new BufferedReader(new StringReader(uri.substring("string://".length())));
        else if (uri.startsWith("http://"))
            return getHttpReader(uri, defaultEncoding);
        else
            return getFileReader(uri, defaultEncoding);
    }

    public static InputStream getInputStreamForURI(String uri) throws IOException {
    	Assert.notNull(uri, "uri");
    	return getInputStreamForURI(uri, true);
    }

    /**
     * Creates an InputStream from a url in String representation.
     * @param uri the source url
     * @return an InputStream theat reads te url.
     * @throws IOException if the url cannot be read.
     */
    public static InputStream getInputStreamForURI(String uri, boolean required) throws IOException {
    	if (logger.isDebugEnabled())
    		logger.debug("getInputStreamForURI(" + uri + ", " + required + ')');
        if (uri.startsWith("string://")) {
			String content = uri.substring("string://".length());
			return new ByteArrayInputStream(content.getBytes(SystemInfo.getCharset()));
		}
        if (uri.startsWith("file://"))
            return getFileOrResourceAsStream(uri.substring("file://".length()), true);
        else if (uri.startsWith("file:"))
            return getFileOrResourceAsStream(uri.substring("file:".length()), true);
        else if (uri.contains("://")) {
            try {
                URLConnection connection = getConnection(uri);
                return connection.getInputStream();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        } else
        	return getFileOrResourceAsStream(uri, required);
    }

    public static InputStream getInputStreamForUriReference(String localUri, String contextUri, boolean required) throws IOException {
    	if (logger.isDebugEnabled())
    		logger.debug("getInputStreamForUriReference(" + localUri + ", " + contextUri + ')');
    	// do not resolve context for absolute URLs or missing contexts
    	if (StringUtil.isEmpty(contextUri) || getProtocol(localUri) != null)
    		return getInputStreamForURI(localUri, required);
    	
    	// now resolve the relative uri
    	String uri = resolveRelativeUri(localUri, contextUri);
    	
    	if (localUri.startsWith("http://")) {
            try {
                URLConnection connection = getConnection(uri);
                return connection.getInputStream();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }

        if (localUri.startsWith("file:") && !localUri.startsWith("file://"))
            return getFileOrResourceAsStream(localUri.substring("file:".length()), true);
        if (!localUri.contains("://"))
            localUri = "file://" + localUri;
        if (localUri.startsWith("file://"))
            return getFileOrResourceAsStream(localUri.substring("file://".length()), true);
        else
            throw new ConfigurationError("Can't to handle URL " + localUri);
    }

    public static String resolveRelativeUri(String relativeUri, String contextUri) {
    	if (logger.isDebugEnabled())
    		logger.debug("resolveLocalUri(" + relativeUri + ", " + contextUri + ')');
    	if (StringUtil.isEmpty(contextUri) || getProtocol(relativeUri) != null)
    		return relativeUri;
    	String protocol = getProtocol(contextUri);
    	try {
    		URL contextUrl = new URL((protocol == null ? "file:" : "")  + contextUri);
    		URL absoluteUrl = new URL(contextUrl, relativeUri);
    		String result = absoluteUrl.toString();
    		if (protocol == null) // cut off 'file:'
    			result = result.substring(5);
    		if (!"./".equals(contextUri) && (protocol == null || protocol.startsWith("file:"))) {
    			File file = new File(result);
    			if (!file.exists() && isURIAvailable(relativeUri))
    					result = relativeUri;
    		}
			return result;
    	} catch (MalformedURLException e) {
    		throw new IllegalArgumentException(e);
    	}
	}

	public static String getContextUri(String uri) {
		if (StringUtil.isEmpty(uri))
			return null;
		String protocol = getProtocol(uri);
		if (protocol != null)
			uri = uri.substring(protocol.length());
		char systemSeparator = SystemInfo.getFileSeparator();
		char uriSeparator = (uri.indexOf(systemSeparator) >= 0 ? systemSeparator : '/');
		String contextUri = StringUtil.splitOnLastSeparator(uri, uriSeparator)[0] + uriSeparator;
		if (protocol != null)
			contextUri = protocol + contextUri;
		return contextUri;
	}

	public static String getProtocol(String uri) {
		if (uri == null)
			return null;
		int sep = uri.indexOf("://");
		return (sep > 0 ? uri.substring(0, sep) : null);
	}

    public static PrintWriter getPrinterForURI(String uri, String encoding)
			throws FileNotFoundException, UnsupportedEncodingException {
    	return getPrinterForURI(uri, encoding, false, SystemInfo.getLineSeparator(), false);
    }

    public static PrintWriter getPrinterForURI(String uri, String encoding, boolean append, 
    			final String lineSeparator, boolean autoCreateFolder)
	    	throws FileNotFoundException, UnsupportedEncodingException {
    	File file = new File(uri);
    	if (autoCreateFolder)
    		FileUtil.ensureDirectoryExists(file.getParentFile());
    	return new PrintWriter(new OutputStreamWriter(new FileOutputStream(uri, append), encoding)) {
    		@Override
    		public void println() {
    			print(lineSeparator);
    		}
    	};
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

    public static void copyFile(String srcUri, String targetUri) throws IOException {
        logger.info("copying " + srcUri + " --> " + targetUri);
        InputStream in = getInputStreamForURI(srcUri);
        OutputStream out = openOutputStreamForURI(targetUri);
        IOUtil.transfer(in, out);
        out.close();
        in.close();
    }

    public static OutputStream openOutputStreamForURI(String uri) throws IOException {
    	if (uri.startsWith("file:")) {
    		uri = uri.substring(5);
    		if (uri.startsWith("//"))
    			uri = uri.substring(2);
    		return new FileOutputStream(uri);
    	} else if (uri.contains("://")) {
    		try {
	    		URL url = new URL(uri);
	    		URLConnection urlc = url.openConnection();
	    		return urlc.getOutputStream();
    		} catch (MalformedURLException e) {
    			throw new IllegalArgumentException(e);
    		}
    	}
	    return new FileOutputStream(uri);
    }

    // Properties I/O --------------------------------------------------------------------------------------------------

    public static Map<String, String> readProperties(String filename) throws IOException {
        return readProperties(filename, SystemInfo.getFileEncoding());
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, String> readProperties(String filename, String encoding) throws IOException {
        return readProperties(new OrderedMap(), filename, null, encoding);
    }
    
    @SuppressWarnings("unchecked")
    public static <V> Map<String, V> readProperties(
            String filename, Converter<Map.Entry, Map.Entry> converter) throws IOException {
        return readProperties(filename, converter, SystemInfo.getFileEncoding());
    }
    
    @SuppressWarnings("unchecked")
    public static <V> Map<String, V> readProperties(
            String filename, Converter<Map.Entry, Map.Entry> converter, String encoding) throws IOException {
        return readProperties(new OrderedMap<String, V>(), filename, converter, encoding);
    }

    @SuppressWarnings("unchecked")
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
                boolean incomplete = (line.endsWith("\\") && line.charAt(line.length() - 2) != '\\');
                if (incomplete)
                	line = line.substring(0, line.length() - 1);
                line = StringUtil.unescape(line);
                if (key != null) {
                    value += line;
                } else {
                    String[] assignment = ParseUtil.parseAssignment(line, "=", false);
                    if (assignment != null && assignment[1] != null) {
                        key = assignment[0];
                        value = assignment[1];
                    } else
                        continue;
                }
                if (!incomplete) {
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

    public static void writeProperties(Map<String, String> properties, String filename) throws IOException {
        writeProperties(properties, filename, SystemInfo.getFileEncoding());
    }

    public static void writeProperties(Map<String, String> properties, String filename, String encoding) throws IOException {
        PrintWriter stream = null;
        try {
            stream = IOUtil.getPrinterForURI(filename, encoding);
            for (Map.Entry<String, String> entry : properties.entrySet())
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
        writeTextFile(filename, content, SystemInfo.getFileEncoding());
    }

    public static void writeTextFile(String filename, String content, String encoding) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(openOutputStreamForURI(filename), encoding);
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
    	if (logger.isDebugEnabled())
    		logger.debug("getFileOrResourceAsStream(" + filename + ", " + required + ')');
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
    	if (logger.isDebugEnabled())
    		logger.debug("getResourceAsStream(" + name + ", " + required + ')');
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
    
    public static void copyDirectory(URL srcUrl, File targetDirectory, Filter<String> filenameFilter) 
    		throws IOException {
    	if (logger.isDebugEnabled())
    		logger.debug("copyDirectory(" + srcUrl + ", " + targetDirectory + ", " + filenameFilter + ")");
        String protocol = srcUrl.getProtocol();
		if (protocol.equals("file")) {
			try {
				FileUtil.copy(new File(srcUrl.toURI()), targetDirectory, true, new FileByNameFilter(filenameFilter));
			} catch (URISyntaxException e) {
				throw new RuntimeException("Unexpected exception", e);
			}
		} else if (protocol.equals("jar")) {
			String path = srcUrl.getPath();
			int separatorIndex = path.indexOf("!");
			String jarPath = path.substring(5, separatorIndex); // extract jar file name
			String relativePath = path.substring(separatorIndex + 2); // extract path inside jar file
			if (!relativePath.endsWith("/"))
				relativePath += "/";
			extractFolderFromJar(jarPath, relativePath, targetDirectory, filenameFilter);
        } else          
        	throw new UnsupportedOperationException("Protocol not supported: "+ protocol + 
        			" (URL: " + srcUrl + ")");
    }
    
    public static void extractFolderFromJar(String jarPath, String directory, File targetDirectory, 
    		Filter<String> filenameFilter) throws IOException {
    	if (logger.isDebugEnabled())
    		logger.debug("extractFolderFromJar(" + jarPath + ", " + directory + ", " + 
    				targetDirectory + ", " + filenameFilter + ")");
		JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.startsWith(directory) && !directory.equals(name) && (filenameFilter == null || filenameFilter.accept(name))) {
				String relativeName = name.substring(directory.length());
				if (entry.isDirectory()) {
					File subDir = new File(targetDirectory, relativeName);
					if (logger.isDebugEnabled())
						logger.debug("creating sub directory " + subDir);
					subDir.mkdir();
				} else {
					File targetFile = new File(targetDirectory, relativeName);
					if (logger.isDebugEnabled())
						logger.debug("copying file " + name + " to " + targetFile);
					InputStream in = jar.getInputStream(entry);
					OutputStream out = new FileOutputStream(targetFile);
					transfer(in, out);
					out.close();
					in.close();
				}
			}
		}
    }

	public static String[] listResources(URL url) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("listResources(" + url + ")");
        String protocol = url.getProtocol();
		if (protocol.equals("file")) {
			try {
				String[] result = new File(url.toURI()).list();
				if (result == null)
					result = new String[0];
				if (logger.isDebugEnabled())
					logger.debug("found file resources: " + result);
				return result;
			} catch (URISyntaxException e) {
				throw new RuntimeException("Unexpected exception", e);
			}
		} else if (protocol.equals("jar")) {
			String path = url.getPath();
			int separatorIndex = path.indexOf("!");
			String jarPath = path.substring(5, separatorIndex); // extract jar file name
			String relativePath = path.substring(separatorIndex + 2); // extract path inside jar file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries();
			Set<String> result = new HashSet<String>();
			while (entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(relativePath)) {
					String entry = name.substring(relativePath.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) // return only the top directory name of all sub directory entries
						entry = entry.substring(0, checkSubdir);
					result.add(entry);
				}
			}
			if (logger.isDebugEnabled())
				logger.debug("found jar resources: " + result);
			return result.toArray(new String[result.size()]);
        } else          
        	throw new UnsupportedOperationException("Protocol not supported: "+ protocol + 
        			" (URL: " + url + ")");
    }
    
    // helpers ---------------------------------------------------------------------------------------------------------

    private static BufferedReader getFileReader(String filename, String defaultEncoding) 
    		throws IOException, UnsupportedEncodingException {
		if (defaultEncoding == null)
		    defaultEncoding = SystemInfo.getFileEncoding();
		InputStream is = getInputStreamForURI(filename);
		PushbackInputStream in = new PushbackInputStream(is, 4);
		defaultEncoding = bomEncoding(in, defaultEncoding);
		return new BufferedReader(new InputStreamReader(in, defaultEncoding));
	}

	private static BufferedReader getHttpReader(String url, String defaultEncoding) 
			throws IOException, UnsupportedEncodingException {
		try {
		    URLConnection connection = getConnection(url);
		    connection.connect();
		    String encoding = encoding(connection, defaultEncoding);
		    InputStream inputStream = connection.getInputStream();
		    return new BufferedReader(new InputStreamReader(inputStream, encoding));
		} catch (MalformedURLException e) {
		    throw new IllegalArgumentException(e);
		}
	}

	static String encoding(URLConnection connection, String defaultEncoding) {
		String encoding = connection.getContentEncoding();
		if (StringUtil.isEmpty(encoding)) {
		    String ct = connection.getHeaderField("Content-Type");
		    if (!StringUtil.isEmpty(ct)) {
			    int i = ct.indexOf("charset");
			    if (i >= 0)
			        encoding = ct.substring(i + "charset".length() + 1).trim();
		    }
		}
		if (StringUtil.isEmpty(encoding))
		    encoding = defaultEncoding;
		if (StringUtil.isEmpty(encoding))
			encoding = SystemInfo.getFileEncoding();
		return encoding;
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
        return Encodings.UTF_8;
    }

}
