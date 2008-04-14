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

import java.util.List;
import java.util.Arrays;

/**
 * Represents an alternative expression part of a regular expression, e.g. '(yes|no)'.<r/>
 * <br/>
 * Created: 17.09.2006 16:14:14
 */
public class AlternativePattern implements SubPattern {

    /** The alternatives */
    private Regex[] patterns;

    // constructors ----------------------------------------------------------------------------------------------------

    /** Constructor that takes a list of alternative patterns */
    public AlternativePattern(List<Regex> patterns) {
        Regex[] ra = new Regex[patterns.size()];
        this.patterns = patterns.toArray(ra);
    }

    /** Constructor that takes an array of alternative patterns */
    public AlternativePattern(Regex ... patterns) {
        this.patterns = patterns;
    }

    // properties ------------------------------------------------------------------------------------------------------

    /** Returns the alternative patterns */
    public Regex[] getPatterns() {
        return patterns;
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return Arrays.equals(patterns, ((AlternativePattern)o).patterns);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return Arrays.hashCode(patterns);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder("(");
        for (int i = 0; i < patterns.length; i++) {
            if (i > 0)
                buffer.append('|');
            buffer.append(patterns[i]);
        }
        buffer.append(')');
        return buffer.toString();
    }

}
