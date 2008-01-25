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

package org.databene.model.comparator;

import org.databene.commons.ComparableComparator;
import org.databene.commons.NullSafeComparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.text.Collator;

/**
 * Creates comparators by the type of the objects to be compared.<br/>
 * <br/>
 * Created: 22.10.2005 21:29:08
 */
public class ComparatorFactory {

    private static Map comparators;

    static {
        comparators = new HashMap();
        addComparator(String.class, Collator.getInstance());
        // TODO v0.3 configure by file
        
        // this is the fallback if no specific Comparator was found
        addComparator(Comparable.class, new ComparableComparator());
    }

    public static void addComparator(Class type, Comparator comparator) {
        comparators.put(type, comparator);
    }

    public static <T> Comparator<T> getComparator(Class<T> type) {
        Comparator comparator = (Comparator) comparators.get(type);
        if (comparator == null && Comparable.class.isAssignableFrom(type))
            comparator = new ComparableComparator();
        if (comparator == null)
            throw new RuntimeException("No Comparator defined for " + type.getName());
        return new NullSafeComparator<T>(comparator);
    }

}
