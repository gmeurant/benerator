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
import java.util.Observer;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Common parent class for Benerator wizard pages which 
 * implements the {@link Observer} interface and adds 'next page' handling.<br/>
 * <br/>
 * Created at 23.02.2009 07:47:46
 * @since 0.5.9
 * @author Volker Bergmann
 */

public abstract class BeneratorWizardPage extends WizardPage implements Observer {
	
	private IWizardPage nextPage;
	private boolean nextPageSet;
	private Observable observable;
	
	public BeneratorWizardPage(String name) {
		super(name);
		nextPage = null;
		nextPageSet = false;
		observable = new ChangedObservable();
	}
	
	// control interface -----------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	public void init(IStructuredSelection selection, IWorkbenchPart activePart) {
	}

	@Override
    protected void setControl(Control newControl) {
		// TODO v0.6 PlatformUI.getWorkbench().getHelpSystem().setHelp(newControl, "todo");
		super.setControl(newControl);
	}
	
    @Override
    public IWizardPage getNextPage() {
    	return (nextPageSet ? nextPage : super.getNextPage());
    }

	public void setNextPage(WizardPage nextPage) {
		this.nextPageSet = true;
    	this.nextPage = nextPage;
    }

	// event handling --------------------------------------------------------------------------------------------------
	
    public void addObserver(Observer arg0) {
	    observable.addObserver(arg0);
    }

	public void deleteObserver(Observer arg0) {
	    observable.deleteObserver(arg0);
    }

	public void update(Observable o, Object arg) {
    	observable.notifyObservers(arg);
    }

	static class ChangedObservable extends Observable {
		
		@Override
		public void notifyObservers() {
		    setChanged();
		    super.notifyObservers();
		}

		@Override
		public void notifyObservers(Object arg) {
		    setChanged();
		    super.notifyObservers(arg);
		}
	}
}
