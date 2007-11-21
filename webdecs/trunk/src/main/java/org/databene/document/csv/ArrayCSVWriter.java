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

import org.databene.script.Script;
import org.databene.script.ScriptException;
import org.databene.script.ScriptedDocumentWriter;
import org.databene.script.ConstantScript;
import org.databene.model.converter.ToStringConverter;
import org.databene.model.Converter;
import org.databene.model.ConversionException;
import org.databene.commons.SystemInfo;

import java.io.Writer;
import java.io.IOException;

/**
 * Writes arrays as CSV rows.<br/>
 * <br/>
 * Created: 06.06.2007 19:35:29
 */
public class ArrayCSVWriter<E> extends ScriptedDocumentWriter<E[]> {

    public ArrayCSVWriter(Writer out, char separator) {
        this(out, separator, (Script)null, (Script)null);
    }

    // TODO test header generation
    public ArrayCSVWriter(Writer out, char separator, String ... columnHeads) {
        this(
                out,
                separator,
                new ConstantScript(CSVUtil.formatHeader(separator, columnHeads)),
                null);
    }

    public ArrayCSVWriter(Writer out, char separator, String headerScriptUrl, String footerScriptUrl)
            throws IOException {
        this(
            out,
            separator,
            (headerScriptUrl != null ? Script.getInstance(headerScriptUrl) : null),
            (footerScriptUrl != null ? Script.getInstance(footerScriptUrl) : null)
        );
    }

    public ArrayCSVWriter(Writer out, char separator, Script headerScript, Script footerScript) {
        super(out, headerScript, new ArrayCSVScript(separator), footerScript);
    }

    // ArrayCSVScript ---------------------------------------------------------------------------------------------

    private static class ArrayCSVScript extends Script {

        private Object[] cellsOfCurrentRow;
        private Converter<Object, String> converter;
        private char separator;

        public ArrayCSVScript(char separator) {
            this.separator = separator;
            this.cellsOfCurrentRow = null;
            this.converter = new ToStringConverter<Object>();
        }

        public void setVariable(String variableName, Object variableValue) {
            if ("part".equals(variableName))
                cellsOfCurrentRow = (Object[]) variableValue;
        }

        public void execute(Writer out) throws IOException, ScriptException {
            try {
                String text = converter.convert(cellsOfCurrentRow[0]);
                out.write(CSVUtil.renderCell(text, separator));
                for (int i = 1; i < cellsOfCurrentRow.length; i++) {
                    out.write(separator);
                    out.write(converter.convert(cellsOfCurrentRow[i]));
                }
                out.write(SystemInfo.lineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }

}
