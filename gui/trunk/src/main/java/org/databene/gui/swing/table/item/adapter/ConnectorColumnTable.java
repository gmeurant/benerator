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

import org.databene.gui.swing.table.item.ItemModel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created: 27.06.2005 09:01:39
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ConnectorColumnTable extends JTable {

    public ConnectorColumnTable(ItemModel model) {
        super(new ConnectorColumnTableModel(model), new ConnectorColumnModel((model != null ? model.getConnectors(): null)));
        setAutoCreateColumnsFromModel(true);
        setRowHeight(16);
        setShowHorizontalLines(false);
        getColumnModel().addColumnModelListener(new Listener());
//        setSurrendersFocusOnKeystroke(true);
    }

    // interface -------------------------------------------------------------------------------------------------------

    public void setItemModel(ItemModel itemModel) {
        ((ConnectorColumnTableModel)getModel()).setItemModel(itemModel);
        ListModel connectors = (itemModel != null ? itemModel.getConnectors() : null);
        setConnectors(connectors);
    }

    public ItemModel getItemModel() {
        return ((ConnectorColumnTableModel)getModel()).getItemModel();
    }

    public void createDefaultColumnsFromModel() {
        if (getItemModel() != null)
            setConnectors(getItemModel().getConnectors());
    }

    public int rowAtPoint(int x, int y) {
        return super.rowAtPoint(new Point(x, y));
    }

    public ListSelectionModel getSelectionModel() {
        return super.getSelectionModel();
    }

    // implementation --------------------------------------------------------------------------------------------------

    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        Component component = super.prepareEditor(editor, row, column);
        if (component instanceof JTextComponent)
            ((JTextComponent)component).selectAll();
        return component;
    }

    private void setConnectors(ListModel connectors) {
        ConnectorColumnModel columnModel = (ConnectorColumnModel) getColumnModel();
        columnModel.setConnectors(connectors);
        initColumnWidths();
    }

    private void initColumnWidths() {
        TableCellRenderer headerRenderer = getTableHeader().getDefaultRenderer();
        ConnectorColumnModel columnModel = (ConnectorColumnModel) getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn tColumn = getColumnModel().getColumn(i);
            Component comp = headerRenderer.getTableCellRendererComponent(this, tColumn.getHeaderValue(), false, false, 0, 0);
            int headerWidth = comp.getPreferredSize().width;
            comp = tColumn.getCellRenderer().getTableCellRendererComponent(this, null, false, false, 0, 0);
            int cellWidth = comp.getPreferredSize().width;
            tColumn.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    private class Listener implements TableColumnModelListener {

        public void columnAdded(TableColumnModelEvent e) {
            initColumnWidths();
        }

        public void columnRemoved(TableColumnModelEvent e) {
        }

        public void columnMoved(TableColumnModelEvent e) {
        }

        public void columnMarginChanged(ChangeEvent e) {
        }

        public void columnSelectionChanged(ListSelectionEvent e) {
        }
    }
}
