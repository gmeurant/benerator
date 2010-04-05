/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.databene.commons.file.DirectoryFileFilter;
import org.databene.commons.file.PatternFileFilter;

/**
 * File Utility class.<br/>
 * <br/>
 * Created: 04.02.2007 08:22:52
 * @since 0.1
 * @author Volker Bergmann
 */
public final class FileUtil {

    public static void ensureDirectoryExists(File directory) {
        if (directory != null && !directory.exists()) {
            File parent = directory.getParentFile();
            if (parent != null)
                ensureDirectoryExists(parent);
            directory.mkdir();
        }
    }

    public static boolean hasSuffix(File file, String suffix, boolean caseSensitive) {
        // don't use suffix(), because the extension may be like '.hbm.xml'
        if (caseSensitive)
            return file.getName().endsWith(suffix);
        else
            return file.getName().toLowerCase().endsWith(suffix.toLowerCase());
    }

    /** extracts the filename part after the last dot */
    public static String suffix(File file) {
        return suffix(file.getName());
    }

    /** extracts the filename part after the last dot */
    public static String suffix(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1)
            return "";
        return filename.substring(dotIndex + 1);
    }

	public static String nativePath(String path) {
		return path.replace('/', SystemInfo.getFileSeparator());
	}

	public static boolean isEmptyFolder(File folder) {
		String[] list = folder.list();
		return list == null || list.length == 0;
	}
	
    public static void copy(File srcFile, File targetFile, boolean overwrite) throws FileNotFoundException, IOException {
    	copy(srcFile, targetFile, overwrite, null);
    }
    
    public static void copy(File srcFile, File targetFile, boolean overwrite, FileFilter filter) 
    		throws FileNotFoundException, IOException {
    	if (filter != null && !filter.accept(srcFile.getCanonicalFile()))
    		return;
    	if (!srcFile.exists())
    		throw new ConfigurationError("Source file not found: " + srcFile);
    	if (!overwrite && targetFile.exists())
    		throw new ConfigurationError("Target file already exists: " + targetFile);
    	if (srcFile.isFile())
    		copyFile(srcFile, targetFile);
    	else
    		copyDirectory(srcFile, targetFile, overwrite, filter);
    }
    
	// private helpers -------------------------------------------------------------------------------------------------

	private static void copyFile(File srcFile, File targetFile)
			throws FileNotFoundException, IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(srcFile));
        OutputStream out = null;
        try {
	        out = new FileOutputStream(targetFile);
	        IOUtil.transfer(in, out);
        } finally {
	        IOUtil.close(out);
	        IOUtil.close(in);
        }
	}

	private static void copyDirectory(File srcDirectory, File targetDirectory, boolean overwrite, FileFilter filter) throws FileNotFoundException, IOException {
		ensureDirectoryExists(targetDirectory);
		for (File src : srcDirectory.listFiles()) {
			File dstFile = new File(targetDirectory, src.getName());
			copy(src, dstFile, overwrite, filter);
		}
	}

    public static void deleteIfExists(File file) {
	    if (file.exists()) {
	    	if (!file.delete())
	    		file.deleteOnExit();
	    }
    }

	public static List<File> listFiles(File dir, String regex, 
			boolean recursive, boolean acceptingFiles, boolean acceptingFolders) {
		PatternFileFilter filter = new PatternFileFilter(regex, acceptingFiles, acceptingFolders);
		return addFilenames(dir, filter, recursive, new ArrayList<File>());
    }

	private static List<File> addFilenames(File dir, FileFilter filter, boolean recursive, List<File> buffer) {
		File[] matches = dir.listFiles(filter);
		if (matches != null)
			for (File match : matches)
				buffer.add(match);
		if (recursive) {
	        File[] subDirs = dir.listFiles(DirectoryFileFilter.instance());
	        if (subDirs != null)
		        for (File subFolder : subDirs)
					addFilenames(subFolder, filter, recursive, buffer);
        }
		return buffer;
    }

}
