/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.regex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.databene.commons.CharSet;

/**
 * Represents a custom character class with inclusions and exclusions.<br/>
 * <br/>
 * Created at 20.09.2009 11:18:28
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class CustomCharClass {

	private List<?> included;
	private List<?> excluded;
	
	// constructors ----------------------------------------------------------------------------------------------------

	public CustomCharClass() {
		this(new ArrayList<Object>());
	}
	
    public CustomCharClass(List<?> included) {
	    this(included, new ArrayList<Object>());
    }

    public CustomCharClass(List<?> included, List<?> excluded) {
	    this.included = included;
	    this.excluded = excluded;
    }
    
    // properties ------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
    public List<Object> getIncluded() {
    	return (List<Object>) included;
    }

	public void setIncluded(List<Object> included) {
    	this.included = included;
    }

	@SuppressWarnings("unchecked")
    public List<Object> getExcluded() {
    	return (List<Object>) excluded;
    }

	public void setExcluded(List<Object> excluded) {
    	this.excluded = excluded;
    }
	
	public CharSet getCharSet() {
		CharSet result = new CharSet(this.toString(), new HashSet<Character>());
		for (Object incl : included)
			result.addAll(RegexParser.toSet(incl));
		for (Object excl : excluded)
			result.removeAll(RegexParser.toSet(excl));
		return result;
	}
	
	// java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((excluded == null) ? 0 : excluded.hashCode());
	    result = prime * result + ((included == null) ? 0 : included.hashCode());
	    return result;
    }

    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    CustomCharClass other = (CustomCharClass) obj;
	    if (excluded == null) {
		    if (other.excluded != null)
			    return false;
	    } else if (!excluded.equals(other.excluded))
		    return false;
	    if (included == null) {
		    if (other.included != null)
			    return false;
	    } else if (!included.equals(other.included))
		    return false;
	    return true;
    }

	/** @see java.lang.Object#toString() */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		appendToString(included, result);
		if (!excluded.isEmpty()) {
			result.append('^');
			appendToString(excluded, result);
		}
		return result.append(']').toString();
	}
	
	// private helpers -------------------------------------------------------------------------------------------------

    private void appendToString(List<?> objects, StringBuilder builder) {
	    for (Object object : objects)
	    	builder.append(object);
    }

}