/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.ExceptionMapper;
import org.databene.commons.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.util.WeakHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO.<br/>
 * <br/>
 * Created: 03.02.2007 11:50:27
 */
public abstract class Script {

    private static final Log logger = LogFactory.getLog(Script.class);

    private static Map<String, String> classNames;
    private static Map<String, Constructor<? extends Script>> constructors;

    private static final String SETUP_FILE_NAME = "org/databene/script/script.properties";

    static {
        String className = null;
        try {
            classNames = new HashMap<String, String>();
            constructors = new HashMap<String, Constructor<? extends Script>>();
            logger.info("Initializing Script mapping from file " + SETUP_FILE_NAME);
            Properties properties = IOUtil.readProperties(SETUP_FILE_NAME);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                className = entry.getValue().toString();
                classNames.put(entry.getKey().toString(), className);
            }
        } catch (FileNotFoundException e) {
            throw new ConfigurationError("Setup file not found: " + SETUP_FILE_NAME, e);
        } catch (IOException e) {
            throw new ConfigurationError("I/O Error while reading file: " + SETUP_FILE_NAME, e);
        }
    }

    private static Map<String, Script> scripts = new WeakHashMap<String, Script>();

    public static Script getInstance(String filename) throws IOException {
        Script instance = scripts.get(filename);
        if (instance == null) {
            Constructor<? extends Script> constructor = null;
            try {
                constructor = getConstructor(FileUtil.suffix(filename));
                instance = constructor.newInstance(filename);
                scripts.put(filename, instance);
            } catch (InstantiationException e) {
                throw ExceptionMapper.configurationException(e, constructor);
            } catch (IllegalAccessException e) {
                throw ExceptionMapper.configurationException(e, constructor);
            } catch (InvocationTargetException e) {
                throw ExceptionMapper.configurationException(e, constructor);
            }
        }
        return instance;
    }

    public static void execute(String filename, Writer out, String variableName, Object variableValue)
            throws IOException, ScriptException {
        execute(filename, out, new ScriptVariable(variableName, variableValue));
    }

    public static void execute(String filename, Writer out, ScriptVariable ... variables)
            throws IOException, ScriptException {
        Script script = getInstance(filename);
        for (ScriptVariable variable : variables)
            script.setVariable(variable.getName(), variable.getValue());
        script.execute(out);
    }

    private static Constructor<? extends Script> getConstructor(String suffix) {
        Constructor<? extends Script> constructor = constructors.get(suffix);
        if (constructor == null) {
            String className = classNames.get(suffix);
            Class<? extends Script> scriptClass = null;
            try {
                scriptClass = (Class<? extends Script>) Class.forName(className);
                constructor = scriptClass.getConstructor(String.class);
                constructors.put(suffix, constructor);
            } catch (NoClassDefFoundError e) {
                throw new ConfigurationError("Script class not found: " + className + "." +
                        " MAke sure that the scripting libraries are in the class path.", e);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationError("Script class not found: " + className + "." +
                        " Make sure that the scripting libraries are in the class path.", e);
            } catch (NoSuchMethodException e) {
                throw new ConfigurationError("No String-arg constructor found in " + scriptClass);
            }
        }
        return constructor;
    }

    public abstract void setVariable(String variableName, Object variableValue);

    public abstract void execute(Writer out) throws IOException, ScriptException;

}
