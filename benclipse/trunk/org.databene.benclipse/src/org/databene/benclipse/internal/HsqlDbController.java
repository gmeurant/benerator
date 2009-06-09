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

package org.databene.benclipse.internal;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME;
import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.commons.db.DBUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.Bundle;

/**
 * Provides for easy management of a local HSQL database instance.<br/>
 * <br/>
 * Created at 23.02.2009 18:55:07
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class HsqlDbController {

	private static final String HSQLDB_MAIN_CLASS = "org.hsqldb.Server";
	
	// TODO v0.6 store the following setup in ILaunchConfiguration and read it from there, 
	// so a user can modify it via 'Run Configurations'
    static final String DRIVER = "org.hsqldb.jdbcDriver";
    static final int PORT = 9001;
    static final String URL = "jdbc:hsqldb:hsql://loalhost:" + PORT;
    static final String USER="sa";
    static final String PASSWORD = "";
	
	private static final String START_HSQL_CFG_NAME = "Start HSQL";
	private static final long MAX_WAIT = 3000;

	static protected IProcess hsqlProcess;

	private static IDebugEventSetListener listener = new IDebugEventSetListener() {
	    public void handleDebugEvents(DebugEvent[] events) {
	    	// if the event was a terminate...
			if (events[0].getKind() == DebugEvent.TERMINATE) {
				Object source = events[0].getSource();
				// ...and the terminated process is a HSQLDB Server...
				if (source instanceof IProcess && source == hsqlProcess) {
					//...remove it and update the ui
					hsqlProcess = null;
					System.out.println("...HSQL Shutdown completed!");
					BenclipsePlugin.getDefault().getHsqldbController().setHsqlRunning(false);
				}
			}
	    }
	};
	
	@SuppressWarnings("unchecked")
    public static void startHSQL(boolean waitUntilAvailable) throws CoreException, IOException {
	    System.out.println("Starting HSQL...");
	    ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
	    ILaunchConfigurationType type = manager.getLaunchConfigurationType(ID_JAVA_APPLICATION);
	    ILaunchConfiguration startHsqlCfg = null;
	    ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
	    for (int i = 0; i < configurations.length; i++) {
	    	ILaunchConfiguration configuration = configurations[i];
	    	if (configuration.getName().equals(START_HSQL_CFG_NAME)) {
	    		startHsqlCfg = configuration;
	    		break;
	    	}
	    }

	    if (startHsqlCfg == null) {
	    	// create launch configuration if it does not exist yet
	    	ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, START_HSQL_CFG_NAME);
	    	
	    	// set main class and arguments
	    	workingCopy.setAttribute(ATTR_MAIN_TYPE_NAME, HSQLDB_MAIN_CLASS);
	    	// workingCopy.setAttribute(ATTR_PROGRAM_ARGUMENTS, "start");
	    	
	    	// set up class path
	    	List<String> classpath = new ArrayList<String>();
	    	
	    	// JRE classes
	    	IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
	    	IRuntimeClasspathEntry systemLibsEntry = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
	    	        IRuntimeClasspathEntry.STANDARD_CLASSES);
	    	classpath.add(systemLibsEntry.getMemento());
	    	
	    	// libraries in lib file
	    	Bundle bundle = BenclipsePlugin.getDefault().getBundle();
	    	Enumeration<String> entryPaths = bundle.getEntryPaths("lib");
	    	while (entryPaths.hasMoreElements()) {
	    		String elementName = entryPaths.nextElement();
	    		URL elementUrl = bundle.getEntry(elementName);
	    		elementUrl = FileLocator.resolve(elementUrl);
	    		IPath elementPath = new Path(new File(elementUrl.getFile()).getAbsolutePath());
	    		IRuntimeClasspathEntry elementClassPath = JavaRuntime.newArchiveRuntimeClasspathEntry(elementPath);
	    		classpath.add(elementClassPath.getMemento());
	    	}
	    	
	    	workingCopy.setAttribute(ATTR_CLASSPATH, classpath);
	    	workingCopy.setAttribute(ATTR_DEFAULT_CLASSPATH, false);

	    	//startHsqlCfg = workingCopy.doSave();
	    	startHsqlCfg = workingCopy;
	    	
	    }
	    DebugPlugin.getDefault().addDebugEventListener(listener);
	    //DebugUITools.launch(startHsqlCfg, ILaunchManager.RUN_MODE);
	    hsqlProcess = startHsqlCfg.launch(ILaunchManager.RUN_MODE, null).getProcesses()[0];
	    BenclipsePlugin.getDefault().getHsqldbController().setHsqlRunning(true);
	    if (waitUntilAvailable) {
		    long startTime = System.currentTimeMillis();
	    	do {
			    try {
			    	if (DBUtil.available(URL, DRIVER, USER, PASSWORD))
			    		break;
			    	Thread.sleep(500);
			    } catch (Exception e) {
			    	// ignore
			    }
	    	} while (System.currentTimeMillis() - startTime < MAX_WAIT);
	    }
    }
	
	public static void stopHSQL() throws ClassNotFoundException, SQLException { // TODO v0.6 move implementation to HSQLUtil
        Statement statement = null;
		try {
			System.out.println("Stopping HSQL...");
	        // submits the SHUTDOWN statement
	        Class.forName("org.hsqldb.jdbcDriver");
	        String url = "jdbc:hsqldb:hsql://127.0.0.1:" + PORT;
	        Connection con = DriverManager.getConnection(url, "sa", "");
	        String sql = "SHUTDOWN";
	        statement = con.createStatement();
	        statement.executeUpdate(sql);
	        BenclipsePlugin.getDefault().getHsqldbController().setHsqlRunning(false);
		} finally {
			DBUtil.close(statement);
		}
	}

	public static boolean isRunning() {
		return (hsqlProcess != null);
	}

	public static boolean canBeStarted() {
		return (!isRunning() && isPortFree());
	}

    private static boolean isPortFree() {
    	Socket socket = null;
	    try {
	        socket = new Socket("127.0.0.1", PORT);
	        return false;
        } catch (Exception e) {
        	return true;
        } finally {
        	if (socket != null)
        		close(socket);
        }
    }

    private static void close(Socket socket) {
	    if (socket != null) {
	    	try {
	    		socket.close();
	    	} catch (Exception e) {
	    		
	    	}
	    }
    }
}
