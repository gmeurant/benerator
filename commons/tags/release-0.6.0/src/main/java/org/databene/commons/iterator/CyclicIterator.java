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

package org.databene.commons.iterator;

/**
 * Iterates through another BidirectionalIterator repeatedly. 
 * This is supported forward as well as backward.<br/>
 * <br/>
 * Created: 12.05.2007 23:21:48
 * @author Volker Bergmann
 */
public class CyclicIterator<E> extends IteratorProxy<E> {

    private boolean cyclic;

    public CyclicIterator(BidirectionalIterator<E> realIterator) {
        super(realIterator);
        this.cyclic = true;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    @Override
    public boolean hasPrevious() {
        return (cyclic || super.hasPrevious());
    }

    @Override
    public boolean hasNext() {
        return (cyclic || super.hasNext());
    }

    @Override
    public E previous() {
        if (super.hasPrevious())
            return super.previous();
        else if (cyclic)
            return super.last();
        else
            throw new IllegalStateException("No element available for previous()");
    }

    @Override
    public E next() {
        if (super.hasNext())
            return super.next();
        else if (cyclic)
            return super.first();
        else
            throw new IllegalStateException("No element available for next()");
    }
}
