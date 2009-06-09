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

package org.databene.benclipse;

import java.util.HashMap;
import java.util.Map;

import org.databene.benclipse.actions.EditorWatchdog;
import org.databene.benclipse.actions.HsqlActionController;
import org.databene.benclipse.core.IBenclipseConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class for the Benclipse plugin.<br/>
 * <br/>
 * Created at 01.02.2009 12:12:12
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class BenclipsePlugin extends AbstractUIPlugin implements IBenclipseConstants {

	private static BenclipsePlugin plugin;
	
	private final HsqlActionController hsqldbController = new HsqlActionController();
	
	EditorWatchdog editorWatchdog = new EditorWatchdog();

	public BenclipsePlugin() { }

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static BenclipsePlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public HsqlActionController getHsqldbController() {
    	return hsqldbController;
    }

	private static Map<String, Image> images = new HashMap<String, Image>();

	public static Image getImage(String relativePath) {
		Image image = images.get(relativePath);
		if (image == null) {
			ImageDescriptor descriptor = BenclipsePlugin.getImageDescriptor(relativePath);
			if (descriptor != null) {
				image = descriptor.createImage();
				images.put(relativePath, image);
			}
		}
		return image;
	}

    public void bindControls() {
    	getActiveWorkbenchWindow().getPartService().addPartListener(editorWatchdog);
    	updateControls();
    }

	public void updateControls() {
	    IPreferenceStore preferenceStore = BenclipsePlugin.getDefault().getPreferenceStore();
        IWorkbenchPage activePage = getActivePage();
    	boolean showMenu = preferenceStore.getBoolean(PREF_MAIN_MENU);
		if (showMenu) {
	        activePage.showActionSet(BENERATOR_ACTION_SET);
	    	if (preferenceStore.getBoolean(PREF_HSQL))
				activePage.showActionSet(HSQL_ACTION_SET);
	    	else
	    		activePage.hideActionSet(HSQL_ACTION_SET);
	    } else {
    		activePage.hideActionSet(BENERATOR_ACTION_SET);
    		activePage.hideActionSet(HSQL_ACTION_SET);
        }
    }
    
    public EditorWatchdog getEditorWatchdog() {
    	return editorWatchdog;
    }

    public static IWorkbenchPage getActivePage() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        return (window != null ? window.getActivePage() : null);
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    public static Shell getActiveWorkbenchShell() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        return (window != null ? window.getShell() : null);
    }

}
