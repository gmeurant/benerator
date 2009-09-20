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

package org.databene.document.flat;

import org.databene.commons.HeavyweightIterator;
import org.databene.commons.IOUtil;
import org.databene.commons.ReaderLineIterator;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.format.PadFormat;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

/**
 * Tests the FlatFileLineIterator.<br/>
 * <br/>
 * Created: 27.08.2007 06:43:50
 */
public class FlatFileLineIterator implements HeavyweightIterator<String[]> {

    private PadFormat[] formats;
    private boolean ignoreEmptyLines;

    private ReaderLineIterator lineIterator;
    private int lineCount;
    private String[] nextLine;

    // constructors ----------------------------------------------------------------------------------------------------

    public FlatFileLineIterator(String uri, PadFormat[] formats) throws IOException {
        this(uri, formats, SystemInfo.getFileEncoding());
    }

    public FlatFileLineIterator(String uri, PadFormat[] formats, String encoding) throws IOException {
        this(uri, formats, false, encoding);
    }

    public FlatFileLineIterator(String uri, PadFormat[] formats, boolean ignoreEmptyLines) throws IOException {
        this(uri, formats, ignoreEmptyLines, SystemInfo.getFileEncoding());
    }

    public FlatFileLineIterator(String uri, PadFormat[] formats, boolean ignoreEmptyLines, String encoding) throws IOException {
        this(IOUtil.getReaderForURI(uri, encoding), formats, ignoreEmptyLines);
    }

    public FlatFileLineIterator(Reader reader, PadFormat[] formats) {
        this(reader, formats, false);
    }

    public FlatFileLineIterator(Reader reader, PadFormat[] formats, boolean ignoreEmptyLines) {
        this.lineIterator = new ReaderLineIterator(reader);
        this.formats = formats;
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.lineCount = 0;
        nextLine = fetchNextLine();
    }

    // interface -------------------------------------------------------------------------------------------------------

    public boolean hasNext() {
        return nextLine != null;
    }

    /**
     * Parses a CSV row into an array of Strings
     * @return an array of Strings that represents a CSV row
     */
    public String[] next() {
        String[] result = nextLine;
        if (lineIterator != null) {
            nextLine = fetchNextLine();
            lineCount++;
        } else
            nextLine = null;
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Closes the source
     */
    public void close() {
        if (lineIterator != null)
            lineIterator.close();
        lineIterator = null;
    }

    public int lineCount() {
        return lineCount;
    }

    // private helpers -------------------------------------------------------------------------------------------------

    @SuppressWarnings("null")
    private String[] fetchNextLine() {
        try {
            if (lineIterator == null)
                return null;
            String[] cells = new String[formats.length];
            int offset = 0;
            String line = null;
            boolean success = false;
            while (lineIterator.hasNext()) {
                lineCount++;
                line = lineIterator.next();
                if (line.length() > 0 || !ignoreEmptyLines) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                close();
                return null;
            } else if (StringUtil.isEmpty(line)) {
                return new String[0];
            } else {
                for (int i = 0; i < formats.length; i++) {
                    PadFormat format = formats[i];
                    String cell = line.substring(offset, Math.min(offset + format.getLength(), line.length()));
                    cells[i] = (String) format.parseObject(cell);
                    offset += format.getLength();
                }
                return cells;
            }
        } catch (ParseException e) {
            throw new RuntimeException("Unexpected error. ", e);
        }
    }
}
