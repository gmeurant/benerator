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

import org.databene.model.DocumentWriter;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * TODO.<br/>
 * <br/>
 * Created: 07.06.2007 11:32:09
 */
public class ScriptedDocumentWriter<E> implements DocumentWriter<E> {

    private Writer out;
    private Map<String, Object> vars;

    private Script headerScript;
    private Script bodyPartScript;
    private Script footerScript;

    private boolean headerWritten;

    public ScriptedDocumentWriter(Writer out, String headerScriptUrl, String bodyPartScriptUrl, String footerScriptUrl)
            throws IOException {
        this(   out,
                (headerScriptUrl != null ? Script.getInstance(headerScriptUrl) : null),
                (bodyPartScriptUrl != null ? Script.getInstance(bodyPartScriptUrl) : null),
                (footerScriptUrl != null ? Script.getInstance(footerScriptUrl) : null)
        );
    }

    public ScriptedDocumentWriter(Writer out, Script headerScript, Script bodyPartScript, Script footerScript) {
        this.out = out;
        this.headerScript = headerScript;
        this.bodyPartScript = bodyPartScript;
        this.footerScript = footerScript;
        this.vars = new HashMap<String, Object>();
        this.headerWritten = false;
    }

    public Script getHeaderScript() {
        return headerScript;
    }

    public void setHeaderScript(Script headerScript) {
        this.headerScript = headerScript;
    }

    public Script getFooterScript() {
        return footerScript;
    }

    public void setFooterScript(Script footerScript) {
        this.footerScript = footerScript;
    }

    // Script interface implementation ---------------------------------------------------------------------------------

    public void setVariable(String name, Object value) {
        vars.put(name, value);
    }

    public void writeElement(E part) throws IOException {
        if (!headerWritten) {
            writeHeader();
            headerWritten = true;
        }
        if (bodyPartScript != null) {
            try {
                bodyPartScript.setVariable("var", vars);
                bodyPartScript.setVariable("part", part);
                bodyPartScript.execute(out);
            } catch (ScriptException e) {
                throw new RuntimeException(e); // TODO handle exception
            }
        }
    }

    public void close() throws IOException {
        writeFooter();
        out.close();
    }

    // helpers ---------------------------------------------------------------------------------------------------------

    protected void writeHeader() throws IOException {
        if (headerScript != null) {
            try {
                headerScript.setVariable("var", vars);
                headerScript.execute(out);
            } catch (ScriptException e) {
                throw new RuntimeException(e); // TODO handle exception
            }
        }
    }

    protected void writeFooter() throws IOException {
        if (footerScript != null) {
            try {
                headerScript.setVariable("var", vars);
                footerScript.execute(out);
            } catch (ScriptException e) {
                throw new RuntimeException(e); // TODO handle exception
            }
        }
    }
}
