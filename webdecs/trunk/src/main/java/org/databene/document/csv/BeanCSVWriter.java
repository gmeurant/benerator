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

import org.databene.script.*;
import org.databene.platform.bean.BeanToPropertyArrayConverter;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.ArrayConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.ToStringConverter;

import java.io.Writer;
import java.io.IOException;

/**
 * Writes JavaBeans as CSV rows.<br/>
 * <br/>
 * Created: 06.06.2007 19:35:29
 */
public class BeanCSVWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanCSVWriter(Writer out, char separator, String ... propertyNames) {
        this(out, separator, true, propertyNames);
    }

    public BeanCSVWriter(Writer out, char separator, boolean headed, String ... propertyNames) {
        this(   out,
                separator,
                (headed ? new ConstantScript(CSVUtil.formatHeader(separator, propertyNames)) : null),
                null,
                propertyNames);
    }

    public BeanCSVWriter(Writer out, char separator,
                         Script headerScript, Script footerScript, String ... propertyNames) {
        super(out, headerScript, new BeanCSVScript(propertyNames, separator), footerScript);
    }

    // BeanCSVScript ---------------------------------------------------------------------------------------------------

    private static class BeanCSVScript implements Script {

        private char separator;
        private Converter<Object, String[]> converter;

        public BeanCSVScript(String[] propertyNames, char separator) {
            this.separator = separator;
            int length = propertyNames.length;
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++)
                propertyConverters[i] = new ToStringConverter();
            this.converter = new ConverterChain(
                new BeanToPropertyArrayConverter(propertyNames),
                new ArrayConverter(String.class, propertyConverters)
            );
        }

        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                String[] cells = converter.convert(context.get("part"));
                if (cells.length > 0)
                    out.write(cells[0]);
                for (int i = 1; i < cells.length; i++) {
                    out.write(separator);
                    out.write(CSVUtil.renderCell(cells[i], separator));
                }
                out.write(SystemInfo.lineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }
}
