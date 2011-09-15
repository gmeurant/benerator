/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayFormat;
import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility methods for CSV processing.<br/>
 * <br/>
 * Created: 07.06.2007 07:44:28
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class CSVUtil {

	public static String[] parseHeader(String uri, char separator, String encoding) {
		DataIterator<String[]> cellIterator = null;
		try {
			cellIterator = new CSVLineIterator(uri, separator, true, encoding);
			DataContainer<String[]> tmp = cellIterator.next(new DataContainer<String[]>());
			if (tmp != null)
				return tmp.getData();
			else
				throw new ConfigurationError("empty CSV file");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtil.close(cellIterator);
		}
	}

    public static String[][] parseRows(String url, char separator) throws IOException {
        return parseRows(url, separator, SystemInfo.getFileEncoding());
    }

    public static String[][] parseRows(String url, char separator, String encoding) throws IOException {
        List<String[]> lines = new ArrayList<String[]>();
        CSVLineIterator iterator = new CSVLineIterator(url, separator, encoding);
        DataContainer<String[]> container = new DataContainer<String[]>();
        while ((container = iterator.next(container)) != null)
			lines.add(container.getData());
        iterator.close();
        String[][] result = new String[lines.size()][];
        return lines.toArray(result);
    }

    public static void writeRow(Writer out, char separator, String... cells) throws IOException {
        if (cells.length > 0)
            out.write(cells[0]);
        for (int i = 1; i < cells.length; i++) {
            out.write(separator);
            out.write(renderCell(cells[i], separator));
        }
        out.write(SystemInfo.getLineSeparator());
    }

    public static String renderCell(String text, char separator) {
    	if (text == null)
    		return "";
        if (text.indexOf(separator) < 0)
            return text;
        text = text.replace("\"", "\"\"");
        return '"' + text + '"';
    }

    public static String formatHeader(char separator, String... propertyNames) {
        return ArrayFormat.format(String.valueOf(separator), propertyNames) + SystemInfo.getLineSeparator();
    }

}
