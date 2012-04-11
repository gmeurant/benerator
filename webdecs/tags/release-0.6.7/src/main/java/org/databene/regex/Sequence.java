/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

import java.util.Arrays;

/**
 * Represents a sequence of regular expression factors.
 * <br/>
 * @see Factor 
 */
public class Sequence {

    /** The represented sequence of regular expression factors */
    private Object[] factors;

    // constructors ----------------------------------------------------------------------------------------------------

    public Sequence(Object ... factors) {
        this.factors = factors;
    }

    public Object[] getFactors() {
        return factors;
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Sequence regex = (Sequence) o;
        return Arrays.equals(factors, regex.factors);
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(factors);
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Object factor : factors)
            buffer.append(factor);
        return buffer.toString();
    }
    
}
