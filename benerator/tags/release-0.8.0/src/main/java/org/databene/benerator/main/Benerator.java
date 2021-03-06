/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.main;

import org.databene.benerator.BeneratorError;
import org.databene.benerator.BeneratorFactory;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.BeneratorMonitor;
import org.databene.benerator.engine.DescriptorRunner;
import org.databene.commons.ArrayUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.LogCategories;
import org.databene.commons.SystemInfo;
import org.databene.commons.VMInfo;
import org.databene.commons.log.LoggingInfoPrinter;
import org.databene.commons.ui.ConsoleInfoPrinter;
import org.databene.commons.ui.InfoPrinter;
import org.databene.commons.version.VersionInfo;
import org.databene.commons.version.VersionNumber;
import org.databene.contiperf.sensor.MemorySensor;
import org.databene.jdbacl.DBUtil;
import org.databene.profile.Profiling;
import org.databene.text.KiloFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * Parses and executes a benerator setup file.<br/>
 * <br/>
 * Created: 14.08.2007 19:14:28
 * @author Volker Bergmann
 */
public class Benerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Benerator.class);
	private static final Logger CONFIG_LOGGER = LoggerFactory.getLogger(LogCategories.CONFIG);

	// methods ---------------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws IOException {
		VersionInfo.getInfo("benerator").verifyDependencies();
		if (ArrayUtil.contains("--version", args) || ArrayUtil.contains("-v", args))
			printVersionInfoAndExit();
		else
			runFromCommandLine(args);
	}

	private static void runFromCommandLine(String[] args) throws IOException {
		try {
			InfoPrinter printer = new LoggingInfoPrinter(LogCategories.CONFIG);
			String filename = (args.length > 0 ? args[0] : "benerator.xml");
			runFile(filename, printer);
	    	DBUtil.assertAllDbResourcesClosed(false);
		} catch (BeneratorError e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(e.getCode());
		}
	}

	public static void runFile(String filename, InfoPrinter printer) throws IOException {
		BeneratorMonitor.INSTANCE.reset();
		MemorySensor memProfiler = MemorySensor.getInstance();
		memProfiler.reset();
		printer.printLines("Running file " + filename);
		checkSystem(printer);
		BeneratorContext context = BeneratorFactory.getInstance().createContext(IOUtil.getParentUri(filename));
		DescriptorRunner runner = new DescriptorRunner(filename, context);
		try {
			runner.run();
		} finally {
			IOUtil.close(runner);
		}
		CONFIG_LOGGER.info("Max. committed heap size: " + new KiloFormatter(1024).format(memProfiler.getMaxCommittedHeapSize()) + "B");
	}

	private static void printVersionInfoAndExit() {
		InfoPrinter console = new ConsoleInfoPrinter();
		printVersionInfo(console);
		System.exit(0);
	}

	private static void checkSystem(InfoPrinter printer) {
		printVersionInfo(printer);
		printer.printLines("Max heap size: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");
		try {
			Class.forName("javax.script.ScriptEngine");
		} catch (ClassNotFoundException e) {
			CONFIG_LOGGER.error("You need to run benerator with Java 6 or greater!");
			if (SystemInfo.isMacOsx())
				CONFIG_LOGGER.error("Please check the manual for Java setup on Mac OS X.");
			System.exit(-1);
		}
		VersionNumber javaVersion = VersionNumber.valueOf(VMInfo.getJavaVersion());
		if (javaVersion.compareTo(VersionNumber.valueOf("1.6")) < 0)
			CONFIG_LOGGER.warn("benerator is written for and tested under Java 6 - " +
					"you managed to set up JSR 223, but may face other problems.");
		if (Profiling.isEnabled())
			CONFIG_LOGGER.warn("Profiling is active. This may lead to memory issues");
	}

	private static void printVersionInfo(InfoPrinter printer) {
		VersionInfo version = VersionInfo.getInfo("benerator");
		printer.printLines(
			"Benerator " + version.getVersion() + " build " + version.getBuildNumber(),
			"Java version " + VMInfo.getJavaVersion(),
			"JVM " + VMInfo.getJavaVmName() + " " + VMInfo.getJavaVmVersion() + " (" + VMInfo.getJavaVmVendor() + ")",
			"OS " + SystemInfo.getOsName() + " " + SystemInfo.getOsVersion() + " (" + SystemInfo.getOsArchitecture() + ")"
		);
        listScriptEngines(printer);
    }

	private static void listScriptEngines(InfoPrinter printer) {
    	printer.printLines("Installed JSR 223 Script Engines:");
        for (ScriptEngineFactory engine : new ScriptEngineManager().getEngineFactories()) {
			printer.printLines("- " + engine.getEngineName() + engine.getNames());
        }
	}

}
