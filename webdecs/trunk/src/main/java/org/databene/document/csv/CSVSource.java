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

import org.databene.webdecs.DataIterator;
import org.databene.webdecs.DataSource;

/**
 * {@link DataSource} implementation that provides for 
 * row-wise or column-wise iteration through CSV files.<br/><br/>
 * Created: 23.11.2011 11:14:54
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class CSVSource implements DataSource<String[]> {

    /** The default separator to use */
    public static final char DEFAULT_SEPARATOR = ',';

    protected String uri;
    protected char separator;
    protected String encoding;
    protected boolean ignoreEmptyLines;

	private boolean rowBased;
	
    // constructors ----------------------------------------------------------------------------------------------------

    public CSVSource(String uri, char separator, String encoding, boolean ignoreEmptyLines, boolean rowBased) {
		this.uri = uri;
		this.separator = separator;
		this.encoding = encoding;
		this.ignoreEmptyLines = ignoreEmptyLines;
		this.rowBased = rowBased;
	}

    // interface -------------------------------------------------------------------------------------------------------

    public Class<String[]> getType() {
    	return String[].class;
    }
    
	public DataIterator<String[]> iterator() {
		try {
			if (rowBased)
				return new CSVLineIterator(uri, separator, ignoreEmptyLines, encoding);
			else
				return new CSVColumnIterator(uri, separator, encoding);
		} catch (IOException e) {
			throw new RuntimeException("Error creating iterator for " + uri, e);
		}
	}
	
    public void close() {
    	// nothing to do
    }
    
}
