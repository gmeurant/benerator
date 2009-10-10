/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.databene.commons.ConversionException;
import org.databene.commons.Patterns;
import org.databene.commons.StringUtil;

/**
 * Parses a String as a time value.<br/>
 * <br/>
 * Created: 14.03.2008 22:15:58
 * @author Volker Bergmann
 */
public class String2TimeConverter extends AbstractBidirectionalConverter<String, Time> implements Patterns {

    public String2TimeConverter() {
        super(String.class, Time.class);
    }

    public Time convert(String sourceValue) throws ConversionException {
        return parse(sourceValue);
    }

    public static Time parse(String sourceValue) throws ConversionException {
        if (StringUtil.isEmpty(sourceValue))
            return null;
        try {
            DateFormat format;
            switch (sourceValue.length()) {
                case 12 : format = new SimpleDateFormat(DEFAULT_TIME_MILLIS_PATTERN);  break;
                case  8 : format = new SimpleDateFormat(DEFAULT_TIME_SECONDS_PATTERN); break;
                case  5 : format = new SimpleDateFormat(DEFAULT_TIME_MINUTES_PATTERN); break;
                default : throw new IllegalArgumentException("Not a supported time format: " + sourceValue);
            }
            Date simpleDate = format.parse(sourceValue);
            long millis = simpleDate.getTime();
            return new Time(millis);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    public String revert(Time target) throws ConversionException {
        return new SimpleDateFormat(DEFAULT_TIME_MILLIS_PATTERN).format(new Date(target.getTime()));
    }

}
