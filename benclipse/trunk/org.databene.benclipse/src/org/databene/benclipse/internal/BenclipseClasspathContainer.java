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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.databene.benclipse.BenclipsePlugin;
import org.databene.benclipse.core.IBenclipseConstants;
import org.databene.commons.ArrayBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

/**
 * IClasspathContainer implementation that groups all Benerator libraries.<br/>
 * <br/>
 * Created at 31.03.2009 20:17:18
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class BenclipseClasspathContainer implements IClasspathContainer {
	
	private static final Path PATH = new Path(IBenclipseConstants.CONTAINER_ID);

	private IClasspathEntry[] entries;
	
    public BenclipseClasspathContainer() throws CoreException {
    	try {
		    Bundle bundle = BenclipsePlugin.getDefault().getBundle();
	        URL libEntry = bundle.getEntry("lib");
			File srcDir = new File(FileLocator.resolve(libEntry).getFile());
			ArrayBuilder<IClasspathEntry> builder = new ArrayBuilder<IClasspathEntry>(IClasspathEntry.class);
	        for (File member : srcDir.listFiles())
	        	if (member.getName().toLowerCase().endsWith(".jar"))
	        		builder.append(JavaCore.newLibraryEntry(new Path(member.getAbsolutePath()), null, null));
		    this.entries = builder.toArray();
    	} catch (IOException e) {
    		throw new CoreException(new Status(IStatus.ERROR, IBenclipseConstants.PLUGIN_ID, 
    				"Error initializing Benclipse Classpath container", e));
    	}
    }

	public IClasspathEntry[] getClasspathEntries() {
	    return entries;
    }

    public String getDescription() {
	    return "Benclipse Library";
    }

    public int getKind() {
	    return K_APPLICATION;
    }

    public IPath getPath() {
	    return PATH;
    }

}
