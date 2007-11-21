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

package org.databene.document.csv;

import org.databene.commons.ArrayFormat;
import org.databene.commons.SystemInfo;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility methods for CSV processing.<br/>
 * <br/>
 * Created: 07.06.2007 07:44:28
 */
public class CSVUtil {

    public static String[][] parseRows(String url, char separator) throws IOException {
        return parseRows(url, separator, SystemInfo.fileEncoding());
    }

    public static String[][] parseRows(String url, char separator, String encoding) throws IOException {
        List<String[]> lines = new ArrayList<String[]>();
        CSVLineIterator iterator = new CSVLineIterator(url, separator, encoding);
        while (iterator.hasNext())
            lines.add(iterator.next());
        iterator.close();
        String[][] result = new String[lines.size()][];
        return lines.toArray(result);
    }

    public static String renderCell(String text, char separator) {
        if (text.indexOf(separator) < 0)
            return text;
        text = text.replace("\"", "\"\"");
        return '"' + text + '"';
    }

    public static String formatHeader(char separator, String... propertyNames) {
        return ArrayFormat.format(String.valueOf(separator), propertyNames) + SystemInfo.lineSeparator();
    }
}
