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

package org.databene.benclipse.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.benclipse.core.Messages;

/**
 * Benerator preference page.<br/>
 * <br/>
 * Created at 10.02.2009 14:21:13
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BeneratorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BeneratorPreferencePage() {
		super(GRID);
		setPreferenceStore(BenclipsePlugin.getDefault().getPreferenceStore());
		setDescription(Messages.getString("preferences.title"));
	}

	@Override
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addField(new BooleanFieldEditor(
				IBenclipseConstants.PREF_MAIN_MENU, Messages.getString("preferences.display.menu"), parent));
		addField(new BooleanFieldEditor(
				IBenclipseConstants.PREF_HSQL, Messages.getString("preferences.display.hsql"), parent));
	}

	public void init(IWorkbench workbench) {
	}
	
	@Override
	public boolean performOk() {
	    boolean result = super.performOk();
		BenclipsePlugin.getDefault().updateControls();
		return result;
	}
}