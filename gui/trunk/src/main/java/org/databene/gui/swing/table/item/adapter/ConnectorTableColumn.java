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

package org.databene.gui.swing.table.item.adapter;

import org.databene.gui.swing.table.item.FieldConnector;

import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.*;

/**
 * Created: 27.06.2005 08:59:55
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ConnectorTableColumn extends TableColumn {

    private static final long serialVersionUID = 7889497773878324784L;
	private FieldConnector connector;

    public ConnectorTableColumn(FieldConnector connector, int index) {
        super(index);
        this.connector = connector;
        TableCellEditor editor = connector.getEditor();
        if (editor instanceof DefaultCellEditor) {
            ((DefaultCellEditor)editor).setClickCountToStart(1); 
        }
/*
if (connector.getMinWidth() > 0) {
    int width = Math.max(connector.getMinWidth(), SwingUtil.tableStringWidth(connector.getDisplayName()));
    setWidth(width);
}*/
    }

    @Override
    public Object getHeaderValue() {
        return connector.getDisplayName();
    }

    @Override
    public TableCellRenderer getCellRenderer() {
        return connector.getRenderer();
    }

    @Override
    public TableCellEditor getCellEditor() {
        return connector.getEditor();
    }
/*
    public int getMinWidth() {
        return connector.getMinWidth();
    }

    public int getPreferredWidth() {
        return connector.getMinWidth();
    }
/*
        public int getWidth() {
            return connector.getMinWidth();
        }
/*
        public int getMaxWidth() {
            return connector.getMinWidth();
        }
*/

    @Override
    public String toString() {
        return getClass().getName() + "['" + getHeaderValue() + "', " + getModelIndex() + "]";
    }
}

