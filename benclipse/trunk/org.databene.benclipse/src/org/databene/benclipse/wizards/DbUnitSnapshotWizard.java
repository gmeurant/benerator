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
import java.util.Observer;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.Messages;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Wizard for creating a benerator project.<br/>
 * <br/>
 * Created at 06.02.2009 08:53:13
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class DbUnitSnapshotWizard extends Wizard implements INewWizard, Observer {
	
	private static final String BANNER_ICON_SNAPSHOT = "icons/wizban/simple.gif";

	private static final ImageDescriptor DEFAULT_PAGE_IMAGE = BenclipsePlugin.getImageDescriptor(BANNER_ICON_SNAPSHOT);

	protected DbUnitSnapshotPage dbUnitSnapshotPage;

	public DbUnitSnapshotWizard() {
		setWindowTitle(Messages.getString("wizard.dbunit.title"));
		setDefaultPageImageDescriptor(DEFAULT_PAGE_IMAGE);
		setNeedsProgressMonitor(true);
	}
	
	// Framework methods -----------------------------------------------------------------------------------------------

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
    public void addPages() {
		dbUnitSnapshotPage = new DbUnitSnapshotPage();
		addPage(dbUnitSnapshotPage);
		dbUnitSnapshotPage.addObserver(this);
	}

	@Override
	public void addPage(IWizardPage page) {
	    super.addPage(page);
	    if (page instanceof BeneratorWizardPage)
	    	((BeneratorWizardPage) page).addObserver(this);
	}
	
	@Override
	public boolean canFinish() {
		 return dbUnitSnapshotPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		final JDBCConnectionInfo c = getDatabaseGroup().getConnectionInfo();
		final File file = dbUnitSnapshotPage.getFile();
		return BenclipseDbUnitSnapshotCreator.createSnapshot(c, file);
	}

	public void update(Observable observable, Object obj) {
	    updateControls();
    }
	
	void updateControls() {
		getContainer().updateButtons();
    }

	DatabaseConnectionGroup getDatabaseGroup() {
	    return dbUnitSnapshotPage.getDatabaseGroup();
    }
	
}