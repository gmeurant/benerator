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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.internal.VMUtil;
import org.databene.benclipse.swt.SWTUtil;
import org.databene.commons.ConfigurationError;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Benerator wizard page for JRE selection.<br/>
 * <br/>
 * Created at 22.02.2009 11:13:00
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class JREGroup extends Observable implements IBenclipseConstants {
	
	// attributes ------------------------------------------------------------------------------------------------------

	private Button defaultJRERadio;
	private Button specificJRERadio;
	private ComboViewer jreCombo;
	Group group;
	
	// interface -------------------------------------------------------------------------------------------------------
	
    public void createControl(Composite parent) {

        String title = Messages.getString("wizard.project.jre.title");
		group = SWTUtil.createGroup(parent, title, 2, 1, GridData.FILL_HORIZONTAL);
        
        String defaultJreFormat = Messages.getString("wizard.project.jre.default");
        String defaultJreLabel = MessageFormat.format(defaultJreFormat, JavaRuntime.getDefaultVMInstall().getName());
		defaultJRERadio = SWTUtil.createRadioButton(group, defaultJreLabel, null, "", 1);
        defaultJRERadio.setSelection(true);

        SWTUtil.createLink(group, Messages.getString("wizard.project.jre.configure"), new ConfigLinkListener());

        specificJRERadio = SWTUtil.createRadioButton(group, Messages.getString("wizard.project.jre.specific"), null, "", 1);
        createJRECombo(group);
        
        // check options
        if (!VMUtil.versionAtLeast(MIN_JVM_REQUIRED, JavaRuntime.getDefaultVMInstall())) {
        	defaultJRERadio.setSelection(false);
        	defaultJRERadio.setEnabled(false);
        	specificJRERadio.setSelection(true);
        }
        
        group.setEnabled(false);
    }

    public void setEnabled(boolean enabled) {
    	group.setEnabled(enabled);
    }

    public IVMInstall getSelectedJRE() {
    	if (defaultJRERadio.getSelection())
    		return JavaRuntime.getDefaultVMInstall();
    	else {
    		IStructuredSelection selection = (IStructuredSelection) jreCombo.getSelection();
    		if (selection.isEmpty())
    			return null;
    		else
    			return (IVMInstall) selection.getFirstElement();
    	}
    }
    
    // non-public helpers ----------------------------------------------------------------------------------------------
    
	private void createJRECombo(Composite parent) {
	    jreCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY);
	    Combo combo = jreCombo.getCombo();
	    combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    jreCombo.setLabelProvider(new VMInstallLabelProvider());
	    jreCombo.setContentProvider(new DefaultContentProvider());
	    List<IVMInstall> installs = updateJRECombo();
	    jreCombo.setSelection(new StructuredSelection(installs.get(0)));
    }

    List<IVMInstall> updateJRECombo() {
	    List<IVMInstall> installs = getVMInstalls();
		jreCombo.setInput(installs);
		return installs;
    }

	private List<IVMInstall> getVMInstalls() {
	    List<IVMInstall> vmInstalls = VMUtil.findVmInstallsVersionOrNewer(MIN_JVM_REQUIRED);
        if (vmInstalls.size() == 0)
        	throw new ConfigurationError("No appropriate JVM installed"); // TODO v0.6 handle the case of no installed JVM 1.6
	    return vmInstalls;
    }

    // helper classes --------------------------------------------------------------------------------------------------
    
	class ConfigLinkListener implements SelectionListener {
		
        public void widgetSelected(SelectionEvent e) {
            widgetDefaultSelected(e);
        }

        public void widgetDefaultSelected(SelectionEvent e) {
            String jreID = "org.eclipse.jdt.debug.ui.preferences.VMPreferencePage";
            Map<String, Boolean> data = new HashMap<String, Boolean>();
            data.put("PropertyAndPreferencePage.nolink", Boolean.TRUE);
            PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(group.getShell(), jreID, new String[] { jreID }, data);
			dialog.open();
			updateJRECombo();
        }
    }
	
	class VMInstallLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			return ((IVMInstall) element).getName();
		}
	}
}
