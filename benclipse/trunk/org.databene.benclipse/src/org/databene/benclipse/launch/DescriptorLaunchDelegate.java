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

import org.databene.benclipse.core.BenclipseLogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

/**
 * Launch Delegate for running Benerator descriptor files.<br/>
 * <br/>
 * Created at 05.02.2009 12:29:49
 * 
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class DescriptorLaunchDelegate extends JavaLaunchDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
	        throws CoreException {
		super.launch(configuration, mode, launch, monitor);
		// TODO v0.6 open all generated files if this is configured
	}

	@Override
	public IVMRunner getVMRunner(final ILaunchConfiguration configuration, String mode) throws CoreException {
		final IVMRunner runner = super.getVMRunner(configuration, mode);

		return new IVMRunner() {
			public void run(VMRunnerConfiguration runnerConfiguration, ILaunch launch, IProgressMonitor monitor)
			        throws CoreException {
				runner.run(runnerConfiguration, launch, monitor);

				IProcess[] processes = launch.getProcesses();
				if (processes != null && processes.length > 0) {
					BackgroundResourceRefresher refresher = new BackgroundResourceRefresher(configuration, processes[0]);
					refresher.startBackgroundRefresh();
				}
			}
		};
	}

	public static class BackgroundResourceRefresher implements IDebugEventSetListener {
		
		final ILaunchConfiguration configuration;
		final IProcess process;

		public BackgroundResourceRefresher(ILaunchConfiguration configuration, IProcess process) {
			this.configuration = configuration;
			this.process = process;
		}

		public void startBackgroundRefresh() {
			synchronized (process) {
				if (process.isTerminated()) {
					refresh();
				} else {
					DebugPlugin.getDefault().addDebugEventListener(this);
				}
			}
		}

		public void handleDebugEvents(DebugEvent[] events) {
			for (int i = 0; i < events.length; i++) {
				DebugEvent event = events[i];
				if (event.getSource() == process && event.getKind() == DebugEvent.TERMINATE) {
					DebugPlugin.getDefault().removeDebugEventListener(this);
					refresh();
					break;
				}
			}
		}

		protected void refresh() {
			Job job = new Job("Refreshing resources...") {
				@Override
                public IStatus run(IProgressMonitor monitor) {
					try {
						RefreshTab.refreshResources(configuration, monitor);
						return Status.OK_STATUS;
					} catch (CoreException e) {
						BenclipseLogger.log(e);
						return e.getStatus();
					}
				}
			};
			job.schedule();
		}
	}

}
