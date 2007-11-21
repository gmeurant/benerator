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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.model.Accessor;
import org.databene.model.Context;
import org.databene.model.Composite;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * TODO.<br/>
 * <br/>
 * Created: 12.06.2007 18:36:11
 */
public class FeatureAccessor implements Accessor {

    private String featureName;

    public FeatureAccessor(String featureName) {
        this.featureName = featureName;
    }

    public Object getValue(Object o) {
        if (o == null)
            return null;
        PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(o.getClass(), featureName);
        if (propertyDescriptor != null) {
            try {
                return propertyDescriptor.getReadMethod().invoke(o);
            } catch (Exception e) {
                throw new ConfigurationError("Unable to access feature '" + featureName + "'", e);
            }
        }
        else if (o instanceof Map)
            return ((Map)o).get(featureName);
        else if (o instanceof Context)
            return ((Context)o).get(featureName);
        else if (o instanceof Composite)
            return ((Composite)o).getComponent(featureName);
        else
            return null;
    }

    public String toString() {
        return getClass().getSimpleName() + '[' + featureName + ']';
    }
}
