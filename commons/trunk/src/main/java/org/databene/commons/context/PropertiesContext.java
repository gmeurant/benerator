/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

import java.util.Map;
import java.util.Properties;

import org.databene.commons.Context;
import org.databene.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps entries of the form x.y to nested Maps.
 * @since 0.3.0
 * @author Volker Bergmann
 */
public class PropertiesContext extends DefaultContext {
    
    private static Logger logger = LoggerFactory.getLogger(PropertiesContext.class);
    
    // constructors ----------------------------------------------------------------------------------------------------

    public PropertiesContext(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet())
            set((String) entry.getKey(), entry.getValue());
    }

    public PropertiesContext(Map<String, ? extends Object> map) {
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            set(name, value);
        }
    }
    
    // Context interface -----------------------------------------------------------------------------------------------

    @Override
	public void set(String name, Object value) {
    	// 1. use fully qualified name as key
        super.set(name, value);
        // and 2. compose recursive Context structures of path components
        String[] tokens = StringUtil.tokenize(name, '.');
        if (tokens.length > 1) {
            Context node = this;
            for (int i = 0; i < tokens.length - 1; i++) {
                String token = tokens[i];
                Object childObject = node.get(token);
                if (childObject != null && !(childObject instanceof Context)) {
                	 // TODO v0.6.0 find a better way for the following:
                	if (!"java.vendor.url.bug".equals(name) && !("java.vendor.url").equals(name)) 
                		logger.warn("ignoring entry " + name + "=" + value);
                    continue;
                }
                Context childContext = (Context)childObject;
                if (childContext == null) {
                    childContext = new DefaultContext();
                    node.set(token, childContext);
                }
                node = childContext;
            }
            node.set(tokens[tokens.length - 1], value);
        }
    }
    
}
