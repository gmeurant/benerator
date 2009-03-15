/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

import junit.framework.TestCase;

/**
 * Created: 21.06.2007 08:35:45
 */
public class VMInfoTest extends TestCase {

    public void testJavaVendor() {
        assertNotNull(System.getProperty("java.vendor"));
    }

    public void testJavaVendorUrl() {
        assertNotNull(System.getProperty("java.vendor.url"));
    }

    public void testJavaSpecificationVersion() {
        assertNotNull(System.getProperty("java.specification.version"));
    }

    public void testJavaSpecificationVendor() {
        assertNotNull(System.getProperty("java.specification.vendor"));
    }

    public void testJavaSpecificationName() {
        assertNotNull(System.getProperty("java.specification.name"));
    }

    public void testJavaClassVersion() {
        assertNotNull(System.getProperty("java.class.version"));
    }

    public void testJavaCompiler() {
        System.getProperty("java.compiler"); // may be null
    }

    public void testJavaHome() {
        assertNotNull(System.getProperty("java.home"));
    }

    public void testExtDirs() {
        assertNotNull(System.getProperty("java.ext.dirs"));
    }

    public void testClassPath() {
        assertNotNull(System.getProperty("java.class.path"));
    }

    public void testLibraryPath() {
        assertNotNull(System.getProperty("java.library.path"));
    }

    public void testJavaVMName() {
        assertNotNull(VMInfo.getJavaVmName());
    }

    public void testJavaVMSpecificationName() {
        assertNotNull(VMInfo.getJavaVmSpecificationName());
    }

    public void testJavaVMSpecificationVendor() {
        assertNotNull(VMInfo.getJavaVmSpecificationVendor());
    }

    public void testJavaVMSpecificationVarsion() {
        assertNotNull(VMInfo.getJavaVmSpecificationVersion());
    }

    public void testJavaVMVendor() {
        assertNotNull(VMInfo.getJavaVmVendor());
    }

    public void testJavaVersion() {
        assertNotNull(VMInfo.getJavaVmVersion());
    }

}
