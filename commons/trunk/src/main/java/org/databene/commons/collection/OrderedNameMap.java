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

import java.util.Map;

import org.databene.commons.NullSafeComparator;
import org.databene.commons.OrderedMap;

/**
 * A map that assigns names to Objects and keeps entries 
 * in the order in which they were inserted.<br/><br/>
 * Created at 14.04.2008 09:49:34
 * @since 0.5.2
 * @author Volker Bergmann
 */
public class OrderedNameMap<E> extends OrderedMap<String, E> {
	
	private static final int SENSITIVE = 0;
	private static final int INSENSITIVE = 1;
	private static final int IGNORANT = 2;
	
	private int caseSupport;
	
	// constructors / factory methods ----------------------------------------------------------------------------------
	
    public OrderedNameMap() {
		this(SENSITIVE);
	}
    
    public static <T> OrderedNameMap<T> createCaseSensitiveMap() {
    	return new OrderedNameMap<T>();
    }

    public static <T> OrderedNameMap<T> createCaseInsensitiveMap() {
    	return new OrderedNameMap<T>(INSENSITIVE);
    }

    public static <T> OrderedNameMap<T> createCaseIgnorantMap() {
    	return new OrderedNameMap<T>(IGNORANT);
    }

    private OrderedNameMap(int caseSupport) {
		this.caseSupport = caseSupport;
	}

    // Map interface implementation ------------------------------------------------------------------------------------
    
	public boolean containsKey(String key) {
        boolean result = super.containsKey(normalizeKey(key));
        if (result || caseSupport != INSENSITIVE)
        	return true;
        for (String tmp : super.keySet())
        	if (NullSafeComparator.equals(tmp, key))
        		return true;
		return result;
    }

	public E get(String key) {
        E result = super.get(normalizeKey(key));
        if (result != null || caseSupport != INSENSITIVE)
        	return result;
        for (Map.Entry<String, E> entry : super.entrySet())
        	if (entry.getKey().equalsIgnoreCase(key))
        		return entry.getValue();
		return result;
    }

    public E put(String key, E value) {
        return super.put(normalizeKey(key), value);
    }

    public E remove(String key) {
        E result = super.remove(normalizeKey(key));
        if (result != null || caseSupport != INSENSITIVE)
        	return result;
        for (Map.Entry<String, E> entry : super.entrySet())
        	if (NullSafeComparator.equals(entry.getKey(), key))
        		return super.remove(entry.getKey());
        return null;
    }

    // private helpers -------------------------------------------------------------------------------------------------
    
    private String normalizeKey(String key) {
		return (caseSupport == IGNORANT && key != null ? key.toLowerCase() : key);
	}
}
