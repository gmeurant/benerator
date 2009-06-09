package org.databene.benclipse.actions;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.launch.DescriptorLaunchShortcut;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Action Delegate that starts Benerator.<br/>
 * <br/>
 * Created at 12.02.2009 19:35:13
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class RunBeneratorAction implements IWorkbenchWindowActionDelegate {
	
	IWorkbenchWindow window;
	
	public RunBeneratorAction() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorPart editor = page.getActiveEditor();
					new DescriptorLaunchShortcut().launch(editor, "run");
				}
			}
		});
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		BenclipsePlugin.getDefault().getEditorWatchdog().registerRunBeneratorAction(action);
	}
	
	public void dispose() {
	}

}