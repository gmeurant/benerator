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

package org.databene.commons.converter;

import org.databene.commons.ConversionException;
import org.databene.commons.Patterns;
import org.databene.commons.StringUtil;

import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Converts Strings of standard date(time) format (yyyy-MM-dd[Thh:mm[:ss[.SSS]]]) to dates.<br/>
 * <br/>
 * Created: 07.09.2007 09:07:12
 * @author Volker Bergmann
 */
public class String2DateConverter<E extends Date> extends ThreadSafeConverter<String, E> implements Patterns {
	
    // TODO v0.6.0 support time zones (like 'Z', '+01:00' or '-01:30')

    private static final String MILLI   = DEFAULT_DATETIME_MILLIS_PATTERN;
    private static final String SECONDS = DEFAULT_DATETIME_SECONDS_PATTERN;
    private static final String MINUTES = DEFAULT_DATETIME_MINUTES_PATTERN;
    private static final String DATE    = DEFAULT_DATE_PATTERN;

    @SuppressWarnings("unchecked")
    public String2DateConverter() {
        this((Class<E>) java.util.Date.class);
    }

    public String2DateConverter(Class<E> targetType) {
        super(String.class, targetType);
    }

    @SuppressWarnings("unchecked")
    public E convert(String sourceValue) {
        return (E) convert(sourceValue, targetType);
    }

    public static <T extends Date> Date convert(String sourceValue, Class<T> targetType) {
        if (StringUtil.isEmpty(sourceValue))
            return null;
        try {
            DateFormat format;
            if (sourceValue.indexOf('T') < 0) {
                format = new SimpleDateFormat(DATE);
            } else {
                switch (sourceValue.length()) {
                    case 10 : format = new SimpleDateFormat(DATE); break;
                    case 16 : format = new SimpleDateFormat(MINUTES); break;
                    case 19 : format = new SimpleDateFormat(SECONDS); break;
                    case 23 : format = new SimpleDateFormat(MILLI); break;
                    default : throw new IllegalArgumentException("Not a supported date format: " + sourceValue);
                }
            }
            java.util.Date simpleDate = format.parse(sourceValue);
            if (targetType == java.util.Date.class)
                return simpleDate;
            else if (targetType == java.sql.Date.class)
                return new java.sql.Date(simpleDate.getTime());
            else if (targetType == java.sql.Timestamp.class)
                return new java.sql.Timestamp(simpleDate.getTime());
            else
                throw new UnsupportedOperationException("Not a supported target type: " + targetType);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }
    
}
