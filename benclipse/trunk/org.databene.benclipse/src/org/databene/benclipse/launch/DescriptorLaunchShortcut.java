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

package org.databene.benclipse.launch;

import java.util.ArrayList;
import java.util.List;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.IBenclipseConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * LauchShortcut for starting Benerator descriptor files.<br/>
 * <br/>
 * Created at 05.02.2009 15:38:20
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class DescriptorLaunchShortcut implements ILaunchShortcut {
	
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile) input.getAdapter(IFile.class);
		launch(file, mode);
	}

	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			Object firstSelectedElement = ((IStructuredSelection)selection).getFirstElement();
			if (firstSelectedElement instanceof IAdaptable)
			launch((IFile) ((IAdaptable) firstSelectedElement).getAdapter(IFile.class), mode);
		}
	}

	protected void launch(IFile file, String mode) {
		ILaunchConfiguration config = getOrCreateLaunchConfiguration(file);
		if (config != null)
			DebugUITools.launch(config, mode);
	}
	
	protected ILaunchConfiguration getOrCreateLaunchConfiguration(IFile descriptorFile) {
		List<ILaunchConfiguration> candidateConfigs = new ArrayList<ILaunchConfiguration>();
		try {
			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType descriptorLaunchConfigType = launchManager.getLaunchConfigurationType(
					IBenclipseConstants.DESCRIPTOR_LAUNCH_CONFIG_TYPE_ID);
			ILaunchConfiguration[] configs = launchManager.getLaunchConfigurations(descriptorLaunchConfigType);
			String projectName = descriptorFile.getProject().getName();
			String descriptorPath = descriptorFile.getProjectRelativePath().toString();
			for (int i = 0; i < configs.length; i++) {
				ILaunchConfiguration config = configs[i];
				String configProject = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
				String configDescriptor = config.getAttribute(IBenclipseConstants.ATTR_FILENAME, "");
				if (configProject.equals(projectName) && configDescriptor.equals(descriptorPath))
					candidateConfigs.add(config);
			}
		} catch (CoreException e) {
			reportError(e);
		}
		
		int candidateCount = candidateConfigs.size();
		if (candidateCount < 1)
			return createConfiguration(descriptorFile);
		else if (candidateCount == 1)
			return candidateConfigs.get(0);
		else
			return chooseConfiguration(candidateConfigs); // let the user choose a configuration, on 'cancel' we get 'null'
	}

    private ILaunchConfiguration createConfiguration(IFile descriptorFile) {
	    try {
			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType configType = launchManager
			        .getLaunchConfigurationType(IBenclipseConstants.DESCRIPTOR_LAUNCH_CONFIG_TYPE_ID);

			String configName = launchManager.generateUniqueLaunchConfigurationNameFrom(defaultLaunchConfigName(descriptorFile));
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, configName);

            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, descriptorFile.getProject().getName());
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, IBenclipseConstants.BENERATOR_MAIN_CLASS);
            LaunchUtil.setDescriptorFile(descriptorFile, wc);
			
			wc.setAttribute(IBenclipseConstants.ATTR_VALIDATE, true);
			wc.setAttribute(IBenclipseConstants.ATTR_OPTIMIZE, false);

			wc.setAttribute(RefreshTab.ATTR_REFRESH_SCOPE, "${project}");
			wc.setAttribute(RefreshTab.ATTR_REFRESH_RECURSIVE, true);

			wc.setAttribute(IBenclipseConstants.ATTR_VALIDATE, true);
			wc.setAttribute(IBenclipseConstants.ATTR_OPTIMIZE, false);
			wc.setAttribute(IBenclipseConstants.ATTR_OPEN_GEN_FILES, true);
			wc.setAttribute(IBenclipseConstants.ATTR_PROPERTIES, new ArrayList<String>());
			
			return wc.doSave();
		} catch (CoreException e) {
			reportError(e);
		}
		return null;
    }

	private String defaultLaunchConfigName(IFile descriptorFile) {
	    return descriptorFile.getProject().getName() + " data generation";
    }
	
	protected ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList) {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle("Select a benerator configuration");  
		dialog.setMessage("Select a benerator configuration");
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK)
			return (ILaunchConfiguration) dialog.getFirstResult();
		return null;		
	}

	protected void reportError(CoreException exception) {
		MessageDialog.openError(getShell(), "Benerator Error", exception.getStatus().getMessage());  
	}

	private Shell getShell() {
	    return BenclipsePlugin.getActiveWorkbenchShell();
    }

}
