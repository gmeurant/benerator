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

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.*;

/**
 * Created: 27.06.2005 09:00:28
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ItemColumnModel extends DefaultTableColumnModel implements ListDataListener {

    private ItemModel itemModel;
    private FieldConnector headerConnector;

    public ItemColumnModel(ItemModel itemModel, FieldConnector headerConnector) {
        setItemModel(itemModel, headerConnector);
    }

    public void setItemModel(ItemModel itemModel, FieldConnector headerConnector) {
        if (itemModel != null) {
            ListModel oldListModel = itemModel.getItems();
            if (oldListModel != null)
                oldListModel.removeListDataListener(this);
        }
        this.itemModel = itemModel;
        if (itemModel != null) {
            if (this.itemModel.getItems() != null)
                this.itemModel.getItems().addListDataListener(this);
        }
        this.headerConnector = headerConnector;
        rebuildColumns();
    }

    private void rebuildColumns() {
        // remove current table columns
        for (int i = tableColumns.size() - 1; i >= 0; i--) {
            TableColumn tableColumn = tableColumns.get(i);
            removeColumn(tableColumn);
        }
        // set connectors
        if (itemModel != null) {
            TableColumn firstColumn = new TableColumn();
            firstColumn.setHeaderValue("");
            super.addColumn(firstColumn);
            for (int i = 0; i < itemModel.getItems().getSize(); i++) {
                Object item = itemModel.getItems().getElementAt(i);
                addItem(item, i + 1);
            }
        }
    }

    private void addItem(Object item, int i) {
        String title = (headerConnector != null ? (String)headerConnector.getValueFor(item) : null);
        TableColumn tColumn = new ItemColumn(i, 50, itemModel.getConnectors());
        super.addColumn(tColumn);
        fireColumnAdded(new TableColumnModelEvent(this, i, i));
    }

    private void removeItem(Object item, int i) {
        String title = (headerConnector != null ? (String)headerConnector.getValueFor(item) : null);
        TableColumn tColumn = new ItemColumn(i, 50, itemModel.getItems());
        super.removeColumn(tColumn);
        fireColumnRemoved(new TableColumnModelEvent(this, i, i));
    }

    public void intervalAdded(ListDataEvent e) {
        for (int i = e.getIndex0(); i < e.getIndex1(); i++)
            addItem(itemModel.getItems().getElementAt(i), i);
    }

    public void intervalRemoved(ListDataEvent e) {
        for (int i = e.getIndex0(); i < e.getIndex1(); i++)
            removeItem(itemModel.getItems().getElementAt(i), i);
    }

    public void contentsChanged(ListDataEvent e) {
        rebuildColumns();
    }

    public int getColumnCount() {
        return super.getColumnCount();
    }
}

