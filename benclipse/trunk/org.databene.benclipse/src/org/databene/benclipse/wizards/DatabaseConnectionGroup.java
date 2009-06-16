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

import java.sql.Connection;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.concurrent.Callable;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.BenclipseLogger;
import org.databene.benclipse.core.Messages;
import org.databene.benclipse.internal.HsqlDbController;
import org.databene.benclipse.swt.FileField;
import org.databene.benclipse.swt.SWTUtil;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.ExceptionUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.db.DBUtil;
import org.databene.commons.db.JDBCDriverInfo;
import org.databene.platform.db.DBSystem;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Benerator widget group for configuring a database connection.<br/>
 * <br/>
 * Created at 22.02.2009 20:02:02
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class DatabaseConnectionGroup extends Observable implements ModifyListener {
	
	private static final int STATE_EDITING = 0;
	private static final int STATE_CONFIGURED = 1;
	private static final int STATE_READY = 2;
	
	// attributes ------------------------------------------------------------------------------------------------------

	int state = STATE_EDITING;
	
	ComboViewer driverCombo;

	Text driverText;
	FileField libraryFile;
	
	Text hostText;
	Text portText;
	Text dbText;
	
	Text urlText;
	Text userText;
	Text schemaText;
	Text passwordText;
	
	Button testConnectionButton;
	boolean updateLock;
	Boolean connectionValid;
	String connectionError;
	JDBCDriverInfo builtinDriver;
	JDBCDriverInfo otherDriver;
	
	String errorMessage;
	
	Composite group;
	
	// interface -------------------------------------------------------------------------------------------------------
	
	public void createControl(Composite parent) {
		connectionValid = false;

		createBuiltInDriver();
		createOtherDriver();
		
		group = SWTUtil.createGroup(parent, Messages.getString("wizard.project.db.conn.title"), 6, 1, GridData.FILL_HORIZONTAL);
        group.setFont(parent.getFont());
        
        createDriverCombo(group);
        JDBCDriverInfo driver = getSelectedDriver();
        ModifyListener urlComponentListener = new URLComponentListener();

	    driverText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.driver.class"), 5);
	    driverText.setText(driver.getDriverClass());
	    driverText.addModifyListener(this);

	    libraryFile = new FileField(group, Messages.getString("wizard.project.db.driver.lib"), "...", 4);
	    libraryFile.setWorkspaceOnly(false);
	    libraryFile.setNullName("<plug-in>");
	    libraryFile.addModifyListener(this);

	    hostText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.host"), 1);
	    hostText.setText("localhost");
	    hostText.addModifyListener(urlComponentListener);

	    portText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.port"), 1);
	    portText.setText(driver.getDefaultPort());
	    portText.addModifyListener(urlComponentListener);

	    dbText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.db"), 1);
	    dbText.setText(getDefaultDb(driver));
	    dbText.addModifyListener(urlComponentListener);

	    urlText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.url"), 5);
	    updateUrl();
	    urlText.addModifyListener(new URLListener());

	    userText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.user"), 3);
	    userText.addModifyListener(this);
	    userText.setText(StringUtil.nullToEmpty(driver.getDefaultUser()));

	    passwordText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.password"), 1);
	    passwordText.setEchoChar('*');
	    passwordText.addModifyListener(this);
	    
	    schemaText = SWTUtil.createLabeledText(group, Messages.getString("wizard.project.db.schema"), 5);
    	schemaText.setText(StringUtil.nullToEmpty(driver.getDefaultSchema()));
	    schemaText.addModifyListener(this);

	    // Set up 'Test Connection' button
	    new Label(group, SWT.NONE);
	    new Label(group, SWT.NONE);
	    testConnectionButton = new Button(group, SWT.BUTTON1);
	    testConnectionButton.setText(Messages.getString("wizard.project.db.test.connection"));
	    GridData gd = new GridData();
	    gd.horizontalSpan = 2;
		testConnectionButton.setLayoutData(gd);
		testConnectionButton.addSelectionListener(new TestConnectionButtonListener());
    }
	
	public void setState(int state) {
    	this.state = state;
    }

	public JDBCDriverInfo getSelectedDriver() {
	    return (JDBCDriverInfo) ((IStructuredSelection) driverCombo.getSelection()).getFirstElement();
    }
    
    public boolean isConnectionValid() {
	    return connectionValid;
    }

    public boolean isDriverInstalled() {
    	return (state != STATE_EDITING);
    }
    
	@SuppressWarnings("unchecked")
    public Class instantiateDriver() {
		final String driverClassName = driverText.getText();
		try {
			Class result = BeanUtil.executeInJarClassLoader(libraryFile.getFile(), new Callable<Class>() {
				public Class call() throws Exception {
		            return BeanUtil.forName(driverClassName);
	            }
			});
			setState(STATE_CONFIGURED);
			return result;
		} catch (Throwable t) {
			setState(STATE_EDITING);
			throw new ConfigurationError("Error instantiating driver " + driverClassName, t);
		}
	}
	
    public String getConnectionError() {
	    return connectionError;
    }

    public JDBCConnectionInfo getConnectionInfo() {
    	JDBCConnectionInfo connectionInfo = new JDBCConnectionInfo(
    			urlText.getText(), 
    			driverText.getText(), 
    			userText.getText(), 
    			passwordText.getText(), 
    			schemaText.getText(),
    			libraryFile.getFile());
		return connectionInfo;
    }

	public boolean isUsingBuiltInDriver() {
        return builtinDriver.equals(getSelectedDriver());
    }

	public boolean isGroupComplete() {
	    return isUsingBuiltInDriver() || isConnectionValid();
	}

	// non-public helper methods ---------------------------------------------------------------------------------------

	private void createBuiltInDriver() {
	    String name = Messages.getString("wizard.project.db.builtin");
	    builtinDriver = new JDBCDriverInfo("BUILTIN", name, name);
	    builtinDriver.setDriverClass("org.hsqldb.jdbcDriver");
	    builtinDriver.setDefaultPort("9001");
	    builtinDriver.setUrlPattern("jdbc:hsqldb:hsql://{0}:{1}");
	    builtinDriver.setDefaultDatabase("");
	    builtinDriver.setDefaultUser("sa");
	    builtinDriver.setDefaultSchema("PUBLIC");
    }

	private void createOtherDriver() {
	    String name = Messages.getString("wizard.project.db.other");
	    otherDriver = new JDBCDriverInfo("OTHER", name, name);
    }

	private void createDriverCombo(Composite parent) {
    	SWTUtil.createLabel(parent, Messages.getString("wizard.project.db.driver"));
	    driverCombo = new ComboViewer(parent, 
	    		SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY);
	    Combo combo = driverCombo.getCombo();
	    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
	    gd.horizontalSpan = 5;
		combo.setLayoutData(gd);
	    driverCombo.setLabelProvider(new JDBCDriverLabelProvider());
	    driverCombo.setContentProvider(new DefaultContentProvider());
	    ArrayList<JDBCDriverInfo> drivers = new ArrayList<JDBCDriverInfo>(JDBCDriverInfo.getInstances());
	    drivers.add(builtinDriver);
	    drivers.add(otherDriver);
	    Collections.sort(drivers, new Comparator<JDBCDriverInfo>() {
			public int compare(JDBCDriverInfo d1, JDBCDriverInfo d2) {
	            return d1.getName().compareTo(d2.getName());
            }
	    });
		driverCombo.setInput(drivers);
	    driverCombo.setSelection(new StructuredSelection(builtinDriver));
	    driverCombo.addSelectionChangedListener(new DriverListener());
    }

	String getDefaultDb(JDBCDriverInfo driver) {
    	return (driver.getDefaultDatabase() != null ? driver.getDefaultDatabase() : "benerator");
    }
	
    // event handling --------------------------------------------------------------------------------------------------
    
	/** 
	 * Updates all controls if a text box content has been changed 
	 * @param modifyEvent the framework's event object
	 */
    public void modifyText(ModifyEvent modifyEvent) {
    	updateControls();
    }

    void updateControls() {
		checkDriver();
		checkState();
    }

	void checkState() {
	    if (!isDriverInstalled())
    		errorMessage = Messages.formatMessage("wizard.project.db.message.no.driver", getSelectedDriver().getDownloadUrl());
		else if (getConnectionError() != null)
    		errorMessage = getConnectionError();
    	else if (!isConnectionValid())
    		errorMessage = Messages.getString("wizard.project.db.message.not.available");
    	else
    		errorMessage = null;
    	setChanged();
    	notifyObservers();
    }
    
	void checkDriver() {
		connectionError = null;
        try {
	    	instantiateDriver();
	    } catch (Exception e) {
	    	// TODO v0.6.0 handle UnsupportedClassVersionError in a special way?
	    	BenclipseLogger.log("Error instantiating driver", e);
	    	e.printStackTrace();
	    }
	    if (testConnectionButton != null)
	    	testConnectionButton.setEnabled(state != STATE_EDITING);
    }

	void updateUrl() {
		String jdbcURL;
		try {
			jdbcURL = getSelectedDriver().jdbcURL(hostText.getText(), portText.getText(), dbText.getText());
		} catch (NullPointerException e) { // TODO v0.6.0 avoid this error in JDBCDriverInfo.jdbcURL(JDBCDriverInfo.java:165)
			jdbcURL = "";
		}
		urlText.setText(jdbcURL);
    }

    public String getErrorMessage() {
    	return errorMessage;
    }
    
	IStatus testConnection(JDBCConnectionInfo info, boolean builtInDriver) {
	    // start HSQL if necessary
	    try {
	        if (builtInDriver && HsqlDbController.canBeStarted())
	        	HsqlDbController.startHSQL(true);
	    } catch (Exception e) {
	    	return new Status(Status.ERROR, BenclipsePlugin.PLUGIN_ID,  
	    			Messages.getString("wizard.project.db.message.hsql.failed"), e);
	    }
	    // check if connection data is alright
	    Connection connection = null;
	    try {
	    	DBSystem db = new DBSystem("db", info.getUrl(), info.getDriverClass(), info.getUser(), info.getPassword());
	    	db.setSchema(info.getSchema());
	        connection = db.createConnection();
	        setState(STATE_READY);
			return new Status(IStatus.OK, BenclipsePlugin.PLUGIN_ID, 
					Messages.getString("wizard.project.db.message.connect.success"));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	setState(STATE_CONFIGURED);
			Throwable rootCause = ExceptionUtil.getRootCause(e);
			return new Status(IStatus.ERROR, BenclipsePlugin.PLUGIN_ID, 
					Messages.formatMessage("wizard.project.db.message.connect.failed", rootCause.getMessage()), rootCause);
	    } finally {
	    	DBUtil.close(connection);
	    }
    }
	
	// helper classes --------------------------------------------------------------------------------------------------
	
    class URLComponentListener implements ModifyListener {
        public void modifyText(ModifyEvent e) {
        	if (!updateLock) {
	        	updateLock = true;
		        updateUrl();
	        	updateLock = false;
        	}
        	updateControls();
        }
    }
	
    class URLListener implements ModifyListener {
        public void modifyText(ModifyEvent evt) {
    		connectionValid = false;
        	if (updateLock)
        		return;
	        updateLock = true;
        	try {
    	        MessageFormat format = new MessageFormat(getSelectedDriver().getUrlPattern());
    	        Object[] components = format.parse(urlText.getText());
    	        if (components.length > 0)
    	        	hostText.setText(String.valueOf(components[0]));
    	        if (components.length > 1)
    	        	portText.setText(String.valueOf(components[1]));
    	        if (components.length > 2)
    	        	dbText.setText(String.valueOf(components[2]));
        	} catch (ParseException e) {
        		hostText.setText("");
        		portText.setText("");
        		dbText.setText("");
            }
        	updateLock = false;
        	updateControls();
        }
    }

    public class TestConnectionButtonListener implements SelectionListener {
	    public void widgetDefaultSelected(SelectionEvent evt) {
	    	widgetSelected(evt);
	    }

	    public void widgetSelected(SelectionEvent evt) {
	    	final String connectMessage = Messages.getString("wizard.project.job.connect");
            final JDBCConnectionInfo info = getConnectionInfo();
            final boolean builtInDriver = isUsingBuiltInDriver();
            final Job job = new WorkspaceJob(connectMessage) {
            	@Override
            	public IStatus runInWorkspace(final IProgressMonitor monitor) {
            		try {
            			IStatus result = BeanUtil.executeInJarClassLoader(libraryFile.getFile(), new Callable<IStatus>() {
            				public IStatus call() throws Exception {
            					return testConnection(info, builtInDriver);
                            }
            			});
						return result;
            		} catch (Exception e) {
            			return new Status(Status.ERROR, BenclipsePlugin.PLUGIN_ID, e.getMessage(), e);
            		} finally {
            			monitor.done();
            		}
            	}
            };
            
            job.addJobChangeListener(new TestConnectionJobListener());
            job.setPriority(20);
            job.schedule(); // TODO v0.6 have progress dialog instead of internal progress bar
	    }
    }

    /** Updates the GUI after changes in the selection of the database driver */
    class DriverListener implements ISelectionChangedListener {
        public void selectionChanged(SelectionChangedEvent evt) {
	        if (hostText.getText().length() == 0)
	        	hostText.setText("localhost");
	        JDBCDriverInfo driver = getSelectedDriver();
	    	if (driver != otherDriver) {
	    		driverText.setText(driver.getDriverClass());
	    		libraryFile.setFile(null);
	    	}
			portText.setText(driver.getDefaultPort() != null ? driver.getDefaultPort() : "");
	        if (dbText.getText().length() == 0)
	        	dbText.setText(getDefaultDb(driver));
		    userText.setText(StringUtil.nullToEmpty(driver.getDefaultUser()));
		    schemaText.setText(StringUtil.nullToEmpty(driver.getDefaultSchema()));
        	updateControls();
        }

    }
    
    class TestConnectionJobListener extends JobChangeAdapter {
    	
		@Override
        public void done(IJobChangeEvent event) {
			final IStatus result = event.getResult();
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (result.isOK()) {
				    	connectionValid = true;
				    	connectionError = null;
				    	MessageDialog.openInformation(group.getShell(), 
				    			Messages.getString("wizard.project.db.message.connect.result"), 
				    			result.getMessage());
					} else {
				    	connectionValid = false;
				    	connectionError = ExceptionUtil.getRootCause(result.getException()).getMessage();
				    	/*
				    	MessageDialog.openError(group.getShell(), 
				    			Messages.getString("wizard.project.db.message.connect.result"), 
				    			result.getMessage());*/
					}
					checkState();
				}
			});
		}
    }

	static final class JDBCDriverLabelProvider extends LabelProvider {
    	@Override
    	public String getText(Object element) {
    		return ((JDBCDriverInfo) element).getDbSystem();
    	}
    }
    
}
