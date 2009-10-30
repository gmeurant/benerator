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
 * Created: 15.05.2005 00:15:20
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class DefaultConnectorListModel extends AbstractListModel implements ConfigurableListModel {

    private static final long serialVersionUID = 8777778284082740252L;
    
	private List<FieldConnector> connectors;
    private ColumnListener columnListener;
    private Object displaySettings;

    public DefaultConnectorListModel(List<FieldConnector> connectors) {
        this(connectors, null);
    }

    public DefaultConnectorListModel(List<FieldConnector> connectors, Object displaySettings) {
        this.connectors = connectors;
        this.displaySettings = displaySettings;
        columnListener = new ColumnListener();
        registerColumnListener();
    }

    public void dispose() {
        unregisterColumnListener();
        columnListener = null;
        connectors = null;
    }

    public Object getConfiguration() {
        return displaySettings;
    }

    // ListModel interface ---------------------------------------------------------------------------------------------

    public int getSize() {
        return connectors.size();
    }

    public Object getElementAt(int index) {
        return connectors.get(index);
    }

    // column listener handling ----------------------------------------------------------------------------------------

    private void registerColumnListener() {
        // register ColumnListener for each accessor
        for (int i = 0; i < this.connectors.size(); i++) {
            FieldConnector accessor = connectors.get(i);
            accessor.addConnectorListener(columnListener);
        }
    }

    private void unregisterColumnListener() {
        // unregister ColumnListener for each accessor
        for (int i = 0; i < this.connectors.size(); i++) {
            FieldConnector accessor = connectors.get(i);
            accessor.removeConnectorListener(columnListener);
        }
    }

    class ColumnListener implements ConnectorListener {
        @SuppressWarnings("synthetic-access")
        public void connectorChanged(FieldConnector accessor) {
            int index = connectors.indexOf(accessor);
            fireContentsChanged(DefaultConnectorListModel.this, index, index);
        }
    }

}
