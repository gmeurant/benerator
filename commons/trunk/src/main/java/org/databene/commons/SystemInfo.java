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
 * Provides the user with the Java system properties related to the Runtime System.<br/>
 * <br/>
 * Created: 06.01.2007 19:10:02
 * @author Volker Bergmann
 */
public final class SystemInfo {

	public static final String USER_LANGUAGE = "user.language";
	public static final String FILE_ENCODING = "file.encoding";
	public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	public static final String USER_HOME = "user.home";
	public static final String USER_NAME = "user.name";
	public static final String USER_DIR = "user.dir";
	public static final String FILE_SEPARATOR = "file.separator";
	public static final String PATH_SEPARATOR = "path.separator";
	public static final String LINE_SEPARATOR = "line.separator";
	public static final String OS_VERSION = "os.version";
	public static final String OS_ARCH = "os.arch";
	public static final String OS_NAME = "os.name";

	private static final String MAC_OS_X = "Mac OS X";
	private static final String WINDOWS = "Windows";

	// TODO v0.5.8 create getters and setters for each System property and deprecate the old methods
	
	/**
     * @return the OS name
     */
    public static String osName() {
        return System.getProperty(OS_NAME);
    }

    /**
     * @return the OS architecture
     */
    public static String osArchitecture() {
        return System.getProperty(OS_ARCH);
    }

    /**
     * @return the OS version
     */
    public static String osVersion() {
        return System.getProperty(OS_VERSION);
    }

    /**
     * @return Line separator ("\n" on UNIX)
     */
    public static String lineSeparator() {
        return System.getProperty(LINE_SEPARATOR);
    }

    /**
     * @return Path separator (":" on UNIX)
     */
    public static String pathSeparator() {
        return System.getProperty(PATH_SEPARATOR);
    }

    /**
     * @return File separator ("/" on UNIX)
     */
    public static char fileSeparator() {
        return System.getProperty(FILE_SEPARATOR).charAt(0);
    }

    /**
     * @return the user's current directory
     */
    public static String currentDir() {
        return System.getProperty(USER_DIR);
    }

    /**
     * @return the user's name
     */
    public static String userName() {
        return System.getProperty(USER_NAME);
    }

    /**
     * @return the user's home directory
     */
    public static String userHome() {
        return System.getProperty(USER_HOME);
    }

    /**
     * @return the default temp file path
     */
    public static String tempDir() {
        return System.getProperty(JAVA_IO_TMPDIR);
    }

    /**
     * @return the file encoding
     */
    public static String fileEncoding() {
        return System.getProperty(FILE_ENCODING);
    }

    /**
     * @return user language
     */
    public static String userLanguage() {
        return System.getProperty(USER_LANGUAGE);
    }

	/**
	 * @return true if the system is a Windows version, else false
	 */
	public static boolean isWindows() {
		return osName().startsWith(WINDOWS);
	}

	/**
	 * @return true if the system is Mac OS X, else false
	 */
	public static boolean isMacOsx() {
		return osName().startsWith(MAC_OS_X);
	}

	/**
	 * @return the system's default {@link Charset}
	 */
	public static Charset charset() {
		return Charset.forName(fileEncoding());
	}

}
