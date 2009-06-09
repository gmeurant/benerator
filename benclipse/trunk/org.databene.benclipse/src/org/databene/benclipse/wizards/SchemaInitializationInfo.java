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

package org.databene.benclipse.wizards;

import java.io.File;

/**
 * Groups configurations for initializing a database schema.<br/>
 * <br/>
 * Created at 09.03.2009 17:41:55
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class SchemaInitializationInfo {

	private final File createScriptFile;
	private final File dropScriptFile;
	private final boolean dbUnitSnapshot;
	
	public SchemaInitializationInfo(File createScriptFile, File dropScriptFile, boolean dbUnitSnapshot) {
	    this.createScriptFile = createScriptFile;
	    this.dropScriptFile = dropScriptFile;
	    this.dbUnitSnapshot = dbUnitSnapshot;
    }

	public File getCreateScriptFile() {
    	return createScriptFile;
    }

	public File getDropScriptFile() {
    	return dropScriptFile;
    }

	public boolean isDbUnitSnapshot() {
    	return dbUnitSnapshot;
    }
	
}
