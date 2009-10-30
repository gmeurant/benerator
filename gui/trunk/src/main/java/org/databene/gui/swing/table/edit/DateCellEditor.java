/*
 * (c) Copyright 2006 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing.table.edit;

import javax.swing.*;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created: 29.01.2005 19:29:26
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class DateCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -1314425083360573730L;
	private DateFormat df;
    private Class<?> type;

    public DateCellEditor() {
        this(DateFormat.getInstance(), Date.class);
    }

    public DateCellEditor(String pattern) {
        this(pattern, Date.class);
    }

    private DateCellEditor(DateFormat dateFormat) {
        this(dateFormat, Date.class);
    }

    private DateCellEditor(String pattern, Class<?> type) {
        this(new SimpleDateFormat(pattern), type);
    }

    private DateCellEditor(DateFormat dateFormat, Class<?> type) {
        super(new JTextField());
        this.df = dateFormat;
        this.type = type;
    }

    public static DateCellEditor createDateEditor() {
        return new DateCellEditor(DateFormat.getDateInstance());
    }

    public static DateCellEditor createDateEditor(String pattern) {
        return new DateCellEditor(pattern);
    }

    public static DateCellEditor createLongDateEditor(String pattern) {
        return new DateCellEditor(pattern, Long.class);
    }

    @Override
    public Object getCellEditorValue() {
        try {
            JTextField field = (JTextField) getComponent();
            String text = field.getText();
            if (text.length() == 0)
                return null;
            Date date = df.parse(text);
            if (Date.class.equals(type))
                return date;
            else if (Long.class.equals(type))
                return new Long(date.getTime());
            else
                throw new RuntimeException("Not a supported type: " + type);

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(getComponent(), "Illegal date", "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected,
        int row, int column) {
        String text = null;
        if (value != null)
            text = df.format((Date) value);
        return super.getTableCellEditorComponent(table, text, isSelected, row, column);
    }
    
}
