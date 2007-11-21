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

package org.databene.document.properties;

import org.databene.script.Script;
import org.databene.script.ScriptException;
import org.databene.script.ScriptedDocumentWriter;
import org.databene.model.Converter;
import org.databene.model.ConversionException;
import org.databene.model.converter.ArrayConverter;
import org.databene.model.converter.ToStringConverter;
import org.databene.model.converter.ConverterChain;
import org.databene.commons.SystemInfo;
import org.databene.commons.BeanUtil;
import org.databene.platform.bean.BeanToPropertyArrayConverter;

import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.text.FieldPosition;
import java.beans.PropertyDescriptor;

/**
 * Writes JavaBeans to property files.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 */
public class BeanPropertiesFileWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanPropertiesFileWriter(Writer out, String ... propertyNames)
            throws IOException {
        this(out, null, (Script)null, (Script)null, propertyNames);
    }

    public BeanPropertiesFileWriter(Writer out, String prefixPattern, String headerScriptUrl, String footerScriptUrl,
                                    String ... propertyNames)
            throws IOException {
        this(
            out,
            prefixPattern,
            (headerScriptUrl != null ? Script.getInstance(headerScriptUrl) : null),
            (footerScriptUrl != null ? Script.getInstance(footerScriptUrl) : null),
            propertyNames
        );
    }

    public BeanPropertiesFileWriter(Writer out, String prefixPattern, Script headerScript, Script footerScript,
                                    String ... propertyNames) {
        super( out, headerScript, new PartScript(prefixPattern, propertyNames), footerScript);
    }

    public static <T> void persist(T bean, String filename) throws IOException {
        FileWriter out = new FileWriter(filename);
        BeanPropertiesFileWriter<T> writer = new BeanPropertiesFileWriter<T>(out, getPropertyNames(bean.getClass()));
        //writer.writeHeader(); /// TODO needed?
        writer.writeElement(bean);
        writer.close();
        out.close();
    }

    private static String[] getPropertyNames(Class beanType) {
        PropertyDescriptor[] descriptors = BeanUtil.getPropertyDescriptors(beanType);
        String[] names = new String[descriptors.length];
        for (int i = 0; i < descriptors.length; i++)
            names[i] = descriptors[i].getName();
        return names;
    }

    // scripts ---------------------------------------------------------------------------------------------------------

    private static class PartScript extends Script {

        private static final String LINE_SEPARATOR = SystemInfo.lineSeparator();

        private MessageFormat prefixFormat;
        private String[] propertyNames;
        private Converter<Object, String[]> converter;

        private int elementCount;
        private Object part;
        private StringBuffer buffer;
        FieldPosition pos0 = new FieldPosition(0);

        public PartScript(String prefixPattern, String ... propertyNames) {
            this.prefixFormat = (prefixPattern != null ? new MessageFormat(prefixPattern) : null);
            this.propertyNames = propertyNames;
            int length = propertyNames.length;
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++)
                propertyConverters[i] = new ToStringConverter();
            this.converter = new ConverterChain(
                new BeanToPropertyArrayConverter(propertyNames),
                new ArrayConverter(String.class, propertyConverters)
            );
            this.elementCount = 0;
            this.part = null;
            this.buffer = new StringBuffer();
        }

        public void setVariable(String variableName, Object variableValue) {
            if ("part".equals(variableName))
                part = variableValue;
        }

        public void execute(Writer out) throws IOException, ScriptException {
            try {
                elementCount++;
                String[] cells = converter.convert(part);
                String prefix = "";
                if (prefixFormat != null) {
                    prefixFormat.format(new Integer[] {elementCount}, buffer, pos0);
                    prefix = buffer.toString();
                    buffer.delete(0, buffer.length());
                }
                for (int i = 0; i < cells.length; i++) {
                    out.write(prefix);
                    out.write(propertyNames[i]);
                    out.write('=');
                    out.write(escape(cells[i]));
                    out.write(LINE_SEPARATOR);
                }
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }

        private String escape(String text) {
            return text.replace("\\", "\\\\");
        }
    }
}
