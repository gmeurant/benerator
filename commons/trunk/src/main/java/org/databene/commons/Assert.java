/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

import java.util.Arrays;

/**
 * An assertion utility.<br/><br/>
 * Created at 25.04.2008 17:59:43
 * @since 0.4.2
 * @author Volker Bergmann
 */
public class Assert {
	
	private Assert() {}
	
	public  void end(String text, String end) {
		if (text == null) {
			if (end != null)
				throw new AssertionError("String is supposed to end with '" + end + ", but is null.");
		} else if (!text.endsWith(end))
			throw new AssertionError("String is supposed to end with '" + end + ", but is: " + text);
	}

	public static void endIgnoreCase(String text, String end) {
		if (text == null) {
			if (end != null)
				throw new AssertionError("String is supposed to end with '" + end + ", but is null.");
		} else if (!text.endsWith(end))
			throw new AssertionError("String is supposed to case-insensitively end with '" + end 
					+ ", but is: " + text);
	}

	public static void notNull(Object object, String objectRole) {
		if (object == null)
			throw new AssertionError(objectRole + " is not supposed to be null");
	}
	
	public static <T> void equals(T[] a1, T[] a2) {
		if (a1 == null && a2 == null)
			return;
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			throw new AssertionError("Arrays are not equal, one of them is null");
		if (a1.length != a2.length)
			throw new AssertionError("Arrays are not equal, the size differs: [" + 
					ArrayFormat.format(a1) + "] vs. [" + ArrayFormat.format(a2) + ']');
		if (!Arrays.deepEquals(a1, a2))
			throw new AssertionError("Arrays are not equal, content differs: [" + 
					ArrayFormat.format(a1) + "] vs. [" + ArrayFormat.format(a2) + ']');
	}
	
	public static void equals(byte[] a1, byte[] a2) {
		if (a1 == null && a2 == null)
			return;
		if ((a1 == null && a2 != null) || (a1 != null && a2 == null))
			throw new AssertionError("Arrays are not equal, one of them is null");
		if (a1.length != a2.length)
			throw new AssertionError("Arrays are not equal, the size differs: [" + 
					ArrayFormat.formatBytes(",", a1) + "] vs. [" + ArrayFormat.formatBytes(",", a2) + ']');
		if (!Arrays.equals(a1, a2))
			throw new AssertionError("Arrays are not equal, content differs: [" + 
					ArrayFormat.formatBytes(",", a1) + "] vs. [" + ArrayFormat.formatBytes(",", a2) + ']');
	}

	public static void length(String string, int length) {
		if (string == null || string.length() != length)
			throw new AssertionError("Unexpected string length: Expected " + length + ", found: " + string.length());
	}
}
