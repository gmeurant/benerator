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

package org.databene.gui.swing.table.item;

import org.databene.gui.swing.table.item.ItemModel;
import org.databene.gui.swing.table.item.ItemSelectionEvent;
import org.databene.gui.swing.table.item.ItemSelectionListener;
import org.databene.gui.swing.table.item.adapter.ConnectorColumnTable;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;


/**
 * Created: 05.01.2005 18:51:27
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ItemTable extends JComponent {

    protected ConnectorColumnTable table;

    // constructors ----------------------------------------------------------------------------------------------------

    public ItemTable() {
        this(null);
    }

    public ItemTable(final ItemModel model) {
        setLayout(new BorderLayout());
        table = new ConnectorColumnTable(model);
        table.getSelectionModel().addListSelectionListener(new Listener());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // interface -------------------------------------------------------------------------------------------------------

    public void setModel(ItemModel model) {
        table.setItemModel(model);
    }

    public ItemModel getModel() {
        return table.getItemModel();
    }

    public Object itemAtPoint(int x, int y) {
        return getItemListModel().getElementAt(this.table.rowAtPoint(new Point(x, y)));
    }

    public List getSelectedItems() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        int fromIndex = selectionModel.getMinSelectionIndex();
        int toIndex = selectionModel.getMaxSelectionIndex();
        return copy(getItemListModel(), fromIndex, toIndex);
    }

    public void setSelectedItems(List list) {
        for (int i = 0; i < list.size(); i++) {
            int index = indexOf(list.get(i));
            table.getSelectionModel().addSelectionInterval(index, index);
        }
    }

    public void setSelectedItem(Object item) {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.clearSelection();
        int index = indexOf(item);
        selectionModel.addSelectionInterval(index, index);
    }

    private List listSelectionListeners = new ArrayList();

    public void addItemSelectionListener(ItemSelectionListener listener) {
        listSelectionListeners.add(listener);
    }

    public void removeItemSelectionListener(ItemSelectionListener listener) {
        listSelectionListeners.remove(listener);
    }

    protected void fireSelectionChanged() {
        ItemSelectionEvent event = new ItemSelectionEvent(this, getSelectedItems());
        for (int i = 0; i < listSelectionListeners.size(); i++) {
            ItemSelectionListener listener = (ItemSelectionListener) listSelectionListeners.get(i);
            listener.selectionChanged(event);
        }
    }

    // implementation --------------------------------------------------------------------------------------------------

    class Listener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            fireSelectionChanged();
        }
    }

    private ListModel getItemListModel() {
        return table.getItemModel().getItems();
    }

    private int indexOf(Object item) {
        ListModel listModel = getItemListModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            Object test = listModel.getElementAt(i);
            if (test.equals(item))
                return i;
        }
        return -1;
    }

    protected void setColumnHeaderRenderer(int column, TableCellRenderer renderer) {
        table.getColumnModel().getColumn(column).setHeaderRenderer(renderer);
    }

    protected JTableHeader getTableHeader() {
        return table.getTableHeader();
    }

    protected JTable getJTable() {
        return table;
    }

    private static List copy(ListModel src, int fromIndex, int toIndex) {
        int size = (fromIndex >= 0 && toIndex >= 0 ? toIndex - fromIndex + 1 : 0);
        List dst = new ArrayList(size);
        for (int i = fromIndex; i <= toIndex && fromIndex >= 0 && toIndex >= 0 ; i++)
            dst.add(src.getElementAt(i));
        return dst;
    }


}
