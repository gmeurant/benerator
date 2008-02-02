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

package org.databene.script.quickscript;

import junit.framework.TestCase;

import java.io.StringWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.databene.commons.Context;
import org.databene.commons.context.DefaultContext;
import org.databene.script.ScriptException;
import org.databene.script.Script;

/**
 * Created: 12.06.2007 17:36:30
 */
public class QuickScriptTest extends TestCase {
    
    private Context context;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new DefaultContext();
        context.set("var", "XYZ");
    }

    public void testPlainText() throws IOException, ScriptException {
        QuickScript script = QuickScript.parseText("Test");
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("Test", writer.toString());
    }

    public void testSimpleVariable() throws IOException, ScriptException {
        QuickScript script = QuickScript.parseText("Test${var}Test");
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("TestXYZTest", writer.toString());
    }

    public void testGraphVariable() throws IOException, ScriptException {
        QuickScript script = QuickScript.parseText("Test${var.class.simpleName}Test");
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("TestStringTest", writer.toString());
    }

    public void testMapVariable() throws IOException, ScriptException {
        QuickScript script = QuickScript.parseText("Test${map.dings}Test");
        Map<String, String> map = new HashMap<String, String>();
        map.put("dings", "XYZ");
        context.set("map", map);
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("TestXYZTest", writer.toString());
    }

    public void testScriptGetInstance() throws IOException, ScriptException {
        Script script = new QuickScriptFactory().readFile("org/databene/script/quickscript/test.qsc");
        context.set("var_dings", "XYZ");
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("TestXYZTest", writer.toString());
    }
  
}
