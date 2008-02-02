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

package org.databene.commons.format;

import org.databene.commons.StringUtil;

import java.text.Format;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * Converts null values to Strings like "N/A" and back, uses a delegate converter for all others.<br/>
 * <br/>
 * Created: 17.05.2005 17:56:43
 * @author Volker Bergmann
 */
public class NullsafeFormat<S> extends TypedFormat<S> {

    private Format format;
    private Class<S> sourceType;
    private String nullString;

    public NullsafeFormat(TypedFormat<S> format, String nullString) {
        this(format,  format.getSourceType(), nullString);
    }

    public NullsafeFormat(Format format, Class<S> sourceType, String nullString) {
        this.format = format;
        this.sourceType = sourceType;
        this.nullString = nullString;
    }

    public Class<S> getSourceType() {
        return sourceType;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj == null)
            return new StringBuffer(nullString);
        else
            return format(obj, toAppendTo, pos);
    }

    public Object parseObject(String source, ParsePosition pos) {
        if (source == null)
            return null;
        int i;
        for (i = pos.getIndex(); i < source.length() && StringUtil.isWhitespace(source.charAt(i)); i++)
            ;
        if (source.substring(i).startsWith(nullString)) {
            pos.setIndex(i + nullString.length());
            return null;
        }
        return format.parseObject(source, pos);
    }
}
