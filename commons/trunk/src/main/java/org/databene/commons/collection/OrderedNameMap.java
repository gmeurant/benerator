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

package org.databene.commons.collection;

import org.databene.commons.OrderedMap;

/**
 * A OrderedNameMap.<br/><br/>
 * Created at 14.04.2008 09:49:34
 * @since 0.5.2
 * @author Volker Bergmann
 *
 */
public class OrderedNameMap<E> extends OrderedMap<String, E> {
	
	private boolean caseSensitive;
	
    public OrderedNameMap() {
		this(true);
	}

    public OrderedNameMap(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean containsKey(String key) {
        return super.containsKey(normalizeKey(key));
    }

	public E get(String key) {
        return super.get(normalizeKey(key));
    }

    public E put(String key, E value) {
        return super.put(key, value);
    }

    public E remove(String key) {
        return super.remove(normalizeKey(key));
    }

    private String normalizeKey(String key) {
		return (caseSensitive || key != null ? key : key.toLowerCase());
	}

}
