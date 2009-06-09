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

import org.databene.benerator.util.GeneratorUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * Enables and disables 'Run/Debug benerator' actions depending on navigator and editor selections.<br/>
 * <br/>
 * Created at 13.02.2009 07:26:39
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class EditorWatchdog implements IPartListener2 {
	
	private IAction runBeneratorAction;
	private IAction debugBeneratorAction;

	// IPartListener2 interface implementation ------------------------------------------------------------------------- 
	
	public void partActivated(IWorkbenchPartReference ref) {
		processActivatedPart(ref);
	}

	public void partBroughtToTop(IWorkbenchPartReference ref) {
		processActivatedPart(ref);
	}

	public void partOpened(IWorkbenchPartReference ref) {
		processActivatedPart(ref);
	}

	public void partVisible(IWorkbenchPartReference ref) {
		processActivatedPart(ref);
	}

	public void partHidden(IWorkbenchPartReference ref) {
		processDeactivatedPart(ref);
	}

	public void partClosed(IWorkbenchPartReference ref) {
		processDeactivatedPart(ref);
	}

	public void partDeactivated(IWorkbenchPartReference ref) {
		processDeactivatedPart(ref);
	}

	public void partInputChanged(IWorkbenchPartReference ref) {
		processDeactivatedPart(ref);
	}
	
	// binding methods -------------------------------------------------------------------------------------------------
	
	public void registerRunBeneratorAction(IAction action) {
		this.runBeneratorAction = action;
	}

	public void registerDebugBeneratorAction(IAction action) {
		this.debugBeneratorAction = action;
	}

	// private helpers -------------------------------------------------------------------------------------------------

	private void processActivatedPart(IWorkbenchPartReference ref) {
		IWorkbenchPart part = ref.getPart(true);
		if (part instanceof IEditorPart) {
			IEditorInput input = ((IEditorPart) part).getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				if (GeneratorUtil.isBeneratorFile(file.getName()))
					setRunBeneratorPossible(true);
			}	
		}
	}

    private void processDeactivatedPart(@SuppressWarnings("unused") IWorkbenchPartReference ref) {
		setRunBeneratorPossible(false);
    }

    private void setRunBeneratorPossible(boolean possible) {
		if (runBeneratorAction != null)
			runBeneratorAction.setEnabled(possible);
		if (debugBeneratorAction != null)
			debugBeneratorAction.setEnabled(possible);
    }
    
}
