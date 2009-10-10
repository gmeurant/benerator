/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.commons;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link VMInfo} class.<br/><br/>
 * Created: 21.06.2007 08:35:45
 * @author Volker Bergmann
 */
public class VMInfoTest {
	
	@Test
    public void testJavaVendor() { 
        assertEquals(System.getProperty("java.vendor"), VMInfo.getJavaVendor());
    }

	// TODO all these tests are nonsense!

	@Test
    public void testJavaVendorUrl() {
        assertNotNull(System.getProperty("java.vendor.url"));
    }

	@Test
    public void testJavaSpecificationVersion() {
        assertNotNull(System.getProperty("java.specification.version"));
    }

	@Test
    public void testJavaSpecificationVendor() {
        assertNotNull(System.getProperty("java.specification.vendor"));
    }

	@Test
    public void testJavaSpecificationName() {
        assertNotNull(System.getProperty("java.specification.name"));
    }

	@Test
    public void testJavaClassVersion() {
        assertNotNull(System.getProperty("java.class.version"));
    }

	@Test
    public void testJavaCompiler() {
        System.getProperty("java.compiler"); // may be null
    }

	@Test
    public void testJavaHome() {
        assertNotNull(System.getProperty("java.home"));
    }

	@Test
    public void testExtDirs() {
        assertNotNull(System.getProperty("java.ext.dirs"));
    }

	@Test
    public void testClassPath() {
        assertNotNull(System.getProperty("java.class.path"));
    }

	@Test
    public void testLibraryPath() {
        assertNotNull(System.getProperty("java.library.path"));
    }

	@Test
    public void testJavaVMName() {
        assertNotNull(VMInfo.getJavaVmName());
    }

	@Test
    public void testJavaVMSpecificationName() {
        assertNotNull(VMInfo.getJavaVmSpecificationName());
    }

	@Test
    public void testJavaVMSpecificationVendor() {
        assertNotNull(VMInfo.getJavaVmSpecificationVendor());
    }

	@Test
    public void testJavaVMSpecificationVarsion() {
        assertNotNull(VMInfo.getJavaVmSpecificationVersion());
    }

	@Test
    public void testJavaVMVendor() {
        assertNotNull(VMInfo.getJavaVmVendor());
    }

	@Test
    public void testJavaVersion() {
        assertNotNull(VMInfo.getJavaVmVersion());
    }

}
