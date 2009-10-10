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

package org.databene.commons;

import java.io.File;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link FileUtil} class.<br/>
 * <br/>
 * Created at 10.03.2009 18:21:54
 * @since 0.5.8
 * @author Volker Bergmann
 */

public class FileUtilTest {

	@Test
	public void testCopyFile() throws Exception {
		File rootDir = new File("target");
		File srcFile = new File(rootDir, getClass().getSimpleName() + ".original");
		File targetFile = new File(rootDir, getClass().getSimpleName() + ".copy");
		try {
	        IOUtil.writeTextFile(srcFile.getAbsolutePath(), "0123456789");
	        assertTrue(srcFile.exists());
	        
	        // create file no matter the preconditions
	        FileUtil.copy(srcFile, targetFile, true);
	        assertTrue(targetFile.exists());
	        assertTrue(targetFile.length() >= 10);
	        
	        // check that copy fails if file exists and overwrite=false
	        try {
	        	FileUtil.copy(srcFile, targetFile, false);
	        	fail("Exception expected if target file exists");
	        } catch (ConfigurationError e) {
	        	// exception is expected here
	        }
	        
	        // check that copy succeeds if file exists and overwrite=true
        	FileUtil.copy(srcFile, targetFile, true);

        } finally {
        	// remove the used files
        	if (targetFile.exists())
        		targetFile.delete();
        	if (srcFile.exists())
        		srcFile.delete();
        }
	}
	
}
