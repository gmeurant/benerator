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

package org.databene.gui.swing.table.item;

import org.databene.commons.Accessor;
import org.databene.commons.Mutator;
import org.databene.commons.UpdateFailedException;
import org.databene.commons.accessor.AccessingComparator;
import org.databene.gui.swing.table.ShadowTableCellRenderer;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides read and write access to a field for Swing tables.
 * Created: 06.01.2005 18:08:26
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class FieldConnector {

    private String displayName;

	private Accessor accessor;
    private TableCellRenderer renderer;

	private Mutator mutator;
    private TableCellEditor editor;

    private Comparator comparator;

    // constructors ----------------------------------------------------------------------------------------------------
/*
    public FieldConnector(
            String displayName,
            Accessor accessor, TableCellRenderer renderer,
            Comparator comparator) {
        this(displayName, accessor, renderer, null, null, comparator);
    }
*/
    public FieldConnector(
            String displayName,
            Accessor accessor, TableCellRenderer renderer,
            Mutator mutator, TableCellEditor editor,
            Comparator comparator) {
        this.displayName = displayName;
        this.accessor = accessor;
        this.renderer = new ShadowTableCellRenderer(renderer);
        this.mutator = mutator;
        this.editor = editor;
        this.comparator = new AccessingComparator(this.accessor, comparator);
    }

    // properties ------------------------------------------------------------------------------------------------------

	public String getDisplayName() {
		return displayName;
	}

	public TableCellRenderer getRenderer() {
		return renderer;
	}

    public void setRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

	public TableCellEditor getEditor() {
		return editor;
	}

    public void setEditor(TableCellEditor editor) {
        this.editor = editor;
    }

	public Comparator getComparator() {
		return comparator;
	}

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public Object getValueFor(Object item) {
        return accessor.getValue(item);
    }

    public void setValueFor(Object item, Object value) {
        try {
            mutator.setValue(item, value);
        } catch (UpdateFailedException e) {
            throw new RuntimeException(e);
        }
    }

	public Accessor getAccessor() {
		return accessor;
	}

	public Mutator getMutator() {
		return mutator;
	}

    // event handling --------------------------------------------------------------------------------------------------

    private List<ConnectorListener> listeners = new ArrayList<ConnectorListener>();

    public void addConnectorListener(ConnectorListener listener) {
        listeners.add(listener);
    }

    public void removeConnectorListener(ConnectorListener listener) {
        listeners.remove(listener);
    }

    protected void fireColumnChanged(FieldConnector column) {
        for (ConnectorListener listener : listeners)
            listener.connectorChanged(column);
    }
}
