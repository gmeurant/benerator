/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benclipse.wizards;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.databene.benclipse.archetype.Archetype;
import org.databene.benclipse.archetype.ArchetypeManager;
import org.databene.benclipse.core.BenclipseLogger;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.SWTUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * WizardPage that lets the user choose a benerator project archetype.<br/>
 * <br/>
 * Created at 15.02.2009 08:16:53
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class ArchetypePage extends BeneratorWizardPage implements ISelectionChangedListener {
	
	private TableViewer viewer;

    public ArchetypePage() {
	    super("ArchetypePage");
		setPageComplete(false);
		setTitle(Messages.getString("wizard.project.archetype.title"));
		setDescription(Messages.getString("wizard.project.archetype.description"));
		setPageComplete(true); 
    }
    
    // interface -------------------------------------------------------------------------------------------------------

	public void createControl(Composite parent) {
		Composite control = SWTUtil.createComposite(parent, 1, 1, GridData.FILL_BOTH, true);
		createArchetypeTable(control);
		setControl(control);
	}

	public Archetype getSelectedArchetype() {
		return (Archetype) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
	}
	
    public void selectionChanged(SelectionChangedEvent arg0) {
    	update();
    }
    
    public boolean isRepro() {
	    return getSelectedArchetype().getDirectory().getName().endsWith("reprodb");
    }

    // non-public helper methods ---------------------------------------------------------------------------------------

	private void createArchetypeTable(Composite control) {
		try {
		    viewer = new TableViewer(control, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
	
		    Table table = viewer.getTable();
		    table.setHeaderVisible(true);
		    table.setLinesVisible(true);
	
		    TableColumn column0 = new TableColumn(table, SWT.LEFT);
		    column0.setWidth(400);
		    column0.setText(Messages.getString("wizard.project.archetype.templates"));
	
		    GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		    table.setLayoutData(tableData);
	
		    ArchetypeManager archetypeManager = ArchetypeManager.getInstance();
		    viewer.setLabelProvider(new ArchetypeLabelProvider());
		    viewer.setContentProvider(new ArchetypeContentProvider());
			viewer.setInput(archetypeManager);
		    viewer.setSelection(new StructuredSelection(archetypeManager.getDefaultArchetype()));
		    
		    viewer.addSelectionChangedListener(this);
		} catch (IOException e) {
			BenclipseLogger.log("", e);
		}
    }
	
	private void update() {
        setErrorMessage(null);
        setMessage(null);
        super.update(null, null);
    }
	
	// helper classes --------------------------------------------------------------------------------------------------

	static final class ArchetypeContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object parent) {
			return ((ArchetypeManager) parent).getArchetypes();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	static class ArchetypeLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
	      return element.toString();
	    }

        public Image getColumnImage(Object element, int columnIndex) {
            Archetype archetype = (Archetype) element;
	        try {
				File iconFile = archetype.getIconFile();
				if (iconFile == null)
					return null;
				return ImageDescriptor.createFromURL(iconFile.toURI().toURL()).createImage();
            } catch (MalformedURLException e) {
            	BenclipseLogger.log("Error loading icon of archetype " + archetype, e);
            }
            return null;
        }
	}

}
