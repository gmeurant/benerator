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

import java.util.Observable;

import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.SWTUtil;
import org.databene.commons.StringUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

/**
 * Wizard page for specifying project location and working set.<br/>
 * <br/>
 * Created at 06.02.2009 07:45:52
 * 
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class ProjectLocationPage extends BeneratorWizardPage {
	
	private ProjectNameGroup projectNameGroup;
	private JREGroup jreGroup;
	
	// constructor -----------------------------------------------------------------------------------------------------
	
	public ProjectLocationPage() {
		super("ProjectLocationPage");
		setPageComplete(false);
		setTitle(Messages.getString("wizard.project.location.title"));
		setDescription(Messages.getString("wizard.project.location.description"));
		setMessage(Messages.getString("wizard.project.location.message.name"));
	}
	
	// interface -------------------------------------------------------------------------------------------------------

	public void createControl(Composite parent) {
		Composite control = SWTUtil.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL, true);
		projectNameGroup = new ProjectNameGroup();
		projectNameGroup.createControl(control);
		projectNameGroup.addObserver(this);
		jreGroup = new JREGroup();
		jreGroup.createControl(control);
		jreGroup.addObserver(this);
//		createLocationGroup(control);
//		createWorkingSetGroup(control);
		setControl(control);
	}

	@Override
	public boolean canFlipToNextPage() {
		return (jreGroup.getSelectedJRE() != null && !StringUtil.isEmpty(projectNameGroup.getProjectName()));
	}

    @Override
    public void update(Observable o, Object arg) {
        IVMInstall selectedJRE = jreGroup.getSelectedJRE();
        if (selectedJRE == null) {
            setErrorMessage(Messages.getString("wizard.project.jre.requires6"));
            setPageComplete(false);
        	super.update(o, arg);
            return;
        }
        String name = projectNameGroup.getProjectName();
        if (name.trim().length() == 0) {
            setErrorMessage(null);
            setMessage(Messages.getString("wizard.project.location.message.name"));
            setPageComplete(false);
        	super.update(o, arg);
            return;
        }
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IStatus nameStatus = workspace.validateName(name, IResource.PROJECT);
        if(!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            setPageComplete(false);
        	super.update(o, arg);
            return;
        }
        IProject handle = workspace.getRoot().getProject(name);
        if(handle.exists() && !projectNameGroup.isOverwrite()) { // TODO v0.6 control enablement of overwrite
            setErrorMessage(Messages.getString("wizard.project.location.message.exists"));
            setPageComplete(false);
        	super.update(o, arg);
            return;
        }
        setPageComplete(true);
        setErrorMessage(null);
        setMessage(null);
    	super.update(o, arg);
    }
    
    // properties ------------------------------------------------------------------------------------------------------
    
	public ProjectNameGroup getProjectNameGroup() {
    	return projectNameGroup;
    }

	public JREGroup getJREGroup() {
    	return jreGroup;
    }

}
