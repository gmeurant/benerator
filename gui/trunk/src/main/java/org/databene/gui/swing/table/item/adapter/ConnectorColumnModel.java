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
public class ConnectorColumnModel extends DefaultTableColumnModel {

    ListModel connectors;
    private Listener listener;
//    private JTable table;

    public ConnectorColumnModel(ListModel connectors) {
        listener = new Listener();
        setConnectors(connectors);
    }

    // interface ------------------------------------------------------------------------------------------------------- 
/*
    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }
*/
    public void setConnectors(ListModel connectors) {
        if (this.connectors != null)
            this.connectors.removeListDataListener(listener);
        this.connectors = (connectors != null ? connectors : null);
        if (this.connectors != null)
            this.connectors.addListDataListener(listener);
        rebuildColumns();
    }

    @Override
    public TableColumn getColumn(int columnIndex) {
        return super.getColumn(columnIndex);
    }

    // implementation --------------------------------------------------------------------------------------------------

    void rebuildColumns() {
        // remove current table columns
        for (int i = tableColumns.size() - 1; i >= 0; i--) {
            TableColumn tableColumn = tableColumns.get(i);
            removeColumn(tableColumn);
        }
        // set connectors
        if (connectors != null) {
            for (int i = 0; i < connectors.getSize(); i++) {
                FieldConnector connector = (FieldConnector) connectors.getElementAt(i);
                addConnectorColumn(connector, i);
            }
        }
    }

    private void addConnectorColumn(FieldConnector connector, int i) {
        TableColumn tColumn = new ConnectorTableColumn(connector, i);
        super.addColumn(tColumn);
    }

    void insertConnectorColumn(FieldConnector connector, int i) {
        TableColumn tColumn = new ConnectorTableColumn(connector, i);
        insertTableColumn(tColumn, i);
    }

    void insertTableColumn(TableColumn column, int index) {
        if (column == null)
            throw new IllegalArgumentException("column is null");

        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn col = tableColumns.get(i);
            int modelIndex = col.getModelIndex();
            if (modelIndex >= index)
                col.setModelIndex(modelIndex + 1);
        }

        tableColumns.insertElementAt(column, index);
        column.addPropertyChangeListener(this);
        totalColumnWidth = -1; // invalidate width cache

        fireColumnAdded(new TableColumnModelEvent(this, 0, getColumnCount() - 1));
        //printColumns();
    }

    void removeTableColumn(int index) {
        super.removeColumn(getColumn(index));
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn col = tableColumns.get(i);
            int modelIndex = col.getModelIndex();
            if (modelIndex > index)
                col.setModelIndex(modelIndex - 1);
        }
        //printColumns();
    }

    class Listener implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
            for (int i = e.getIndex0(); i <= e.getIndex1(); i++)
                insertConnectorColumn((FieldConnector) connectors.getElementAt(i), i);
        }

        public void intervalRemoved(ListDataEvent e) {
            for (int i = e.getIndex1(); i >= e.getIndex0(); i--)
                removeTableColumn(i);
        }

        public void contentsChanged(ListDataEvent e) {
            rebuildColumns();
        }
    }

}
