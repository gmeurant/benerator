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

package org.databene.model.filter;

import org.databene.model.Filter;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO.<br/>
 * <br/>
 * Created: 10.04.2007 08:07:12
 */
public class Splitter {

    public static <T> SplitResult<T> split(T[] items, Filter<T> filter) {
        List<T> matches = new ArrayList<T>();
        List<T> mismatches = new ArrayList<T>();
        for (T item : items) {
            if (filter.accept(item))
                matches.add(item);
            else
                mismatches.add(item);
        }
        return new SplitResult<T>(matches, mismatches);
    }

    public static <T> List<List<T>> filter(T[] items, Filter<T> ... filters) {
        List<List<T>> results = new ArrayList<List<T>>(filters.length);
        for (int i = 0; i < filters.length; i++)
            results.add(new ArrayList<T>());
        for (T item : items) {
            for (int i = 0; i < filters.length; i++) {
                Filter<T> filter = filters[i];
                if (filter.accept(item))
                    results.get(i).add(item);
            }
        }
        return results;
    }
}
