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
import org.databene.gui.swing.table.item.ItemModel;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
 * Created: 27.06.2005 09:01:13
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ConnectorColumnTableModel extends AbstractTableModel {

    private ItemModel itemModel;

    public ConnectorColumnTableModel(ItemModel model) {
        setItemModel(model);
    }

    public ItemModel getItemModel() {
        return itemModel;
    }

    public void setItemModel(ItemModel model) {
        this.itemModel = model;
        if (this.itemModel != null) {
            model.getItems().addListDataListener(new Listener());
        }
        fireTableStructureChanged();
    }

    // TableModel interface --------------------------------------------------------------------------------------------

    public int getRowCount() {
        if (itemModel == null)
            return 0;
        return itemModel.getItems().getSize();
    }

    public int getColumnCount() {
        if (itemModel == null)
            return 0;
        return itemModel.getConnectors().getSize();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (itemModel == null)
            return null;
        Object item = getItem(rowIndex);
        if (item == null)
            return null;
        return getConnector(columnIndex).getValueFor(item);
    }

    private FieldConnector getConnector(int columnIndex) {
        return (FieldConnector) itemModel.getConnectors().getElementAt(columnIndex);
    }

    public String getColumnName(int columnIndex) {
        if (itemModel == null)
            return null;
        return getConnector(columnIndex).getDisplayName();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (itemModel == null)
            return false;
        return getConnector(columnIndex).getEditor() != null;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (itemModel == null)
            return;
        Object item = getItem(rowIndex);
        itemModel.mutateField(item, getConnector(columnIndex), value);
    }

    // implementation ----------------------------------------------------------------------------------------------

    private Object getItem(int row) {
        if (itemModel == null)
            return null;
        return itemModel.getItems().getElementAt(row);
    }

    private class Listener implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
            fireTableRowsInserted(e.getIndex0(), e.getIndex1());
        }

        public void intervalRemoved(ListDataEvent e) {
            fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
        }

        public void contentsChanged(ListDataEvent e) {
            fireTableRowsUpdated(e.getIndex0(), e.getIndex1());
        }


    }
}
