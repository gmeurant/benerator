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

package org.databene.gui.swing.table.item;

import javax.swing.*;
import java.util.List;

/**
 * Created: 01.02.2005 20:49:22
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class DefaultItemModel implements ItemModel {

    protected ListModel items;
    protected ListModel connectors;

    public DefaultItemModel(List items, List connectorList) {
        this(new DefaultItemListModel(items), new DefaultConnectorListModel(connectorList));
    }

    public DefaultItemModel(ListModel itemListModel, List connectorList) {
        this(itemListModel, new DefaultConnectorListModel(connectorList));
    }

    public DefaultItemModel(List items, ListModel connectorListModel) {
        this(new DefaultItemListModel(items), connectorListModel);
    }

    public DefaultItemModel(ListModel itemListModel, ListModel connectorListModel) {
        this.items = itemListModel;
        this.connectors = connectorListModel;
    }

    // ItemModel interface ---------------------------------------------------------------------------------------------

    public ListModel getItems() {
        return items;
    }
    public ListModel getConnectors() {
        return connectors;
    }

    public Object accessField(Object item, FieldConnector connector) {
        return connector.getValueFor(item);
    }

    public void mutateField(Object item, FieldConnector connector, Object value) {
        connector.setValueFor(item, value);
    }

}
