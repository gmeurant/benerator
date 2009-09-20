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

import org.databene.script.AbstractScript;
import org.databene.script.ScriptException;
import org.databene.commons.Accessor;
import org.databene.commons.Context;
import org.databene.commons.IOUtil;

import java.io.Writer;
import java.io.IOException;

/**
 * Experimental high-performance implementation of the script interface,
 * restricted to template-like expressions.<br/>
 * <br/>
 * Created: 12.06.2007 17:26:32
 * @author Volker Bergmann
 */
@SuppressWarnings("unchecked")
public class QuickScript extends AbstractScript {
    
    private Accessor[] accessors;

    public QuickScript(String filename) throws IOException {
        this(QuickScriptTokenizer.tokenize(IOUtil.getContentOfURI(filename)));
    }

    private QuickScript(Accessor[] accessors) {
        this.accessors = accessors;
    }

    @Override
    public void execute(Context context, Writer out) throws IOException, ScriptException {
        for (Accessor<Context, ? extends Object> accessor : this.accessors)
            out.write(String.valueOf(accessor.getValue(context)));
    }

    public static QuickScript parseText(String text) {
        return new QuickScript(QuickScriptTokenizer.tokenize(text));
    }
    
}
