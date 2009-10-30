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
public class ItemColumnTableModel extends AbstractTableModel implements ListDataListener {

    private static final long serialVersionUID = 9160449788189575039L;

	private ItemModel itemModel;

    private FieldConnector headerConnector;

    public ItemColumnTableModel(ItemModel model, FieldConnector headerConnector) {
        setItemModel(model, headerConnector);
    }

    public ItemModel getItemModel() {
        return itemModel;
    }

    public void setItemModel(ItemModel model, FieldConnector headerConnector) {
        this.itemModel = model;
        if (this.itemModel != null) {
            model.getItems().addListDataListener(this);
        }
        this.headerConnector = headerConnector;
        fireTableStructureChanged();
    }

    public FieldConnector getHeaderConnector() {
        return headerConnector;
    }

    // TableModel interface - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public int getRowCount() {
        if (itemModel == null)
            return 0;
        return itemModel.getConnectors().getSize();
    }

    public int getColumnCount() {
        if (itemModel == null)
            return 0;
        return 1 + itemModel.getItems().getSize();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (itemModel == null)
            return null;
        FieldConnector connector = getConnector(rowIndex);
        if (columnIndex == 0)
            return connector.getDisplayName();
        else
            return connector.getValueFor(getItem(columnIndex));
    }

    @Override
	public String getColumnName(int columnIndex) {
        if (itemModel == null)
            return null;
        return (String)headerConnector.getValueFor(getItem(columnIndex));
    }

    @Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (itemModel == null)
            return false;
        return getConnector(rowIndex).getEditor() != null;
    }

    @Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (itemModel == null)
            return;
        Object item = getItem(columnIndex);
        itemModel.mutateField(item, getConnector(rowIndex), value);
    }

    public void intervalAdded(ListDataEvent e) {
        fireTableDataChanged(); // TODO v0.2 improve
    }

    public void intervalRemoved(ListDataEvent e) {
        fireTableDataChanged(); // TODO v0.2 improve
    }

    public void contentsChanged(ListDataEvent e) {
        fireTableDataChanged(); // TODO v0.2 improve
    }

    // implementation ----------------------------------------------------------------------------------------------

    private Object getItem(int column) {
        if (itemModel == null)
            return null;
        return itemModel.getItems().getElementAt(column - 1);
    }

    private FieldConnector getConnector(int rowIndex) {
        return (FieldConnector) itemModel.getConnectors().getElementAt(rowIndex);
    }

}
