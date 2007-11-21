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

package org.databene.regex;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Pattern that represents a set of Characters, e.g. '[a-z]'.<br/>
 * <br/>
 * Created: 18.08.2006 19:11:31
 */
public class CharSetPattern implements SubPattern {

    /** The character set that keeps the characters */
    private Set<Character> charSet;

    /** Constructor that initializes to the specified collection */
    public CharSetPattern(Collection<Character> charSet) {
        this.charSet = new HashSet<Character>();
        this.charSet.addAll(charSet);
    }

    /** Returns the represented character set */
    public Set<Character> getCharSet() {
        return charSet;
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final CharSetPattern that = (CharSetPattern) o;
        return charSet.equals(that.charSet);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return charSet.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder builder = new StringBuilder(charSet.size() + 2);
        for (Character character : charSet)
            builder.append(character);
        if (charSet.size() > 1)
            return '[' + builder.toString() + ']';
        else
            return builder.toString();
    }
}
