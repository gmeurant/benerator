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

package org.databene.commons.comparator;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

/**
 * Comparator that compares objects by its Java type with a predefined order.<br/>
 * <br/>
 * Created: 22.05.2007 18:19:54
 * @author Volker Bergmann
 */
public class ObjectTypeComparator implements Comparator<Object> {

    private Map<Class<?>, Integer> indexes;

    public ObjectTypeComparator(Class<?> ... orderedClasses) {
        indexes = new HashMap<Class<?>, Integer>();
        int count = 0;
        for (Class<?> type : orderedClasses)
            indexes.put(type, ++count);
    }

    public int compare(Object o1, Object o2) {
        Class<?> c1 = o1.getClass();
        int i1 = indexOfClass(c1);
        Class<?> c2 = o2.getClass();
        int i2 = indexOfClass(c2);
        return IntComparator.compare(i1, i2);
    }

    private int indexOfClass(Class<?> type) {
        return indexes.get(type);
    }
    
}
