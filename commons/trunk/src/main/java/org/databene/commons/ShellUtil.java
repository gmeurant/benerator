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

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Executes a shell file.<br/>
 * <br/>
 * Created at 05.08.2008 07:40:00
 * @since 0.4.5
 * @author Volker Bergmann
 */
public class ShellUtil {
	
	public static void runShellTask(ReaderLineIterator iterator, ErrorHandler errorHandler) {
		while (iterator.hasNext()) {
			String command = iterator.next().trim();
			if (command.length() > 0)
				runShellTaskCommand(command, errorHandler);	
		}
    }

	public static void runShellTaskCommand(String command, ErrorHandler errorHandler) {
		try {
			String s = null;
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			// read the output from the command
			while ((s = stdIn.readLine()) != null) {
			    System.out.println(s);
			}
			// read any errors from the attempted command
			while ((s = stdErr.readLine()) != null) {
			    System.out.println(s);
			}
			p.waitFor();
			if (p.exitValue() != 0) {
				errorHandler.handleError("Process (" + command + ") did not terminate normally: Return code " + p.exitValue());
			}
		} catch (Exception e) {
			errorHandler.handleError("Error in shell invocation: " + command, e);
		}
	}

}
