/*
 * (c) Copyright 2005-2008 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing.table.render;

import org.databene.commons.TimeUtil;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.Component;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Formats a Date or Long object as a Date string.<br/>
 * <br/>
 * Created: 30.01.2005 09:17:47
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class DateTableCellRenderer extends DefaultTableCellRenderer{

    private DateFormat df;

    public DateTableCellRenderer() {
        this.df = SimpleDateFormat.getDateInstance();
    }

    public DateTableCellRenderer(String pattern) {
        this.df = new SimpleDateFormat(pattern);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        String text = null;
        if (value != null) {
            if (value instanceof Long)
                value = TimeUtil.date(((Long)value).longValue());
            text = df.format((Date)value);
        }
        Component renderer = super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
        return renderer;
    }
}
