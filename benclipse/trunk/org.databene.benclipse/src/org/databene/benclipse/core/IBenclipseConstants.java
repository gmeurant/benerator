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

package org.databene.benclipse.core;

import org.databene.benerator.main.Benerator;

/**
 * Provides common constants for Benclipse.<br/>
 * <br/>
 * Created at 05.02.2009 18:59:13
 * @since 0.5.9
 * @author Volker Bergmann
 */

public interface IBenclipseConstants {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.databene.benclipse";
	
	public static final String CONTAINER_ID = PLUGIN_ID + ".BENCLIPSE_CLASSPATH_CONTAINER";


    public static final String MIN_JVM_REQUIRED = "1.6";

	public static final String PREF_HSQL = "hsqlPreference";
	public static final String PREF_MAIN_MENU = "mainMenuPreference";
	public static final String PREF_SNAPSHOT_DIR = "snapshotDirPreference";
	
	public static final String HSQL_ACTION_SET = "org.databene.benclipse.hsqlActionSet";
	public static final String BENERATOR_ACTION_SET = "org.databene.benclipse.beneratorActionSet";


	String BENERATOR_XML = "benerator.xml";
	String BENERATOR_SUFFIX = ".ben.xml";

	public static final String BENERATOR_MAIN_CLASS = Benerator.class.getName();
	public static final String DESCRIPTOR_LAUNCH_CONFIG_TYPE_ID = "org.databene.benclipse.launch.descriptor";
	
	public static final String ATTR_FILENAME = "org.databene.benclipse.launch.filename";
	public static final String ATTR_VALIDATE = "org.databene.benclipse.launch.validate";
	public static final String ATTR_OPTIMIZE = "org.databene.benclipse.launch.optimize";
	public static final String ATTR_PROPERTIES = "org.databene.benclipse.launch.properties";
	public static final String ATTR_OPEN_GEN_FILES = "org.databene.benclipse.launch.open.generated.files";


}
