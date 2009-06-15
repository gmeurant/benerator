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

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.EclipseProgressMonitor;
import org.databene.benerator.main.DBSnapshotTool;
import org.databene.commons.StringUtil;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Benclipse utility that creates database snapshots in DbUnit XML file format.<br/>
 * <br/>
 * Created at 09.03.2009 17:37:04
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BenclipseDbUnitSnapshotCreator {

	public static boolean createSnapshot(final JDBCConnectionInfo c, final File file) {
	    final String createSnapshotMessage = Messages.getString("wizard.dbunit.job.creating.snapshot");
	    final ClassLoader classLoaderToUse = Thread.currentThread().getContextClassLoader();
		final Job job = new WorkspaceJob(createSnapshotMessage) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				try {
					Thread.currentThread().setContextClassLoader(classLoaderToUse);
					String schema = StringUtil.emptyToNull(c.getSchema().trim());
					EclipseProgressMonitor eclipseMonitor = new EclipseProgressMonitor(monitor, createSnapshotMessage, 1);
					DBSnapshotTool.export(c.getUrl(), c.getDriverClass(), schema, c.getUser(), c.getPassword(), 
							file.getAbsolutePath(), eclipseMonitor);
					return Status.OK_STATUS;
				} catch (Exception e) {
					return new Status(Status.ERROR, BenclipsePlugin.PLUGIN_ID, e.getMessage(), e);
				} finally {
					monitor.done();
					Thread.currentThread().setContextClassLoader(contextClassLoader);
				}
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				final IStatus result = event.getResult();
				if (!result.isOK()) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(
									BenclipsePlugin.getActiveWorkbenchShell(), 
									Messages.getString("wizard.dbunit.job.failed"), 
							        result.getMessage());
						}
					});
				}
			}
		});
		job.schedule();
		return true;
    }
	

}
