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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * Tests the LiteralParser class.<br/><br/>
 * Created: 20.03.2008 07:18:30
 * @author Volker Bergmann
 */
public class LiteralParserTest extends TestCase {
    
    public void testNull() {
        assertEquals(null, LiteralParser.parse(null));
        assertEquals(null, LiteralParser.parse(""));
        assertEquals(null, LiteralParser.parse(" \t "));
    }

    public void testBoolean() {
        assertEquals(Boolean.TRUE, LiteralParser.parse("true"));
        assertEquals(Boolean.TRUE, LiteralParser.parse(" true "));
        assertEquals(Boolean.FALSE, LiteralParser.parse("false"));
        assertEquals(Boolean.FALSE, LiteralParser.parse(" false "));
    }

    public void testString() {
        checkText("Alpha");
        checkText("true or false");
        checkText("7 days");
        checkText("True");
        checkText("TRUE");
        checkText("1.2.3.4");
        checkText("1 2 3 4");
    }
    
    public void testInteger() {
        assertEquals(0, LiteralParser.parse("0"));
        assertEquals(1, LiteralParser.parse("1"));
        assertEquals(-1, LiteralParser.parse("-1"));
    }

    public void testLong() {
        checkLong(Long.MAX_VALUE);
        checkLong(((long) Integer.MAX_VALUE) + 1);
        checkLong(((long) Integer.MIN_VALUE) - 1);
        checkLong(Long.MIN_VALUE + 1);
    }

    public void testDouble() {
        assertEquals( 0., LiteralParser.parse("0.0"));
        assertEquals( 1., LiteralParser.parse("1."));
        assertEquals( 1., LiteralParser.parse("1.0"));
        assertEquals(-1., LiteralParser.parse("-1."));
        assertEquals(-1., LiteralParser.parse("-1.0"));
    }
    
    public void testDate() throws ParseException {
        checkDate("1970-01-01", "yyyy-MM-dd");
        checkDate("1970-01-02", "yyyy-MM-dd");
        checkDate("1969-12-31", "yyyy-MM-dd");
        checkDate("1970-01-01T00:01", "yyyy-MM-dd'T'HH:mm");
        checkDate("1970-01-02T00:01", "yyyy-MM-dd'T'HH:mm");
        checkDate("1969-12-31T23:59", "yyyy-MM-dd'T'HH:mm");
        checkDate("1970-01-01T00:01:01", "yyyy-MM-dd'T'HH:mm:ss");
        checkDate("1970-01-02T00:01:01", "yyyy-MM-dd'T'HH:mm:ss");
        checkDate("1969-12-31T23:59:59", "yyyy-MM-dd'T'HH:mm:ss");
        checkDate("1970-01-01T00:01:01.123", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        checkDate("1970-01-02T00:01:01.123", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        checkDate("1969-12-31T23:59:59.123", "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    // private helper methods ------------------------------------------------------------------------------------------
    
    private void checkText(String text) {
        assertEquals(text, LiteralParser.parse(text));
    }

    private void checkLong(long value) {
        assertEquals(value, LiteralParser.parse(String.valueOf(value)));
    }

    private void checkDate(String dateString, String pattern) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        Date date = f.parse(dateString);
        assertEquals(date, LiteralParser.parse(dateString));
    }

}
