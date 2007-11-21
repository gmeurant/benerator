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

package org.databene.model.accessor;

import org.databene.model.Accessor;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO.<br/>
 * <br/>
 * Created: 06.03.2006 17:09:30
 */
public class ConditionalAccessor implements DependentAccessor {

    private List options;

    // constructors ----------------------------------------------------------------------------------------------------

    public ConditionalAccessor() {
        this(new ArrayList());
    }

    public ConditionalAccessor(Accessor condition, Accessor trueAccessor, Accessor falseAccessor) {
        this(createSingleBranch(condition, trueAccessor, falseAccessor));
    }

    public ConditionalAccessor(List options) {
        this.options = options;
    }

    // interface -------------------------------------------------------------------------------------------------------

    public Object getValue(Object item) {
        for (int i = 0; i < options.size(); i++) {
            AccessorOption option = (AccessorOption) options.get(i);
            if (option.accept(item))
                return option.getValue(item);
        }
        return null; // no option was applicable
    }

    public List getDependencies() {
        List dependencies = new ArrayList();
        for (int i = 0; i < options.size(); i++) {
            AccessorOption accessorOption = (AccessorOption) options.get(i);
            List list = accessorOption.getDependencies();
            for (int j = 0; j < list.size(); j++) {
                Accessor accessor = (Accessor) list.get(j);
                if (!dependencies.contains(accessor))
                    dependencies.add(accessor);
            }
        }
        return dependencies;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final ConditionalAccessor that = (ConditionalAccessor) o;
        return this.options.equals(that.options);
    }

    public int hashCode() {
        return options.hashCode();
    }

    // implementation --------------------------------------------------------------------------------------------------

    private static List createSingleBranch(Accessor condition, Accessor trueAccessor, Accessor falseAccessor) {
        List list = new ArrayList(2);
        list.add(new AccessorOption(condition, trueAccessor));
        list.add(new AccessorOption(new NotAccessor(condition), falseAccessor));
        return list;
    }


}
