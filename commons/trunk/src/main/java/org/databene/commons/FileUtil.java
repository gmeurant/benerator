/*
 * (c) Copyright 2007, 2008 by Volker Bergmann. All rights reserved.
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

/**
 * File Utility class.<br/>
 * <br/>
 * Created: 04.02.2007 08:22:52
 * @since 0.1
 * @author Volker Bergmann
 */
public final class FileUtil {

    public static void ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
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
		return path.replace("/", SystemInfo.fileSeparator());
	}

	public static boolean isEmptyFolder(File folder) {
		String[] list = folder.list();
		return list == null || list.length == 0;
	}
}
