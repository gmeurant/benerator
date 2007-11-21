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

/**
 * Represents a quantifier of a regular expression, e.g. '{1,3}'.<br/>
 * <br/>
 */
public class Quantifier {

    /** minimum length */
    private int min;

    /** maximum length */
    private int max;

    /** Complete constructor that takes values for all attributes */
    public Quantifier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /** returns the minimum value */
    public int getMin() {
        return min;
    }

    /** returns the maximum value */
    public int getMax() {
        return max;
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    /**
     * Creates a String of regular expression format, e.g. '{2,}', '{1,3}', '+', '?'
     * @see java.lang.Object#equals(Object)
     */
    public String toString() {
        if (min == max)
            return (min == 1 ? "" : '{' + String.valueOf(min) + '}');
        else if (min == 0) {
            switch (max) {
                case 1 : return "?";
                case -1: return "*";
                default: return "{0," + max + '}';
            }
        } else if (min == 1)
            return (max == -1 ? "+" : "{1," + max + "}");
        else
            return (max == -1 ? "{" + min + ",}" : "{1," + "}");
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Quantifier that = (Quantifier) o;
        if (max != that.max)
            return false;
        return min == that.min;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public int hashCode() {
        return 29 * min + max;
    }
}
