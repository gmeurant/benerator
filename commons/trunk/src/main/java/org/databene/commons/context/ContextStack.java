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

package org.databene.commons.context;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.commons.Context;

/**
 * A Stack of Contexts, querying for items from top to bottom, 
 * setting and removing items only on the top.
 * @since 0.3.0
 * @author Volker Bergmann
 */
public class ContextStack implements Context {
    
    private static final Log logger = LogFactory.getLog(ContextStack.class);

    protected Stack<Context> contexts;
    
    public ContextStack(Context ... contexts) {
        super();
        this.contexts = new Stack<Context>();
        for (Context c : contexts) 
            this.contexts.push(c);
    }

    public synchronized Object get(String key) {
        for (int i = contexts.size() - 1; i >= 0; i--) {
            Context c = contexts.get(i);
            Object result = c.get(key);
            if (result != null)
                return result;
        }
        return null;
    }

    public synchronized Set<String> keySet() {
        Set<String> keySet = new HashSet<String>();
        for (int i = contexts.size() - 1; i >= 0; i++) {
            Context c = contexts.get(i);
            keySet.addAll(c.keySet());
        }
        return keySet;
    }

    public synchronized void remove(String key) {
        if (contexts.size() > 0)
            contexts.peek().remove(key);
    }

    public synchronized void set(String key, Object value) {
        if (contexts.size() > 0)
            contexts.peek().set(key, value);
        else
            logger.warn("ContextStack is empty, ignoring element: " + key);
    }

    public synchronized void push(Context context) {
        this.contexts.push(context);
    }
    
    public synchronized Context pop() {
        return this.contexts.pop();
    }
}
