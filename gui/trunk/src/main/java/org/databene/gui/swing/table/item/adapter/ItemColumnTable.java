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

import javax.swing.*;
import java.awt.*;

/**
 * Created: 27.06.2005 09:01:39
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ItemColumnTable extends JTable {

    private static final Dimension PREFERRED_SIZE = new Dimension(200, 0);

    // constructors ----------------------------------------------------------------------------------------------------

    public ItemColumnTable() {
        this(null, null);
    }

    public ItemColumnTable(ItemModel itemModel, FieldConnector headerConnector) {
        super(new ItemColumnTableModel(itemModel, headerConnector), new ItemColumnModel(itemModel, headerConnector));
        setAutoCreateColumnsFromModel(true);
    }

    // interface -------------------------------------------------------------------------------------------------------

    public ItemModel getItemModel() {
        return ((ItemColumnTableModel)getModel()).getItemModel();
    }

    public void setItemModel(ItemModel itemModel, FieldConnector headerConnector) {
        ItemColumnTableModel tableModel = getItemColumnTableModel();
        tableModel.setItemModel(itemModel, headerConnector);
        ((ItemColumnModel)getColumnModel()).setItemModel(itemModel, headerConnector);
    }

    public void createDefaultColumnsFromModel() {
        ItemColumnModel columnModel = (ItemColumnModel) getColumnModel();
        if (getItemModel() != null)
            columnModel.setItemModel(getItemModel(), getItemColumnTableModel().getHeaderConnector());
    }

    public int rowAtPoint(int x, int y) {
        return super.rowAtPoint(new Point(x, y));
    }

    public ListSelectionModel getSelectionModel() {
        return super.getSelectionModel();
    }

    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
/*
    public int getRowHeight() {
        return 30;
    }
*/
    // implementation --------------------------------------------------------------------------------------------------

    private ItemColumnTableModel getItemColumnTableModel() {
        return (ItemColumnTableModel)getModel();
    }

}
