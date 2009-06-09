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
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to edit a key-value pair.<br/>
 * <br/>
 * Created at 02.03.2009 10:25:32
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BeneratorPropertyDialog extends Dialog {

	private final String title;

	private final String[] initialValues;

	private final boolean variables;

	private Text nameText;

	private Text valueText;

	private String name;

	private String value;

	private VerifyListener verifyListener;

	public BeneratorPropertyDialog(Shell shell, String title, String[] initialValues, boolean variables) {
		super(shell);
		this.title = title;
		this.initialValues = initialValues;
		this.variables = variables;
	}

	public BeneratorPropertyDialog(Shell shell, String title, String[] initialValues, boolean variables,
	        VerifyListener nameVerifyListener) {
		this(shell, title, initialValues, variables);
		this.verifyListener = nameVerifyListener;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 7;
		gridLayout.marginWidth = 12;
		comp.setLayout(gridLayout);

		Label nameLabel = new Label(comp, SWT.NONE);
		nameLabel.setText(Messages.getString("dialog.prop.name"));
		nameLabel.setFont(comp.getFont());

		nameText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		nameText.setLayoutData(gd);
		nameText.setFont(comp.getFont());
		if (initialValues.length >= 1) {
			nameText.setText(initialValues[0]);
		}
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		Label valueLabel = new Label(comp, SWT.NONE);
		valueLabel.setText(Messages.getString("dialog.prop.value"));
		valueLabel.setFont(comp.getFont());

		valueText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		valueText.setLayoutData(gd);
		valueText.setFont(comp.getFont());
		if (initialValues.length >= 2) {
			valueText.setText(initialValues[1]);
		}
		valueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		if (variables) {
			Button variablesButton = new Button(comp, SWT.PUSH);
			variablesButton.setText(Messages.getString("dialog.prop.browse"));
			gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
			gd.horizontalSpan = 2;
			int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
			gd.widthHint = Math.max(widthHint, variablesButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
			variablesButton.setLayoutData(gd);
			variablesButton.setFont(comp.getFont());

			variablesButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent se) {
					getVariable();
				}
			});
		}

		return comp;
	}

	protected void getVariable() {
		StringVariableSelectionDialog variablesDialog = new StringVariableSelectionDialog(getShell());
		int returnCode = variablesDialog.open();
		if (returnCode == IDialogConstants.OK_ID) {
			String variable = variablesDialog.getVariableExpression();
			if (variable != null) {
				valueText.insert(variable.trim());
			}
		}
	}

	public String[] getNameValuePair() {
		return new String[] { name, value };
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			name = nameText.getText();
			value = valueText.getText();
		} else {
			name = null;
			value = null;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
	}

	protected void updateButtons() {
		String name = nameText.getText().trim();
		String value = valueText.getText().trim();
		// verify name
		Event e = new Event();
		e.widget = nameText;
		VerifyEvent ev = new VerifyEvent(e);
		ev.doit = true;
		if (verifyListener != null) {
			ev.text = name;
			verifyListener.verifyText(ev);
		}
		getButton(IDialogConstants.OK_ID).setEnabled((name.length() > 0) && (value.length() > 0) && ev.doit);
	}

	@Override
	public void create() {
		super.create();
		updateButtons();
	}
}
