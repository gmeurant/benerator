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

package org.databene.benclipse.internal;

import org.databene.benclipse.core.BenclipseLogger;
import org.databene.commons.StringUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Provides several common Eclipse related utility methods.<br/>
 * <br/>
 * Created at 02.02.2009 12:11:42
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class EclipseUtil {

    public static void openInDefaultEditor(final IFile resource) {
		openInDefaultEditor(resource, getActiveWorkbenchWindow());
	}

    public static void openInDefaultEditor(final IFile resource, final IWorkbenchWindow workbenchWindow) {
		Display display = workbenchWindow.getShell().getDisplay();
		if (display != null) {
			display.asyncExec(new Runnable() {
				public void run() {
					try {
						IDE.openEditor(workbenchWindow.getActivePage(), resource, true);
					} catch (PartInitException e) {
						BenclipseLogger.log(e.getStatus());
					}
				}
			});
		}
	}

    /*
	public static void openInDefaultEditor(IFile file) throws PartInitException {
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		IEditorInput editorInput = new FileEditorInput(file);
		getActivePage().openEditor(editorInput, desc.getId());
	}
*/
    /*
    public static IFile getFileInEditor() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null) {
					IWorkbenchPage page = window.getActivePage();
					if (page != null) {
						IEditorPart editor = page.getActiveEditor();
						new DescriptorLaunchShortcut().launch(editor, "run");
						if (editor != null) {
							IEditorInput input = editor.getEditorInput();
							if (input instanceof IFileEditorInput) {
								IFileEditorInput fileInput = (IFileEditorInput) input;
								IFile file = fileInput.getFile();
							}
						}
					}
				}
			}
		});
    }
    */
    public static Shell getActiveWorkbenchShell() {
	    IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
	    return (activeWorkbenchWindow != null ? activeWorkbenchWindow.getShell() : null);
    }

	public static IWorkbenchPage getActivePage() {
	    IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
	    return (activeWorkbenchWindow != null ? activeWorkbenchWindow.getActivePage() : null);
    }

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
	    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }
	
	public static ImageDescriptor getSharedImageDescriptor(String key) {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(key);
	}

	public static Image getSharedImage(String imageKey) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

	public static FontMetrics getFontMetrics() {
	    Font dialogFont = JFaceResources.getDialogFont();
        GC gc = new GC(dialogFont.getDevice());
        gc.setFont(dialogFont);
        FontMetrics fontmetrics = gc.getFontMetrics();
        gc.dispose();
        return fontmetrics;
    }

	public static int convertHeightInCharsToPixels(int chars) {
        return Dialog.convertHeightInCharsToPixels(getFontMetrics(), chars);
    }

	public static int convertWidthInCharsToPixels(int chars) {
        return Dialog.convertWidthInCharsToPixels(getFontMetrics(), chars);
    }

	public static IStructuredSelection evaluateCurrentSelection() {
		IWorkbenchWindow window = EclipseUtil.getActiveWorkbenchWindow();
		if (window != null) {
			ISelection selection = window.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection)
				return (IStructuredSelection) selection;
		}
		return StructuredSelection.EMPTY;
	}

	public static IFile ifileFromAbsolutePath(String absolutePath) {
		if (StringUtil.isEmpty(absolutePath))
			return null;
		IPath path = Path.fromOSString(absolutePath);
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
	}
}
