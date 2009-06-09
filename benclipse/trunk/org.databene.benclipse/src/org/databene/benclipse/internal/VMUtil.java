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

import java.util.ArrayList;
import java.util.List;

import org.databene.model.version.VersionNumber;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Utility class for finding VM installations in Eclipse.<br/>
 * <br/>
 * Created at 22.02.2009 13:08:36
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class VMUtil {

	public static IVMInstall findVmInstallVersionOrNewer(String minVersion) {
	    IVMInstall vmInstall = null;
	    IVMInstall defaultVMInstall = JavaRuntime.getDefaultVMInstall();
	    if (versionAtLeast(minVersion, defaultVMInstall))
	    	vmInstall = defaultVMInstall;
	    else {
	    	for (IVMInstallType installType : JavaRuntime.getVMInstallTypes()) {
	    		for (IVMInstall install : installType.getVMInstalls()) {
	    			if (versionAtLeast(minVersion, install)) {
	    				vmInstall = install;
	    				break;
	    			}
	    		}
	    		if (vmInstall != null)
	    			break;
	    	}
	    }
	    return vmInstall;
    }
	
	public static List<IVMInstall> findVmInstallsVersionOrNewer(String minVersion) {
		List<IVMInstall> result = new ArrayList<IVMInstall>();
    	for (IVMInstallType installType : JavaRuntime.getVMInstallTypes())
    		for (IVMInstall install : installType.getVMInstalls())
    			if (versionAtLeast(minVersion, install))
    				result.add(install);
	    return result;
    }
	
    public static boolean versionAtLeast(String minVersion, IVMInstall vmInstall) {
    	VersionNumber minVersionNumber = new VersionNumber(minVersion);
		VersionNumber versionNumber = new VersionNumber(javaVersion(vmInstall));
		return versionNumber.compareTo(minVersionNumber) >= 0;
    }

    public static String javaVersion(IVMInstall vmInstall) {
		if (vmInstall instanceof IVMInstall2)
			return ((IVMInstall2) vmInstall).getJavaVersion();
		return vmInstall.getId();
    }


}
