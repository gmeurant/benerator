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

package org.databene.gui.swing.table;

import org.databene.gui.swing.ColorUtil;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 22.05.2005 09:31:52
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ShadowTableCellRenderer implements TableCellRenderer {

    public static final Color DEFAULT_ALTERNATIVE_COLOR = new Color(222, 255, 255);

    private TableCellRenderer realRenderer;
    private Color alternativeColor;

    public ShadowTableCellRenderer(TableCellRenderer subject) {
        this(subject, DEFAULT_ALTERNATIVE_COLOR);
    }

    public ShadowTableCellRenderer(TableCellRenderer realRenderer, Color alternativeColor) {
        this.realRenderer = realRenderer;
        this.alternativeColor = alternativeColor;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (realRenderer instanceof Component)
            ((Component)realRenderer).setBackground(table.getBackground());
        Component renderer = realRenderer.getTableCellRendererComponent(table, value, false, hasFocus, row, column);
        if (renderer instanceof JComponent)
            ((JComponent)renderer).setBorder(new EmptyBorder(3, 4, 4, 2));
//        Color oldBackground = renderer.getBackground();
//        Color newBackground = new Color();

        if (isSelected)
            renderer.setBackground(ColorUtil.adaptBrightness(renderer.getBackground(), 0.85));
        else {
            if ((alternativeColor != null) && (row % 2 == 1) && table.getBackground().equals(renderer.getBackground()))
                renderer.setBackground(alternativeColor);
        }
        return renderer;
    }
}
