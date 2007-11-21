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

import org.databene.commons.BeanUtil;

import java.io.PrintWriter;
import java.io.Writer;
import java.beans.PropertyDescriptor;

/**
 * Writes JavaBeans as CSV files.
 * Created: 20.01.2007 08:54:01
 * @deprecated this has been replaced by BeanCSVWriter
 */
public class JavaBeanToCSVMapper {

    private char separator;
    private String nullValue;
    private String[] propertyNames;

    private transient PrintWriter pw;

    private transient boolean headerPrinted;

    // constructors ----------------------------------------------------------------------------------------------------

    public JavaBeanToCSVMapper(Writer writer) {
        this(writer, ',', "");
    }

    public JavaBeanToCSVMapper(Writer writer, char separator, String nullValue) {
        this.separator = separator;
        this.nullValue = nullValue;
        headerPrinted = false;
        this.pw = new PrintWriter(writer);
    }

    // interface -------------------------------------------------------------------------------------------------------

    public void store(Object bean) {
        if (!headerPrinted)
            printHeader(bean);
        for (int i = 0; i < propertyNames.length; i++) {
            Object propertyValue = BeanUtil.getPropertyValue(bean, propertyNames[i]);
            if (propertyValue == null)
                propertyValue = nullValue;
            pw.print(propertyValue);
            if (i < propertyNames.length - 1)
                pw.print(separator);
        }
        pw.println();
    }

    // implementation --------------------------------------------------------------------------------------------------

    private void printHeader(Object bean) {
        if (propertyNames == null)
            createAttributeNamesBySample(bean);
        for (int i = 0; i < propertyNames.length; i++) {
            pw.print(propertyNames[i]);
            if (i < propertyNames.length - 1)
                pw.print(separator);
        }
        pw.println();
        headerPrinted = true;
    }

    private void createAttributeNamesBySample(Object bean) {
        PropertyDescriptor[] descriptors = BeanUtil.getPropertyDescriptors(bean.getClass());
        propertyNames = new String[descriptors.length - 1];
        int i = 0;
        for (PropertyDescriptor descriptor : descriptors) {
            if (!"class".equals(descriptor.getName()))
                propertyNames[i++] = descriptor.getName();
        }
    }
}
