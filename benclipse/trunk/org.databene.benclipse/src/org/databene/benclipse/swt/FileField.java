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

package org.databene.benclipse.swt;

import java.io.File;
import java.util.Observable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Widget for choosing a file by name or by workspace browser.<br/>
 * <br/>
 * Created at 05.03.2009 15:25:42
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class FileField extends Observable {
	
	Text text;
	Button button;
	Shell shell;
	File file;
	boolean workspaceOnly;
	boolean save;
	String[] filterExtensions;
	String nullName;
	
	public FileField(Composite parent, String labelText, String buttonText, int textHSpan) {
		this.text = SWTUtil.createLabeledText(parent, labelText, textHSpan);
		this.shell = parent.getShell();
		this.button = SWTUtil.createButton(parent, buttonText);
		this.button.addSelectionListener(new Listener());
		this.save = false;
		this.workspaceOnly = true;
		this.nullName = "";
	}
	
	public void setWorkspaceOnly(boolean restrictedToWorkspace) {
    	this.workspaceOnly = restrictedToWorkspace;
    }
	
	public void setNullName(String nullName) {
    	this.nullName = nullName;
    	if (file == null)
    		text.setText(nullName);
    }

	public void setSave(boolean save) {
    	this.save = save;
    }

	public void setFilterExtensions(String[] filterExtensions) {
    	this.filterExtensions = filterExtensions;
    }

	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
		text.setText(file != null ? file.getName() : nullName);
	}
	
	class Listener extends SelectionAdapter {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (workspaceOnly) {
				ElementTreeSelectionDialog dialog =
					new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
			    if(dialog.open() == IDialogConstants.OK_ID) {
			    	Object firstResult = dialog.getFirstResult();
			    	file = ((IFile) ((IAdaptable) firstResult).getAdapter(IFile.class)).getLocation().toFile();
			    	text.setText(file.getName());
			    }
			} else {
				int style = (save ? SWT.SAVE : SWT.OPEN);
			    FileDialog dialog = new FileDialog(shell, style);
			    if (file != null) {
			    	dialog.setFilterPath(file.getParentFile().getAbsolutePath());
			    	dialog.setFileName(file.getName());
			    }
			    if (filterExtensions != null)
			    	dialog.setFilterExtensions(filterExtensions);
			    String filename = dialog.open();
			    if(filename != null) {
			    	file = new Path(filename).toFile();
			    	text.setText(filename);
			    }
			}
		}
	}

    public void addModifyListener(ModifyListener listener) {
	    text.addModifyListener(listener);
    }
}
