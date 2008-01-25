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

package org.databene.platform.bean;

import org.databene.model.Converter;
import org.databene.model.ConversionException;

/**
 * Wraps an Accessor with a Converter interface implementation.<br/>
 * <br/>
 * Created: 25.06.2007 08:04:22
 */
public class PropertyAccessConverter implements Converter {

    private Class propertyType;
    private PropertyAccessor accessor;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public PropertyAccessConverter(String propertyName) {
        this(propertyName, null, true);
    }

    public PropertyAccessConverter(String propertyName, Class propertyType) {
        this(propertyName, propertyType, true);
    }

    public PropertyAccessConverter(String propertyName, Class propertyType, boolean strict) {
        this.accessor = PropertyAccessorFactory.getAccessor(propertyName, strict);
        this.propertyType = propertyType;
    }
    
    // Converter interface implementation ------------------------------------------------------------------------------

    public Class getTargetType() {
        return propertyType;
    }

    public Object convert(Object sourceValue) throws ConversionException {
        return accessor.getValue(sourceValue);
    }
}
