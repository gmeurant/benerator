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

import java.io.IOException;

import org.databene.commons.ArrayBuilder;
import org.databene.commons.SystemInfo;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;

/**
 * Gives access to content of a CSV file by String arrays
 * that represent the CSV columns as specified in RFC 4180.<br/><br/>
 * Created: 23.11.2011 07:39:32
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class CSVColumnIterator implements DataIterator<String[]> {

    /** The default separator to use */
    public static final char DEFAULT_SEPARATOR = ',';
    
    private String uri;
    private char separator;
    private String encoding;

    private String[][] rowData;
    private int currentColumnIndex;

	private int columnCount;

    // constructors ----------------------------------------------------------------------------------------------------

    /**
     * Creates a parser that reads from a uri
     * @param uri the URL to read from
     * @throws IOException if uri access failed
     */
    public CSVColumnIterator(String uri) throws IOException {
        this(uri, DEFAULT_SEPARATOR);
    }

    /**
     * Creates a parser that reads from a uri
     * @param uri the URL to read from
     * @param separator
     * @throws IOException
     */
    public CSVColumnIterator(String uri, char separator) throws IOException {
        this(uri, separator, SystemInfo.getFileEncoding());
    }

    public CSVColumnIterator(String uri, char separator, String encoding) throws IOException {
    	this.uri = uri;
    	this.separator = separator;
    	this.encoding = encoding;
        this.rowData = null;
        this.columnCount = 0;
        this.currentColumnIndex = 0;
    }

    // interface -------------------------------------------------------------------------------------------------------

    public Class<String[]> getType() {
    	return String[].class;
    }
    
    /**
     * Parses a CSV column into an array of Strings
     * @return an array of Strings that represents a CSV column
     */
	public DataContainer<String[]> next(DataContainer<String[]> wrapper) {
		assureInitialized();
		if (currentColumnIndex >= columnCount)
			return null;
		int rowCount = rowData.length;
		String[] result = new String[rowCount];
		for (int i = 0; i < rowCount; i++) {
			String[] row = rowData[i];
			result[i] = (currentColumnIndex < row.length ? row[currentColumnIndex] : null);
		}
		currentColumnIndex++;
		return wrapper.setData(result);
    }

    public void close() {
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void assureInitialized() {
        if (rowData != null)
        	return;
		try {
	        ArrayBuilder<String[]> builder = new ArrayBuilder<String[]>(String[].class); // TODO test
	        CSVLineIterator source = new CSVLineIterator(uri, separator, true, encoding);
	        DataContainer<String[]> wrapper = new DataContainer<String[]>();
	        while ((wrapper = source.next(wrapper)) != null) {
	        	String[] row = wrapper.getData();
	        	columnCount = Math.max(columnCount, row.length);
				builder.add(row);
	        }
	        rowData = builder.toArray();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file " + uri, e);
		}
    }

}
