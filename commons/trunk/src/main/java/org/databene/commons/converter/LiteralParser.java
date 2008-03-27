/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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
import org.databene.commons.Converter;
import org.databene.commons.StringCharacterIterator;
import org.databene.commons.TimeUtil;

/**
 * Parses the literal representation a simple type into an appropriate Java object of type 
 * Boolean, Integer, Long, Double, Date or String.<br/><br/>
 * Created: 19.03.2008 20:05:25
 * @author Volker Bergmann
 */
public class LiteralParser implements Converter<String, Object> {

    public Class<Object> getTargetType() {
        return Object.class;
    }

    public Object convert(String sourceValue) throws ConversionException {
        return parse(sourceValue);
    }

    /**
     * parses a String into a date, number, boolean or String.
     * <code>
     *   content := boolean | date | number | anytext
     *   boolean := 'true' | 'false'
     *   date := digit{4} '-' digit{2} '-' digit{2} ['T' digit{2} ':' digit{2} [':' digit{2} ['.' digit{1,3}]]]
     *   number := [-] digit* ['.' digit*]
     * </code> 
     * @param text
     * @return
     */
    public static Object parse(String text) {
        if (text == null)
            return null;
        String trimmed = text.trim();
        if (trimmed.length() == 0)
            return null;

        // test for boolean
        if ("true".equals(trimmed))
            return Boolean.TRUE;
        else if ("false".equals(trimmed))
            return Boolean.FALSE;
        
        // precheck for anytext
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!(c == ':' || c == '-' || c == 'T' || c == '.' || (c >= '0' && c <= '9') || c == ' ' || c == '\t'))
                return text;
        }
        
        // test for number or date
        StringCharacterIterator iterator = new StringCharacterIterator(trimmed);
        char c = iterator.next();
        if (c == '-')
            return parseNonNegativeNumber(iterator, true);
        else if (c >= '0' && c <= '9') {
            iterator.pushBack();
            Object tmp = parseNonNegativeNumber(iterator, false);
            if (tmp != null)
                return tmp;
            tmp = parseDate(trimmed);
            if (tmp != null)
                return tmp;
        }
        return trimmed;
    }

    private static Object parseDate(String trimmed) {
        StringCharacterIterator iterator = new StringCharacterIterator(trimmed);
        // parse day
        int year = (int) parseNonNegativeIntegerPart(iterator);
        if (!iterator.hasNext() || iterator.next() != '-')
            return null;
        int month = (int) parseNonNegativeIntegerPart(iterator) - 1;
        if (!iterator.hasNext() || iterator.next() != '-')
            return null;
        int day = (int) parseNonNegativeIntegerPart(iterator);
        if (!iterator.hasNext())
            return TimeUtil.date(year, month, day);
        if (iterator.next() != 'T')
            return null;
        // parse hours:minutes
        int hours = (int) parseNonNegativeIntegerPart(iterator);
        if (!iterator.hasNext() || iterator.next() != ':')
            return null;
        int minutes = (int) parseNonNegativeIntegerPart(iterator);
        if (!iterator.hasNext())
            return TimeUtil.date(year, month, day, hours, minutes, 0, 0);
        // parse seconds
        if (iterator.next() != ':')
            return trimmed;
        int seconds = (int) parseNonNegativeIntegerPart(iterator);
        if (!iterator.hasNext())
            return TimeUtil.date(year, month, day, hours, minutes, seconds, 0);
        // parse second fractions
        if (iterator.next() != '.')
            return trimmed;
        double f = parseFraction(iterator);
        if (!iterator.hasNext())
            return TimeUtil.date(year, month, day, hours, minutes, seconds, (int)(f * 1000));
        else
            return trimmed;
    }
    
    private static double parseFraction(StringCharacterIterator iterator) {
        double p = 0.;
        double base = 0.1;
        while (iterator.hasNext()) {
            char c = iterator.next();
            if (c >= '0' && c <= '9')
                p += base * (c - '0');
            else {
                iterator.pushBack();
                return p;
            }
            base /= 10;
        }
        return p;
    }
    
    private static long parseNonNegativeIntegerPart(StringCharacterIterator iterator) {
        long n = 0;
        int c = '0';
        while (iterator.hasNext() && Character.isDigit(c = iterator.next())) {
            n = n * 10 + (c - '0');
            c = -1;
        }
        if (c != -1)
            iterator.pushBack();
        return n;
    }

    private static Object parseNonNegativeNumber(StringCharacterIterator iterator, boolean negative) {
        // parse integral number (part)
        long n = parseNonNegativeIntegerPart(iterator);
        // handle numbers without fraction digits
        if (!iterator.hasNext()) {
            if (n > (long) Integer.MAX_VALUE)
                return (negative ? -n : n);
            else
                return (negative ? - (int) n : (int) n);
        } else {
            if (iterator.next() == '.')  {
                if (!iterator.hasNext())
                    return (negative ? -(double) n : (double)n);
            } else
                return null;
        }
        // parse fraction
        double p = n;
        double base = 0.1;
        while (iterator.hasNext()) {
            char c = iterator.next();
            if (c >= '0' && c <= '9')
                p += base * (c - '0');
            else
                return null;
            base /= 10;
        }
        return (negative ? -p : p);
    }
}
