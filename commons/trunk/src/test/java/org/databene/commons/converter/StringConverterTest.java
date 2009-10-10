/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

import org.junit.Test;
import static junit.framework.Assert.*;
import org.databene.SomeEnum;
import org.databene.commons.ArrayUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.TimeUtil;
import org.databene.commons.converter.StringConverter;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Tests the {@link StringConverter}.<br/>
 * <br/>
 * Created: 20.08.2007 07:52:38
 * @author Volker Bergmann
 */
public class StringConverterTest {

	@Test
	public void testConvertNull() {
        assertEquals(null, StringConverter.convert(null, Object.class));
	}
	
	@Test
	public void testConvertToString() {
        assertEquals("ABC", StringConverter.convert("ABC", String.class));
	}
	
	@Test
	public void testConvertEmptyToNumber() {
        assertEquals(null, StringConverter.convert("", Integer.class));
        assertEquals(null, StringConverter.convert("", Byte.class));
        assertEquals(null, StringConverter.convert("", BigDecimal.class));
	}
	
	@Test
	public void testConvertEmptyToPrimitive() {
        assertEquals(null, StringConverter.convert("", int.class));
        assertEquals(null, StringConverter.convert("", byte.class));
        assertEquals(null, StringConverter.convert("", char.class));
        assertEquals(null, StringConverter.convert("", boolean.class));
	}
	
	@Test
	public void testConvertValidToCharacter() {
        assertEquals('A', (char)StringConverter.convert("A", Character.class));
        assertEquals(null, StringConverter.convert("", Character.class));
	}
	
	@Test
	public void testConvertInvalidToCharacter() {
		try {
			StringConverter.convert("ABC", Character.class);
			fail(ConversionException.class.getSimpleName() + " expected");
		} catch (ConversionException e) {
			// expected
		}
	}
	
	@Test
    public void test2Enum() {
        assertEquals(SomeEnum.ONE, StringConverter.convert("ONE", SomeEnum.class));
    }

	@Test
    public void test2Number() {
        assertEquals((byte) 1, (byte) StringConverter.convert("1", byte.class));
        assertEquals(Byte.valueOf((byte) 1), StringConverter.convert("1", Byte.class));
        
        assertEquals((short) 1, (short) StringConverter.convert("1", short.class));
        assertEquals(Short.valueOf((short) 1), StringConverter.convert("1", Short.class));
        
        assertEquals(1, (int) StringConverter.convert("1", int.class));
        assertEquals(Integer.valueOf(1), StringConverter.convert("1", Integer.class));
        
        assertEquals(1L, (long) StringConverter.convert("1", long.class));
        assertEquals(Long.valueOf((byte) 1), StringConverter.convert("1", Long.class));
        
        assertEquals((float) 1, StringConverter.convert("1", float.class));
        assertEquals(Float.valueOf((byte) 1), StringConverter.convert("1", Float.class));
        
        assertEquals(1., (double) StringConverter.convert("1", double.class));
        assertEquals(Double.valueOf((byte) 1), StringConverter.convert("1", Double.class));
        
        assertEquals(new BigInteger("1"), StringConverter.convert("1", BigInteger.class));
        assertEquals(new BigDecimal("1"), StringConverter.convert("1", BigDecimal.class));
    }
    
	@Test
    public void test2Date() {
        assertEquals(new GregorianCalendar(1970, 0, 1).getTimeInMillis(), StringConverter.convert("1970-01-01", Date.class).getTime());
    }

	@Test
    public void test2GregorianCalendar() {
        assertEquals(new GregorianCalendar(1970, 0, 1), StringConverter.convert("1970-01-01", GregorianCalendar.class));
        assertEquals(new GregorianCalendar(2001, 1, 2, 3, 4, 5), StringConverter.convert("2001-02-02T03:04:05", GregorianCalendar.class));
    }
    
	@Test
    public void test2Calendar() {
        assertEquals(new GregorianCalendar(1970, 0, 1), StringConverter.convert("1970-01-01", Calendar.class));
    }
    
	@Test
    public void test2StringArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray("A", "B", "C"), 
        		StringConverter.convert("A,B,C", String[].class)));
    }
    
	@Test
    public void test2CharArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray('A', 'B', 'C'), 
        		StringConverter.convert("A,B,C", Character[].class)));
    }

	@Test
    public void test2IntegerArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray(1, 2, 3), 
        		StringConverter.convert("1,2,3", Integer[].class)));
    }
    
	@Test
    public void test2PrimitiveNumber() {
        assertEquals(1, (int)StringConverter.convert("1", int.class));
        assertEquals((byte)1, (byte)StringConverter.convert("1", byte.class));
    }
    
	@Test
    public void test2Locale() {
        assertEquals(Locale.GERMAN, StringConverter.convert("de", Locale.class));
    }
    
	@Test
    public void test2Class() {
		assertEquals(URL.class, StringConverter.convert("java.net.URL", Class.class));
    }
    
	@Test
    public void test2StringConverterTest() {
    	try {
    		StringConverter.convert("Bla", StringConverterTest.class);
    		fail(ConversionException.class.getSimpleName() + " expected");
    	} catch (ConversionException e) {
    		// expected
    	}
    }
    
	@Test
    public void test2File() {
    	assertEquals(new File("license.txt"), StringConverter.convert("license.txt", File.class));
    }
    
	@Test
    public void test2URL() throws Exception {
    	assertEquals(new URL("http://databene.org/databene.benerator:80"), 
    			StringConverter.convert("http://databene.org/databene.benerator:80", URL.class));
    }
    
	@Test
    public void test2URI() throws Exception {
    	assertEquals(new URI("http://databene.org/databene.benerator:80"), 
    			StringConverter.convert("http://databene.org/databene.benerator:80", URI.class));
    }
    
	@Test
    public void test2SimpleDateFormat() {
    	assertEquals(new SimpleDateFormat("dd.MM.yy"), StringConverter.convert("dd.MM.yy", SimpleDateFormat.class));
    }
    
	@Test
    public void test2DateFormat() {
    	assertEquals(new SimpleDateFormat("dd.MM.yy"), StringConverter.convert("dd.MM.yy", DateFormat.class));
    }
    
	@Test
    public void test2DecimalFormat() {
    	assertEquals(new DecimalFormat("00.00"), StringConverter.convert("00.00", DecimalFormat.class));
    }
    
	@Test
    public void test2NumberFormat() {
    	assertEquals(new DecimalFormat("00.00"), StringConverter.convert("00.00", NumberFormat.class));
    }
    
	@Test
    public void test2SqlDate() {
        assertEquals(new GregorianCalendar(1970, 0, 1).getTimeInMillis(), StringConverter.convert("1970-01-01", java.sql.Date.class).getTime());
    }

	@Test
    public void test2SqlTime() {
        assertEquals(TimeUtil.time(12, 34, 56, 789), StringConverter.convert("12:34:56.789", java.sql.Time.class));
    }
    
	@Test
    public void test2SqlTimestamp() {
        assertEquals(
        	TimeUtil.timestamp(2008, 1, 1, 12, 34, 56, 789000000), 
        	StringConverter.convert("2008-02-01T12:34:56.789", java.sql.Timestamp.class));
    }

	@Test
    public void test2Pattern() {
    	// the Pattern's equal() method does not seem to be implemented correctly, 
    	// so we only compare the String representations
        assertEquals(
        		Pattern.compile("[1-3]{2,4}").toString(), 
        		StringConverter.convert("[1-3]{2,4}", Pattern.class).toString());
    }
	
}
