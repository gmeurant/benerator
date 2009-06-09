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

import java.lang.reflect.Method;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.commons.BeanUtil;
import org.databene.commons.db.hsql.HSQLUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action Delegate that starts the HSQL database manager GUI for the built-in HSQL database instance.<br/>
 * <br/>
 * Created at 27.02.2009 07:19:24
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class StartHSQLManagerAction implements IWorkbenchWindowActionDelegate {

	public void init(IWorkbenchWindow window) {
	}

    @SuppressWarnings("unchecked")
    public void run(IAction action) { // TDOO v0.6 move to HSQLUtil
    	// DatabaseManager.main is called here by reflection in order to avoid compile-time dependencies to HSQL:
    	Class<Object> dbManagerClass = BeanUtil.forName("org.hsqldb.util.DatabaseManager");
    	Method mainMethod = BeanUtil.getMethod(dbManagerClass, "main", String[].class);
		BeanUtil.invoke(null, mainMethod, (Object) new String[] {
				"--noexit",
				"--driver", HSQLUtil.DRIVER,
				"--url", "jdbc:hsqldb:hsql://localhost:9001",
				"--user", "sa",
				"--password", ""
		});
	}

	public void selectionChanged(IAction action, ISelection selection) {
		BenclipsePlugin.getDefault().getHsqldbController().registerHsqlManagerAction(action);
	}

	public void dispose() {
	}

}