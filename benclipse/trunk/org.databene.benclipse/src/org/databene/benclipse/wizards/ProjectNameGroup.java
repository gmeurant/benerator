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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Widget group for defining project name.<br/>
 * <br/>
 * Created at 22.02.2009 10:42:59
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class ProjectNameGroup extends Observable implements ModifyListener, SelectionListener {
	
	private Text projectNameText;
	//private Button overwriteCheck;
	private Button simpleCheck;

	public void createControl(Composite parent) {
	    Composite control = new Composite(parent, SWT.NONE);
        SWTUtil.initGridLayout(control, 2, 1, GridData.FILL_HORIZONTAL, false);
        control.setFont(parent.getFont());
        
        // create 'Project name' input field
	    projectNameText = SWTUtil.createLabeledText(control, Messages.getString("wizard.project.location.projectName"), 1);
	    projectNameText.addModifyListener(this);
	    
	    new Label(control, SWT.NONE);
		simpleCheck = SWTUtil.createCheckbox(control, Messages.getString("wizard.project.beginner"), null, "", 1);
		simpleCheck.setSelection(true);
		simpleCheck.addSelectionListener(this);

		// TODO v0.6 create 'Overwrite existing project' checkbox
	    //new Label(control, SWT.NONE);
	    //String overwriteLabel = Messages.getString("wizard.project.location.existing");
		//overwriteCheck = SWTUtil.createCheckbox(control, overwriteLabel, null, overwriteLabel, 1);
		//overwriteCheck.addSelectionListener(this);
    }

	public String getProjectName() {
	    return projectNameText.getText();
    }

    public boolean isOverwrite() {
    	return false; // TODO v0.6 overwriteCheck.getSelection();
    }

    public boolean isSimple() {
	    return simpleCheck.getSelection();
    }
    
    public void modifyText(ModifyEvent modifyevent) {
    	updateObservers();
    }

    public void widgetDefaultSelected(SelectionEvent selectionevent) {
    	updateObservers();
    }

    public void widgetSelected(SelectionEvent selectionevent) {
    	updateObservers();
    }
    
    private void updateObservers() {
    	setChanged();
    	notifyObservers(this);
    }
}
