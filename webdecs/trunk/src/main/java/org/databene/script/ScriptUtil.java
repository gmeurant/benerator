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

package org.databene.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Context;
import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.commons.StringCharacterIterator;
import org.databene.script.jsr227.Jsr223ScriptFactory;

/**
 * Utility class for scripting.<br/><br/>
 * @since 0.3.0
 * @author Volker Bergmann
 */
public class ScriptUtil {
    
    private static final Log logger = LogFactory.getLog(ScriptUtil.class);

    // extension mapping -----------------------------------------------------------------------------------------------
    
	private static String defaultScriptEngine = "ftl";

	private static Map<String, ScriptFactory> factories;

    private static final String SETUP_FILE_NAME = "org/databene/script/script.properties";

    static {
        parseConfigFile();
    }
    
    // utility methods -------------------------------------------------------------------------------------------------

    public static Object execute(Script script, Context context) {
    	return script.evaluate(context);
    }

    /*    
    public static void execute(String filename, Writer out,
            String variableName, Object variableValue) throws IOException,
            ScriptException {
        Context context = new DefaultContext();
        context.set(variableName, variableValue);
        execute(filename, context, out);
    }

    public static void execute(String filename, Context context, Writer out)
            throws IOException, ScriptException {
        Script script = getInstance(filename);
        script.execute(context, out);
    }

    public static void execute(String filename, Writer out,
            ScriptVariable... variables) throws IOException, ScriptException {
        Script script = getInstance(filename);
        Context context = new DefaultContext();
        for (ScriptVariable variable : variables)
            context.set(variable.getName(), variable.getValue());
        script.execute(context, out);
    }
*/
    public static Object render(String text, Context context) {
		if (text.startsWith("{{") && text.endsWith("}}"))
            return text.substring(1, text.length() - 1);
        else if (text.startsWith("{") && text.endsWith("}")) {
            Script script = parseUnspecificText(text);
            return execute(script, context);
        } else
        	return text;
    }
    
	// static factory methods ------------------------------------------------------------------------------------------

    private static Map<String, Script> scriptsByName = new WeakHashMap<String, Script>();

    public static Script readFile(String uri) throws IOException {
        Script script = scriptsByName.get(uri);
        if (script == null) {
            String engineId = FileUtil.suffix(uri);
            ScriptFactory factory = getFactory(engineId);
            script = factory.readFile(uri);
            scriptsByName.put(uri, script);
        }
        return script;
    }

    public static Script parseSpecificText(String text, String engineId) {
        if (engineId == null)
            throw new IllegalArgumentException("engineId is null");
        ScriptFactory factory = getFactory(engineId);
        if (factory != null)
            return factory.parseText(text);
        else
            return new ConstantScript(text);
    }
    
    public static Script parseUnspecificText(String text) {
        if (text.startsWith("{") && text.endsWith("}")) {
            text = text.substring(1, text.length() - 1);
            Script script = null;
            StringCharacterIterator iterator = new StringCharacterIterator(text);
            String engineId = iterator.parseLetters();
            iterator.skipWhitespace();
            if (iterator.next() == ':') {
                if (getFactory(engineId) != null) {
                    String scriptText = iterator.remainingText();
                    script = parseSpecificText(scriptText, engineId);
                } else
                    script = parseSpecificText(text, ScriptUtil.getDefaultScriptEngine());
            } else
                script = parseSpecificText(text, ScriptUtil.getDefaultScriptEngine());
            return script;
        }
        return new ConstantScript(text);
        
    }

	private static ScriptFactory getFactory(String engineId) {
		return factories.get(engineId);
	}
    
    // private helpers -------------------------------------------------------------------------------------------------
    
    private static void parseConfigFile() {
        String className = null;
        try {
            factories = new HashMap<String, ScriptFactory>();
            
            // check installed JSR 223 script engines
            ScriptEngineManager mgr = new ScriptEngineManager();
            for (ScriptEngineFactory engineFactory : mgr.getEngineFactories()) {
        		Jsr223ScriptFactory factory = new Jsr223ScriptFactory(engineFactory.getScriptEngine());
            	List<String> names = engineFactory.getNames();
				for (String name : names) {
					factories.put(name, factory);
            	}
            }

            // read config file
            logger.info("Initializing Script mapping from file " + SETUP_FILE_NAME);
            Map<String, String> properties = IOUtil.readProperties(SETUP_FILE_NAME);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                className = entry.getValue().toString();
                ScriptFactory factory = (ScriptFactory) BeanUtil.newInstance(className);
                factories.put(entry.getKey().toString(), factory);
            }
        } catch (FileNotFoundException e) {
            throw new ConfigurationError("Setup file not found: " + SETUP_FILE_NAME, e);
        } catch (IOException e) {
            throw new ConfigurationError("I/O Error while reading file: " + SETUP_FILE_NAME, e);
        }
    }

	public static String getDefaultScriptEngine() {
		return defaultScriptEngine;
	}

	public static void setDefaultScriptEngine(String defaultScriptEngine) {
		ScriptUtil.defaultScriptEngine = defaultScriptEngine;
	}

}
