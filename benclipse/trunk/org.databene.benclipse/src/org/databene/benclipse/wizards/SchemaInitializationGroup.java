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

import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.FileField;
import org.databene.benclipse.swt.SWTUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Groups dialog elements for configuring database initialization.<br/>
 * <br/>
 * Created at 05.03.2009 08:59:55
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class SchemaInitializationGroup {
	
	// attributes ------------------------------------------------------------------------------------------------------

	FileField createScriptField;
	FileField dropScriptField;
	Button dbUnitSnapshot;

	Button createButton;

	Composite group;
	
	// interface -------------------------------------------------------------------------------------------------------
	
	public void createControl(Composite parent) {
		group = SWTUtil.createGroup(parent, Messages.getString("wizard.project.initdb.title"), 3, 1, GridData.FILL_HORIZONTAL);
        group.setFont(parent.getFont());
        createScriptField = new FileField(group, Messages.getString("wizard.project.initdb.create"), "...", 1);
        createScriptField.setWorkspaceOnly(false);
	    dropScriptField = new FileField(group, Messages.getString("wizard.project.initdb.drop"), "...", 1);
	    dropScriptField.setWorkspaceOnly(false);
	    dbUnitSnapshot = SWTUtil.createCheckbox(group, Messages.getString("wizard.project.initdb.dbunit"), null, "", 3);
    }

    public void setVisible(boolean visible) {
	    group.setVisible(visible);
    }

    public SchemaInitializationInfo getInfo() {
		return new SchemaInitializationInfo(
				createScriptField.getFile(), dropScriptField.getFile(), dbUnitSnapshot.getSelection());
    }
    
}
