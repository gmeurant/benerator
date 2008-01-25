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

package org.databene.model.mutator;

import org.databene.model.Mutator;
import org.databene.model.UpdateFailedException;

/**
 * Mutator Proxy that converts non-int value to integer and passes them to the real Mutator.<br/>
 * <br/>
 * Created: 18.12.2005 21:05:59
 * @deprecated 
 */
public class IntMutator<C, V extends Object> extends MutatorProxy<C, V> {

    public IntMutator(Mutator<C, V> realMutator) {
        super(realMutator);
    }

    public void setValue(C target, V value) throws UpdateFailedException {
        Integer i;
        if (value == null)
            i = null;
        else if (value instanceof Number)
            i = new Integer(((Number) value).intValue());
        else if (value instanceof String)
            i = new Integer((String)value);
        else
            throw new IllegalArgumentException("Cannot convert " + value.getClass());
        realMutator.setValue(target, (V)i);
    }
}
