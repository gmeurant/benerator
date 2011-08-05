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

import org.databene.script.AbstractScript;
import org.databene.script.Script;
import org.databene.script.ScriptException;
import org.databene.script.ScriptUtil;
import org.databene.script.ScriptedDocumentWriter;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.FormatFormatConverter;
import org.databene.commons.converter.ToStringConverter;
import org.databene.commons.format.PadFormat;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes arrays as flat file columns.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 * @author Volker Bergmann
 */
public class ArrayFixedWidthWriter<E> extends ScriptedDocumentWriter<E[]> {

    public ArrayFixedWidthWriter(Writer out, FixedWidthColumnDescriptor ... descriptors) {
        this(out, (Script)null, (Script)null, descriptors);
    }

    public ArrayFixedWidthWriter(Writer out, String headerScriptUrl, String footerScriptUrl, FixedWidthColumnDescriptor ... descriptors)
            throws IOException {
        this(
            out,
            (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null),
            (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null),
            descriptors
        );
    }

    public ArrayFixedWidthWriter(Writer out, Script headerScript, Script footerScript, FixedWidthColumnDescriptor ... descriptors) {
        super(
            out,
            headerScript,
            new ArrayFixedWidthScript(descriptors),
            footerScript
        );
    }

    // ArrayFlatFileScript ---------------------------------------------------------------------------------------------

    private static class ArrayFixedWidthScript extends AbstractScript {

        private Converter<Object, String>[] converters;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public ArrayFixedWidthScript(FixedWidthColumnDescriptor[] descriptors) {
            this.converters = new Converter[descriptors.length];
            for (int i = 0; i < descriptors.length; i++) {
                FixedWidthColumnDescriptor descriptor = descriptors[i];
                this.converters[i] = new ConverterChain(
                        new ToStringConverter(),
                        new FormatFormatConverter(String.class, 
                                new PadFormat(descriptor.getWidth(), descriptor.getAlignment(), ' '),
                                true
                                )
                );
            }
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                Object[] cellsOfCurrentRow = (Object[]) context.get("part");
                for (int i = 0; i < cellsOfCurrentRow.length; i++)
                    out.write(converters[i].convert(cellsOfCurrentRow[i]));
                out.write(SystemInfo.getLineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }

}
