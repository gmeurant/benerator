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

package org.databene.benclipse.actions;

import org.databene.benclipse.internal.EclipseUtil;
import org.databene.benclipse.wizards.DbUnitSnapshotWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Opens a {@link DbUnitSnapshotWizard} in a dialog window for creating database snapshot in DbUnit file format.<br/>
 * <br/>
 * Created at 07.03.2009 18:13:26
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class DbUnitSnapshotAction implements IWorkbenchWindowActionDelegate {
	
	private Shell shell;
	private IStructuredSelection selection;

	public void init(IWorkbenchWindow window) {
		this.shell = (window.getShell() != null ? window.getShell() : EclipseUtil.getActiveWorkbenchShell());
	}

	public void run(IAction action) {
		INewWizard wizard = new DbUnitSnapshotWizard();
		wizard.init(PlatformUI.getWorkbench(), selection);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.setMinimumPageSize(
				EclipseUtil.convertWidthInCharsToPixels(70), 
				EclipseUtil.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();
	}

	public void dispose() {
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (selection instanceof IStructuredSelection ? ((IStructuredSelection) selection)
		        : StructuredSelection.EMPTY);
	}

}