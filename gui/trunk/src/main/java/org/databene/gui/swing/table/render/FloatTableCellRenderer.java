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

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.awt.Component;

/**
 * Created: 01.03.2005 22:37:14
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class FloatTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1628805385067179328L;
    
	private NumberFormat format;
    private final static Float nullValue = new Float(0);

    public FloatTableCellRenderer(int precision) {
        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        this.format = new DecimalFormat();
        format.setMinimumFractionDigits(precision);
        format.setMaximumFractionDigits(precision);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            value = null;
        else if (nullValue.equals(value))
            value = "0";
        else
            value = format.format(value);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}
