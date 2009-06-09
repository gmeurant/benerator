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

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.internal.HsqlDbController;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action Delegate that launches the built-in HSQLDB in a separate Java process.<br/>
 * <br/>
 * Created at 08.02.2009 08:02:40
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class StartHSQLAction implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow window;
	
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

    public void run(IAction action) {
		try {
		    HsqlDbController.startHSQL(false);
		} catch (CoreException e) {
			MessageDialog.openInformation(window.getShell(), "HSQL Error", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(window.getShell(), "HSQL Error", e.toString());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		BenclipsePlugin.getDefault().getHsqldbController().registerHsqlStartAction(action);
	}

	public void dispose() {
	}

}