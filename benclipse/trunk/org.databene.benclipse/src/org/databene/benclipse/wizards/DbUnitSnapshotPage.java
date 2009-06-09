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

import java.io.File;
import java.util.Observable;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.FileField;
import org.databene.benclipse.swt.SWTUtil;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Wizard Page for configuring a DbUnit snapshot.<br/>
 * <br/>
 * Created at 07.03.2009 17:46:05
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class DbUnitSnapshotPage extends BeneratorWizardPage {
	
	private DatabaseConnectionGroup databaseGroup;
	private FileField fileField;
	
	public DbUnitSnapshotPage() {
		super("DbUnitSnapshotPage");
		setPageComplete(false);
		setTitle(Messages.getString("wizard.dbunit.title"));
		setDescription(Messages.getString("wizard.dbunit.description"));
	}
	
	// interface -------------------------------------------------------------------------------------------------------

	public File getFile() {
	    return fileField.getFile();
    }
	
	public DatabaseConnectionGroup getDatabaseGroup() {
    	return databaseGroup;
    }
	
	public void createControl(Composite parent) {
		Composite control = SWTUtil.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL, true);
		
		Composite fileGroup = SWTUtil.createComposite(control, 3, 1, GridData.FILL_HORIZONTAL);
		fileField = new FileField(fileGroup, Messages.getString("wizard.dbunit.file"), "...", 1);
		fileField.setSave(true);
		fileField.setWorkspaceOnly(false);
		fileField.setFilterExtensions(new String[] { ".dbunit.xml" });
		fileField.setFile(getRecentFile());
		fileField.addObserver(this);
		
		databaseGroup = new DatabaseConnectionGroup();
		databaseGroup.createControl(control);
		databaseGroup.addObserver(this);
		
		setControl(control);
	}

	@Override
	public boolean isPageComplete() {
	    return databaseGroup.isGroupComplete() && getFile() != null;
	}

    @Override
    public void update(Observable o, Object arg) {
    	setRecentFile(fileField.getFile());
    	String errorMessage = databaseGroup.getErrorMessage();
    	if (errorMessage != null) {
    		setPageComplete(false);
    		setErrorMessage(errorMessage);
    	} else {
    		setPageComplete(true);
    		setErrorMessage(null);
    		if (databaseGroup.isConnectionValid())
    			setMessage(Messages.getString("wizard.project.db.message.available"));
    	}
    	super.update(o, arg);
    }
    
    // private helper methods ------------------------------------------------------------------------------------------

    private File getRecentFile() {
	    String snapshotDir = preferenceStore().getString(IBenclipseConstants.PREF_SNAPSHOT_DIR);
	    return new File(snapshotDir);
    }

    private void setRecentFile(File recentFile) {
	    preferenceStore().setValue(IBenclipseConstants.PREF_SNAPSHOT_DIR, recentFile.getAbsolutePath());
    }

	private IPreferenceStore preferenceStore() {
	    return BenclipsePlugin.getDefault().getPreferenceStore();
    }

}
