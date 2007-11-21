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

package org.databene.script.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.databene.script.Script;
import org.databene.script.ScriptException;

/**
 * TODO.<br/>
 * <br/>
 * Created: 31.01.2007 19:56:20
 */
public class FreeMarkerScript extends Script {

    private static Configuration cfg;

    static {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(FreeMarkerScript.class, "/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    private Template template;
    private Map<String, Object> root;

    public FreeMarkerScript(String filename) throws IOException {
        template = cfg.getTemplate(filename);
        root = new HashMap<String, Object>();
    }

    public void setVariable(String variableName, Object value) {
        root.put(variableName, value);
    }

    public void execute(Writer out) throws IOException, ScriptException {
        try {
            template.process(root, out);
        } catch (TemplateException e) {
            throw new ScriptException(e);
        }
    }
}
