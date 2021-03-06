/*
 * (c) Copyright 2006-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.document.csv;

import org.databene.commons.ConversionException;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;

import java.io.IOException;

/**
 * Iterates through cells of a CSV file.<br/>
 * <br/>
 * Created: 26.08.2006 18:52:08
 * @author Volker Bergmann
 */
public class CSVCellIterator implements DataIterator<String> {

    /** The source uri */
    private String uri;

    private char separator;

    /** The tokenizer for CSV file access */
    private CSVTokenizer tokenizer;

    // constructors ----------------------------------------------------------------------------------------------------

    public CSVCellIterator(String uri, char separator, String encoding) throws IOException {
        this.uri = uri;
        this.separator = separator;
        this.tokenizer = new CSVTokenizer(uri, separator, encoding);
        skipEOLs();
    }

    // properties ------------------------------------------------------------------------------------------------------

    public String getUri() {
        return uri;
    }

    public char getSeparator() {
        return separator;
    }

    // Iterator implementation -----------------------------------------------------------------------------------------

    public Class<String> getType() {
    	return String.class;
    }
    
	public DataContainer<String> next(DataContainer<String> wrapper) {
    	if (tokenizer == null)
    		return null;
        try {
            String result = tokenizer.cell;
            skipEOLs();
            if (tokenizer.ttype == CSVTokenType.EOF)
                close();
            return wrapper.setData(result);
        } catch (ConversionException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Operation not supported: remove()");
    }

    public void close() {
        if (tokenizer != null) {
            tokenizer.close();
            tokenizer = null;
        }
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void skipEOLs() {
        try {
            do {
                tokenizer.next();
            } while (tokenizer.ttype == CSVTokenType.EOL);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
