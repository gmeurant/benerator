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

package org.databene.model.converter;

import org.databene.model.Converter;
import org.databene.model.ConversionException;
import org.databene.commons.ArrayFormat;

/**
 * Converts an object to a String by using its toString() method.
 * Null values can be mapped to an individual String.<br/>
 * <br/>
 * Created: 31.08.2006 18:44:59
 */
public class ToStringConverter<S> implements Converter<S, String> {

    /** The String used to replace null values */
    private String nullResult;

    /** Default canstructor that uses an isEmpty String as null representation */
    public ToStringConverter() {
        this("");
    }

    /**
     * Constructor that initialises the null replacement to the specified parameter.
     * @param nullString the String to use for replacing null values.
     */
    public ToStringConverter(String nullString) {
        this.nullResult = nullString;
    }

    /** Returns the nullResult attribute */
    public String getNullResult() {
        return nullResult;
    }

    /** Sets the nullResult attribute */
    public void setNullResult(String nullResult) {
        this.nullResult = nullResult;
    }

    public Class<String> getTargetType() {
        return String.class;
    }

    /**
     * @see org.databene.model.Converter
     */
    public String convert(S source) throws ConversionException {
        return convert(source, nullResult);
    }

    public static <TT> String convert(TT source, String nullString) {
        if (source == null)
            return nullString;
        else if (source.getClass().isArray())
            return ArrayFormat.format((Object[])source);
        else if (source instanceof Class)
            return ((Class)source).getName();
        else
            return source.toString();
    }
}
