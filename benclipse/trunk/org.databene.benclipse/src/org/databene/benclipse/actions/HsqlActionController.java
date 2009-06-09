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

package org.databene.benclipse.actions;

import org.databene.benclipse.internal.HsqlDbController;
import org.eclipse.jface.action.IAction;

/**
 * Controls enablement of the HSQL related actions.<br/>
 * <br/>
 * Created at 11.02.2009 11:33:31
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class HsqlActionController {

	private IAction startHsqldbAction;
	private IAction stopHsqldbAction;
	private IAction startHsqldbManagerAction;
	
	private boolean running = !HsqlDbController.canBeStarted();
	
	public void registerHsqlStopAction(IAction action) {
		this.stopHsqldbAction = action;
		updateHsqlActions();
	}
	
	public void registerHsqlStartAction(IAction action) {
		this.startHsqldbAction = action;
		updateHsqlActions();
	}
	
    public void registerHsqlManagerAction(IAction action) {
	    this.startHsqldbManagerAction = action;
    }
	
	public void setHsqlRunning(boolean running) {
		this.running = running;
		updateHsqlActions();
	}

	public boolean isHsqlRunning() {
		return this.running;
	}

	private void updateHsqlActions() {
	    if (startHsqldbAction != null)
			startHsqldbAction.setEnabled(!running);
		if (stopHsqldbAction != null)
			stopHsqldbAction.setEnabled(running);
		if (startHsqldbManagerAction != null)
			startHsqldbManagerAction.setEnabled(running);
    }

}
