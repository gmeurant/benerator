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

package org.databene.document.flat;

import org.databene.script.Script;
import org.databene.script.ScriptException;
import org.databene.script.ScriptedDocumentWriter;
import org.databene.model.Converter;
import org.databene.model.ConversionException;
import org.databene.model.format.PadFormat;
import org.databene.model.converter.ToStringConverter;
import org.databene.model.converter.ConverterChain;
import org.databene.model.converter.FormatFormatConverter;
import org.databene.commons.SystemInfo;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes arrays as flat file columns.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 */
public class ArrayFlatFileWriter<E> extends ScriptedDocumentWriter<E[]> {

    public ArrayFlatFileWriter(Writer out, FlatFileColumnDescriptor ... descriptors) {
        this(out, (Script)null, (Script)null, descriptors);
    }

    public ArrayFlatFileWriter(Writer out, String headerScriptUrl, String footerScriptUrl, FlatFileColumnDescriptor ... descriptors)
            throws IOException {
        this(
            out,
            (headerScriptUrl != null ? Script.getInstance(headerScriptUrl) : null),
            (footerScriptUrl != null ? Script.getInstance(footerScriptUrl) : null),
            descriptors
        );
    }

    public ArrayFlatFileWriter(Writer out, Script headerScript, Script footerScript, FlatFileColumnDescriptor ... descriptors) {
        super(
            out,
            headerScript,
            new ArrayFlatFileScript(descriptors),
            footerScript
        );
    }

    // ArrayFlatFileScript ---------------------------------------------------------------------------------------------

    private static class ArrayFlatFileScript extends Script {

        private Converter<Object, String>[] converters;
        private Object[] cellsOfCurrentRow;

        public ArrayFlatFileScript(FlatFileColumnDescriptor[] descriptors) {
            this.converters = new Converter[descriptors.length];
            for (int i = 0; i < descriptors.length; i++) {
                FlatFileColumnDescriptor descriptor = descriptors[i];
                this.converters[i] = new ConverterChain(
                        new ToStringConverter(),
                        new FormatFormatConverter(
                                new PadFormat(descriptor.getWidth(), descriptor.getAlignment(), ' ')
                                )
                );
            }
            this.cellsOfCurrentRow = null;
        }

        public void setVariable(String variableName, Object variableValue) {
            if ("part".equals(variableName))
                cellsOfCurrentRow = (Object[]) variableValue;
        }

        public void execute(Writer out) throws IOException, ScriptException {
            try {
                for (int i = 0; i < cellsOfCurrentRow.length; i++)
                    out.write(converters[i].convert(cellsOfCurrentRow[i]));
                out.write(SystemInfo.lineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }

}
