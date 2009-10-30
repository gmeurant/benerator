/*
 * (c) Copyright 2005-2009 by Volker Bergmann. All rights reserved.
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
import javax.swing.event.CellEditorListener;
import java.awt.Component;
import java.util.EventObject;

/**
 * Created: 27.06.2005 08:59:55
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class ItemColumn extends TableColumn {

    private static final long serialVersionUID = -2356993997677902454L;

	public ItemColumn(int index, int minWidth, ListModel connectorListModel) {
        super(index);
        if (minWidth == 0)
            minWidth = 64;
        setMinWidth(minWidth);
        setPreferredWidth(minWidth);
        setConnectorListModel(connectorListModel);
    }

    public void setConnectorListModel(ListModel connectorListModel) {
        setCellRenderer(new Renderer(connectorListModel));
        setCellEditor(new Editor(connectorListModel));
    }

    // Renderer --------------------------------------------------------------------------------------------------------

    private class Renderer implements TableCellRenderer {

        private ListModel connectorListModel;

        public Renderer(ListModel connectorListModel) {
            this.connectorListModel = connectorListModel;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (connectorListModel == null)
                throw new RuntimeException("connectorListModel has not been initialized");
            FieldConnector connector = (FieldConnector) connectorListModel.getElementAt(row);
            TableCellRenderer renderer = connector.getRenderer();
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    // Editor ----------------------------------------------------------------------------------------------------------

    private class Editor implements TableCellEditor {

        private ListModel connectorListModel;
        private TableCellEditor currentEditor;

        public Editor(ListModel connectorListModel) {
            this.connectorListModel = connectorListModel;
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (connectorListModel == null)
                throw new RuntimeException("connectorListModel has not been initialized");
            FieldConnector connector = (FieldConnector) connectorListModel.getElementAt(row);
            this.currentEditor = connector.getEditor();
            return currentEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        public void addCellEditorListener(CellEditorListener l) {
            currentEditor.addCellEditorListener(l);
        }

        public void cancelCellEditing() {
            currentEditor.cancelCellEditing();
        }

        public Object getCellEditorValue() {
            return currentEditor.getCellEditorValue();
        }

        public boolean isCellEditable(EventObject anEvent) {
            return currentEditor.isCellEditable(anEvent);
        }

        public void removeCellEditorListener(CellEditorListener l) {
            currentEditor.removeCellEditorListener(l);
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return currentEditor.shouldSelectCell(anEvent);
        }

        public boolean stopCellEditing() {
            return currentEditor.stopCellEditing();
        }
    }

}

