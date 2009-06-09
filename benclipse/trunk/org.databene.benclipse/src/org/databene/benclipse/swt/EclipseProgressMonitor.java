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

package org.databene.benclipse.swt;

import org.databene.commons.ui.ProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * {@link ProgressMonitor} implementation that proxies an Eclipse {@link IProgressMonitor}.<br/>
 * <br/>
 * Created at 07.03.2009 21:20:53
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class EclipseProgressMonitor implements ProgressMonitor {
	
	private IProgressMonitor monitor;
	private int steps;
	
    public EclipseProgressMonitor(IProgressMonitor monitor, String taskName, int steps) {
	    this.monitor = monitor;
	    this.steps = steps;
	    monitor.beginTask(taskName, steps);
    }

	public void advance() {
	    monitor.worked(1);
    }

    public int getMaximum() {
	    return steps;
    }

    public boolean isCanceled() {
	    return false;
    }

    public void setMaximum(int steps) {
	    this.steps = steps;
    }

    public void setNote(String note) {
	    monitor.subTask(note);
    }

    public void setProgress(int steps) {
	    monitor.worked(steps);
    }

}
