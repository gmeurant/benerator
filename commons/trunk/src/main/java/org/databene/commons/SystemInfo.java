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

import java.nio.charset.Charset;

/**
 * Provides the user with the Java system properties related to the Runtime System.
 * Created: 06.01.2007 19:10:02
 */
public final class SystemInfo {

    /**
     * @return the OS name
     */
    public static String osName() {
        return System.getProperty("os.name");
    }

    /**
     * @return the OS architecture
     */
    public static String osArchitecture() {
        return System.getProperty("os.arch");
    }

    /**
     * @return the OS version
     */
    public static String osVersion() {
        return System.getProperty("os.version");
    }

    /**
     * @return Line separator ("\n" on UNIX)
     */
    public static String lineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * @return Path separator (":" on UNIX)
     */
    public static String pathSeparator() {
        return System.getProperty("path.separator");
    }

    /**
     * @return File separator ("/" on UNIX)
     */
    public static char fileSeparator() {
        return System.getProperty("file.separator").charAt(0);
    }

    /**
     * @return the user's current directory
     */
    public static String currentDir() {
        return System.getProperty("user.dir");
    }

    /**
     * @return the user's name
     */
    public static String userName() {
        return System.getProperty("user.name");
    }

    /**
     * @return the user's home directory
     */
    public static String userHome() {
        return System.getProperty("user.home");
    }

    /**
     * @return the default temp file path
     */
    public static String tempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * @return the file encoding
     */
    public static String fileEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * @return user language
     */
    public static String userLanguage() {
        return System.getProperty("user.language");
    }

	/**
	 * @return true if the system is a Windows version, else false
	 */
	public static boolean isWindows() {
		return osName().startsWith("Windows");
	}

	/**
	 * @return true if the system is Mac OS X, else false
	 */
	public static boolean isMacOsx() {
		return osName().startsWith("Mac OS X");
	}

	/**
	 * @return the system's default {@link Charset}
	 */
	public static Charset charset() {
		return Charset.forName(SystemInfo.fileEncoding());
	}

}
