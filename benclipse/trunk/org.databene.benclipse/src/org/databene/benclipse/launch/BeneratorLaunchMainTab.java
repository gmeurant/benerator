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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.BenclipseLogger;
import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.swt.SWTUtil;
import org.databene.benclipse.wizards.BeneratorPropertyDialog;
import org.databene.commons.StringUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Main editor panel for Benerator specific launch configuration.<br/>
 * <br/>
 * Created at 05.02.2009 13:30:28
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BeneratorLaunchMainTab extends AbstractLaunchConfigurationTab {
	
	protected Text descriptorFilenameText;
	
	protected Table propsTable;
	
	private Button validateButton;
	private Button optimizeButton;
	//private Button openButton;

	@Override
	public Image getImage() {
		return BenclipsePlugin.getImage("icons/main_tab.gif");
	}

	public String getName() {
		return "Benerator";
	}

	public void createControl(Composite parent) {
		Composite control = SWTUtil.createComposite(parent, 3, 1, GridData.FILL_HORIZONTAL, true);

		createDescriptorEditor(control);
		createCheckboxes(control);
		createPropertyTable(control);
		setControl(control);
		// TODO v0.6 provide help for all GUI elements
		// like PlatformUI.getWorkbench().getHelpSystem().setHelp(mainComposite, BeneratorConstants.BENERATOR_MAIN_TAB);
		descriptorFilenameText.setFocus();
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		String descriptorFilename = getAttribute(configuration, IBenclipseConstants.ATTR_FILENAME, "");
		if (descriptorFilename.length() > 0) {
			IProject project = getProject(configuration);
			IFile descriptorFile = project.getFile(descriptorFilename);
			descriptorFilename = "${workspace_loc:/" + project.getName() + "}/" + descriptorFile.getProjectRelativePath();
		}
		this.descriptorFilenameText.setText(descriptorFilename);

		this.validateButton.setSelection(getAttribute(configuration, IBenclipseConstants.ATTR_VALIDATE, true));
		this.optimizeButton.setSelection(getAttribute(configuration, IBenclipseConstants.ATTR_OPTIMIZE, false));
		//this.openButton.setSelection(getAttribute(configuration, IBenclipseConstants.ATTR_OPEN_GEN_FILES, false));

		try {
			propsTable.removeAll();

			@SuppressWarnings("unchecked")
			List<String> properties = configuration.getAttribute(IBenclipseConstants.ATTR_PROPERTIES,
			        Collections.EMPTY_LIST);
			for (String property : properties) {
				try {
					String[] ss = StringUtil.splitOnFirstSeparator(property, '=');
					TableItem item = new TableItem(propsTable, SWT.NONE);
					item.setText(0, ss[0]);
					if (ss.length > 1) {
						item.setText(1, ss[1]);
					}
				} catch (Exception e) {
					BenclipseLogger.log("Error parsing argument: " + property, e);
				}
			}
		} catch (CoreException ex) {
			BenclipseLogger.log(ex);
		}
		setDirty(false);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		//configuration.setAttribute(IBenclipseConstants.ATTR_FILENAME, "benerator.xml");
		configuration.setAttribute(IBenclipseConstants.ATTR_VALIDATE, true);
		configuration.setAttribute(IBenclipseConstants.ATTR_OPTIMIZE, false);
		configuration.setAttribute(IBenclipseConstants.ATTR_OPEN_GEN_FILES, true);
		configuration.setAttribute(IBenclipseConstants.ATTR_PROPERTIES, new ArrayList<String>());
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String descriptorFile = getDescriptorFile(this.descriptorFilenameText.getText());
		if (!StringUtil.isEmpty(descriptorFile)) {
			Path path = new Path(descriptorFile);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
			LaunchUtil.setDescriptorFile(file, configuration);
		}

		configuration.setAttribute(IBenclipseConstants.ATTR_VALIDATE, this.validateButton.getSelection());
		configuration.setAttribute(IBenclipseConstants.ATTR_OPTIMIZE, this.optimizeButton.getSelection());
		//configuration.setAttribute(IBenclipseConstants.ATTR_DEBUG, this.debugOutputButton.getSelection());
		//configuration.setAttribute(IBenclipseConstants.ATTR_OPEN_GEN_FILES, this.openButton.getSelection());
		
		// store as String in "param=value" format 
		List<String> properties = new ArrayList<String>();
		for(TableItem item : this.propsTable.getItems()) {
			String p = item.getText(0);
			String v = item.getText(1);
			if(p != null && p.trim().length() > 0) {
				String prop = p.trim() + "=" + (v == null ? "" : v);
				properties.add(prop);
			}
		}
		configuration.setAttribute(IBenclipseConstants.ATTR_PROPERTIES, properties);
	  }

	@Override
	public boolean isValid(ILaunchConfiguration configuration) {
		setErrorMessage(null);

		String descriptorFilename = this.descriptorFilenameText.getText();
		if (descriptorFilename == null || descriptorFilename.trim().length() == 0) {
			setErrorMessage(Messages.getString("launch.descriptor.required"));
			return false;
		}
		if (!fileExists(descriptorFilename)) {
			setErrorMessage(Messages.getString("launch.descriptor.not.exist"));
			return false;
		}
		if (!isDescriptorFilename(descriptorFilename)) {
			setErrorMessage(Messages.formatMessage("launch.not.a.descriptor", descriptorFilename));
			return false;
		}
		return true;
	}
	
	// helper methods --------------------------------------------------------------------------------------------------

    private void createDescriptorEditor(Composite parent) {
    	Group group = SWTUtil.createGroup(parent, Messages.getString("launch.descriptor.title"), 2, 3, GridData.FILL_HORIZONTAL); 
		this.descriptorFilenameText = SWTUtil.createText(group, 2);
		this.descriptorFilenameText.addModifyListener(new FilenameListener());

		// descriptor file selection buttons
		new Label(group, SWT.NONE);
		Composite descriptorButtonsComposite = SWTUtil.createComposite(group, 2, 1, GridData.HORIZONTAL_ALIGN_END);
		Button browseWorkspaceButton = new Button(descriptorButtonsComposite, SWT.NONE);
		browseWorkspaceButton.setText(Messages.getString("launch.descriptor.workspace"));
		browseWorkspaceButton.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("synthetic-access")
            @Override
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog =
					new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());

			    int res = dialog.open();
			    if(res == IDialogConstants.OK_ID) {
			    	Object firstResult = dialog.getFirstResult();
			    	IFile file = (IFile) ((IAdaptable) firstResult).getAdapter(IFile.class);
					descriptorFilenameText.setText(file.getProject().getName() +"/" + file.getProjectRelativePath());
			      entriesChanged();
			    }
			}
		});
		final Button browseVariablesButton = new Button(descriptorButtonsComposite, SWT.NONE);
		browseVariablesButton.setText(Messages.getString("launch.descriptor.variables"));
		browseVariablesButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
            @Override
			public void widgetSelected(SelectionEvent e) {
				StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
				dialog.open();
				String variable = dialog.getVariableExpression();
				if (variable != null) {
					descriptorFilenameText.insert(variable);
				}
			}
		});
	}

	private void createCheckboxes(Composite parent) {
    	Group group = SWTUtil.createGroup(parent, Messages.getString("launch.options.title"), 1, 1, GridData.FILL_BOTH); 
    	Composite composite = SWTUtil.createComposite(group, 1, 1, GridData.FILL_BOTH);
    	SelectionListener listener = new CheckListener();
		validateButton = SWTUtil.createLocalCheckbox(
    			composite, "launch.properties.validate", listener, "-Dbenerator.validate", 1);
	    optimizeButton = SWTUtil.createLocalCheckbox(
	    		composite, "launch.properties.optimize", listener, "-Dbenerator.optimize", 1);
	    //openButton = SWTUtil.createLocalCheckbox(
	    //		composite, "launch.properties.open.generated.files", listener, "", 1); // TODO v0.6 'open files' checkbox
    }

	private void createPropertyTable(Composite parent) {
    	Group group = SWTUtil.createGroup(parent, Messages.getString("launch.props.title"), 2, 1, GridData.FILL_BOTH); 
    	Composite composite = SWTUtil.createComposite(group, 2, 2, GridData.FILL_BOTH);

	    TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				TableItem[] selection = propsTable.getSelection();
				if (selection.length == 1)
					editProperty(selection[0].getText(0), selection[0].getText(1));
			}
		});

		this.propsTable = tableViewer.getTable();
		// this.tProps.setItemCount(10);
		this.propsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		this.propsTable.setLinesVisible(true);
		this.propsTable.setHeaderVisible(true);

		final TableColumn propColumn = new TableColumn(this.propsTable, SWT.NONE, 0);
		propColumn.setWidth(120);
		propColumn.setText(Messages.getString("launch.props.name"));

		final TableColumn valueColumn = new TableColumn(this.propsTable, SWT.NONE, 1);
		valueColumn.setWidth(200);
		valueColumn.setText(Messages.getString("launch.props.value"));

		final Button addPropButton = new Button(composite, SWT.NONE);
		addPropButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		addPropButton.setText(Messages.getString("launch.props.add"));
		addPropButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addProperty();
			}
		});

		final Button removePropButton = new Button(composite, SWT.NONE);
		removePropButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		removePropButton.setText(Messages.getString("launch.props.remove"));
		removePropButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (propsTable.getSelectionCount() > 0) {
					propsTable.remove(propsTable.getSelectionIndices());
					entriesChanged();
				}
			}
		});
    }

	void addProperty() {
		BeneratorPropertyDialog dialog = new BeneratorPropertyDialog(getShell(), 
		        Messages.getString("dialog.prop.add.title"), new String[] {}, true);
		int res = dialog.open();
		if (res == IDialogConstants.OK_ID) {
			String[] result = dialog.getNameValuePair();
			TableItem item = new TableItem(propsTable, SWT.NONE);
			item.setText(0, result[0]);
			item.setText(1, result[1]);
			entriesChanged();
		}
	}

	void editProperty(String name, String value) {
		BeneratorPropertyDialog dialog = new BeneratorPropertyDialog(getShell(), 
		        Messages.getString("dialog.prop.edit.title"), new String[] { name, value }, true);
		int res = dialog.open();
		if (res == IDialogConstants.OK_ID) {
			String[] result = dialog.getNameValuePair();
			TableItem[] item = propsTable.getSelection();
			// we expect only one row selected
			item[0].setText(0, result[0]);
			item[0].setText(1, result[1]);
			entriesChanged();
		}
	}

	private IProject getProject(ILaunchConfiguration configuration) {
	    String projectName = getAttribute(configuration, IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
	    return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }

	private String getAttribute(ILaunchConfiguration configuration, String name, String defaultValue) {
		try {
			return configuration.getAttribute(name, defaultValue);
		} catch (CoreException ex) {
			return defaultValue;
		}
	}

	private boolean getAttribute(ILaunchConfiguration configuration, String name, boolean defaultValue) {
		try {
			return configuration.getAttribute(name, defaultValue);
		} catch (CoreException ex) {
			return defaultValue;
		}
	}

	protected boolean fileExists(String name) {
        if (name == null || name.trim().length() == 0)
        	return false;
        name = getDescriptorFile(name);
        if (name == null)
        	return false;
        return (new File(name).exists());
	}

	private String getDescriptorFile(String expression) {
		try {
			return VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(expression);
        } catch (CoreException e) {
        	BenclipseLogger.log(e);
            return null;
        }
    }

	protected boolean isDescriptorFilename(String name) {
		if (name == null || name.trim().length() == 0)
			return false;
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(name));
		String localFilename = file.getName();
		return ("benerator.xml".equals(localFilename) || localFilename.endsWith(".ben.xml"));
	}

	void entriesChanged() {
		setDirty(true);
		updateLaunchConfigurationDialog();
	}

	class FilenameListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			entriesChanged();
		}
	}
	
	class CheckListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent arg0) {
			entriesChanged();
        }

        public void widgetSelected(SelectionEvent arg0) {
			entriesChanged();
        }
		
	}

}
