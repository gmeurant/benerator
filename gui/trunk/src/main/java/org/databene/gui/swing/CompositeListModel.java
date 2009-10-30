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

package org.databene.gui.swing;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Combines multiple {@link ListModel} to a composite one.<br/>
 * <br/>
 * Created: 17.11.2005 18:30:30
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class CompositeListModel extends AbstractListModel {

	private static final long serialVersionUID = 6500619424821631785L;
	
	private List<ListModel> subListModels;
    private List<Object> items;
    private ListDataListener listener;

    public CompositeListModel(ListModel ... subListModels) {
        this.subListModels = new ArrayList<ListModel>(subListModels.length);
        this.items = new ArrayList<Object>();
        this.listener = new Listener();
        for (ListModel subModel : subListModels)
            addSubListModel(subModel);
    }

    public void addSubListModel(ListModel listModel) {
        int index0 = items.size();
        subListModels.add(listModel);
        synch();
        int index1 = index0 + listModel.getSize() - 1;
        listModel.addListDataListener(listener);
        fireIntervalAdded(this, index0, index1);
    }

    public int getSize() {
        return items.size();
    }

    public Object getElementAt(int index) {
        return (index < items.size() ? items.get(index) : null);
    }

    void synch() {
        items.clear();
        for (ListModel subListModel : subListModels) {
            for (int j = 0; j < subListModel.getSize(); j++) {
                Object item = subListModel.getElementAt(j);
                items.add(item);
            }
        }
    }

    int indexOfFirstElement(ListModel listModel) {
        int base = 0;
        for (ListModel test : subListModels) {
            if (test == listModel)
                break;
            base += test.getSize();
        }
        return base;
    }

    @SuppressWarnings("synthetic-access")
    class Listener implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            synch();
            fireContentsChanged(CompositeListModel.this, 0, items.size() - 1);
        }

        public void intervalAdded(ListDataEvent e) {
            ListModel listModel = (ListModel) e.getSource();
            int base = indexOfFirstElement(listModel);
            int index0 = base + e.getIndex0();
            int index1 = base + e.getIndex1();
            synch();
            fireIntervalAdded(CompositeListModel.this, index0, index1);
        }

        public void intervalRemoved(ListDataEvent e) {
            ListModel listModel = (ListModel) e.getSource();
            int base = indexOfFirstElement(listModel);
            int index0 = base + e.getIndex0();
            int index1 = base + e.getIndex1();
            synch();
            fireIntervalRemoved(CompositeListModel.this, index0, index1);
        }
    }
}
