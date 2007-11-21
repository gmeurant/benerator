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

package org.databene.model.visitor;

import org.databene.commons.ComparableComparator;
import org.databene.model.Visitor;
import org.databene.model.Element;

import java.util.Comparator;

/**
 * TODO.<br/>
 * <br/>
 * Created: 04.02.2007 08:50:13
 */
public class ExtremeElementFinder<E> {

    public static <E> E findMax(Element<E> root) {
        return findMax(root, (Comparator<E>) new ComparableComparator());
    }

    public static <E> E findMax(Element<E> root, Comparator<E> comparator) {
        return findExtreme(root, comparator, 1);
    }

    public static <E> E findMin(Element<E> root) {
        return findMin(root, (Comparator<E>) new ComparableComparator());
    }

    public static <E> E findMin(Element<E> root, Comparator<E> comparator) {
        return findExtreme(root, comparator, -1);
    }

    private static <E> E findExtreme(Element<E> root, Comparator<E> comparator, int extreme) {
        ExtremeVisitor<E> visitor = new ExtremeVisitor<E>(comparator, extreme);
        root.accept(visitor);
        return visitor.extremeElement;
    }

    private static final class ExtremeVisitor<E> implements Visitor<E> {

        private Comparator<E> comparator;
        private int extreme;
        private E extremeElement;

        public ExtremeVisitor(Comparator<E> comparator, int extreme) {
            this.comparator = comparator;
            this.extreme = extreme;
            this.extremeElement = null;
        }

        public void visit(E element) {
            if (extremeElement == null || comparator.compare(element, extremeElement) == extreme)
                extremeElement = element;
        }
    }
}
