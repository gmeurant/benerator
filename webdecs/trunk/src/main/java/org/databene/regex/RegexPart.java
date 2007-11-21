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

/**
 * Represents a regular expression part composed of a SubPattern and a Quantifier.<br/>
 * <br/>
 * Created: 18.08.2006 19:11:31
 */
public class RegexPart {

    /** The sub pattern */
    private SubPattern pattern;

    /** The quantifier */
    private Quantifier quantifier;

    // constructors ----------------------------------------------------------------------------------------------------

    public RegexPart(SubPattern pattern) {
        this(pattern, new Quantifier(1, 1));
    }

    public RegexPart(SubPattern pattern, Quantifier quantifier) {
        this.pattern = pattern;
        this.quantifier = quantifier;
    }
/*
    public RegexPart(Set<Character> charSet) {
        this(charSet, new Quantifier(1, 1));
    }
*/
    public RegexPart(Set<Character> charSet, Quantifier quantifier) {
        this.pattern = new CharSetPattern(charSet);
        this.quantifier = quantifier;
    }

    // properties ------------------------------------------------------------------------------------------------------

    /** Returns the represented sub pattern */
    public SubPattern getPattern() {
        return pattern;
    }

    /** Returns the represented quantifier */
    public Quantifier getQuantifier() {
        return quantifier;
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
        final RegexPart regexPart = (RegexPart) o;
        if (!pattern.equals(regexPart.pattern))
            return false;
        return quantifier.equals(regexPart.quantifier);
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public int hashCode() {
        return 29 * pattern.hashCode() + quantifier.hashCode();
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public String toString() {
        return pattern.toString() + quantifier.toString();
    }
}
