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

package org.databene.model.accessor;

import org.databene.model.Accessor;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO.<br/>
 * <br/>
 * Created: 11.03.2006 12:45:26
 */
public class AccessorMapAccessor implements DependentAccessor {

    private static final List EMPTY_LIST = new ArrayList();

    private Map map;
    private Object key;

    public AccessorMapAccessor(Map map, Object key) {
        this.map = map;
        this.key = key;
    }

    // interface -------------------------------------------------------------------------------------------------------

    public Object getKey() {
        return key;
    }

    public Object getValue(Object item) {
        Accessor accessor = getAccessor();
        if (accessor == null)
            throw new IllegalStateException("Key not found: " + key);
        return accessor.getValue(item);
    }

    public List getDependencies() {
        Accessor accessor = getAccessor();
        if (accessor instanceof DependentAccessor)
            return ((DependentAccessor)accessor).getDependencies();
        else
            return EMPTY_LIST;
    }

    public Accessor getAccessor() {
        return (Accessor) map.get(key);
    }
}
