/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.bean;

import org.databene.commons.ArrayUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.converter.ThreadSafeConverter;
import org.databene.commons.converter.AnyConverter;

/**
 * Extracts property values from an array of JavaBeans in a way that
 * processing n beans results in an array of n elements with the property values.<br/>
 * <br/>
 * Created: 02.08.2007 20:47:35
 * @author Volker Bergmann
 */
public class ArrayPropertyExtractor<E> extends ThreadSafeConverter<Object[], E[]> {

    private String propertyName;
    private Class<E> propertyType;

    @SuppressWarnings("unchecked")
    public ArrayPropertyExtractor(String propertyName, Class<E> propertyType) {
    	super(Object[].class, ArrayUtil.arrayType(propertyType));
        this.propertyName = propertyName;
        this.propertyType = propertyType;
    }

    public E[] convert(Object[] sourceValue) throws ConversionException {
        return convert(sourceValue, propertyName, propertyType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] convert(Object[] sourceValue, String propertyName, Class<T> propertyType)
            throws ConversionException {
        T[] array = ArrayUtil.newInstance(propertyType, sourceValue.length);
        PropertyAccessor<Object, T> propertyAccessor = PropertyAccessorFactory.getAccessor(propertyName);
        for (int i = 0; i < sourceValue.length; i++) {
            Object value = propertyAccessor.getValue(sourceValue[i]);
            array[i] = AnyConverter.convert(value, propertyType);
        }
        return array;
    }
    
}
