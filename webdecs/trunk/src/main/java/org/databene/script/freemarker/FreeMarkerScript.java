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

import java.io.StringReader;
import java.io.Writer;
import java.io.IOException;

import org.databene.commons.Context;
import org.databene.script.AbstractScript;
import org.databene.script.Script;
import org.databene.script.ScriptException;

/**
 * {@link Script} implementation that uses the FreeMarker engine.<br/>
 * <br/>
 * Created: 31.01.2007 19:56:20
 * @author Volker Bergmann
 */
public class FreeMarkerScript extends AbstractScript {
    
    private static Configuration cfg;

    static {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(FreeMarkerScript.class, "/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setNumberFormat("0.##");
    }

    private Template template;

    // constructors ----------------------------------------------------------------------------------------------------
    
    public FreeMarkerScript(String filename) throws IOException {
        this(cfg.getTemplate(filename));
    }

    public FreeMarkerScript(Template template) throws IOException {
        this.template = template;
    }
    
    // factory methods -------------------------------------------------------------------------------------------------
    
    public static FreeMarkerScript createFromText(String text) {
        try {
            StringReader reader = new StringReader(text);
            Template template = new Template(text, reader, cfg, null);
            return new FreeMarkerScript(template);
        } catch (IOException e) {
            throw new RuntimeException(e); // This is not supposed to happen
        }
    }
    
    // Script interface implementation ---------------------------------------------------------------------------------

    public void execute(Context context, Writer out) throws IOException, ScriptException {
        try {
            template.process(context, out);
        } catch (TemplateException e) {
            throw new ScriptException(e);
        }
    }
    
    // java.lang.Object overrides --------------------------------------------------------------------------------------
    
    @Override
    public String toString() {
        return template.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return template.hashCode();
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object obj) {
        return template.equals(obj);
    }
    
}
