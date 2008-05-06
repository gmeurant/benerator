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

package org.databene.commons.converter;

import junit.framework.TestCase;
import org.databene.SomeEnum;
import org.databene.commons.ArrayUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.converter.StringConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Tests the StringConverter.<br/>
 * <br/>
 * Created: 20.08.2007 07:52:38
 */
public class StringConverterTest extends TestCase {

	public void testConvertNull() {
        assertEquals(null, StringConverter.convert(null, Object.class));
	}
	
	public void testConvertEmptyToNumber() {
        assertEquals(null, StringConverter.convert("", Integer.class));
        assertEquals(null, StringConverter.convert("", Byte.class));
        assertEquals(null, StringConverter.convert("", BigDecimal.class));
	}
	
	public void testConvertEmptyToPrimitive() {
        assertEquals(null, StringConverter.convert("", int.class));
        assertEquals(null, StringConverter.convert("", byte.class));
        assertEquals(null, StringConverter.convert("", char.class));
        assertEquals(null, StringConverter.convert("", boolean.class));
	}
	
	public void testConvertValidToCharacter() {
        assertEquals('A', (char)StringConverter.convert("A", Character.class));
        assertEquals(null, StringConverter.convert("", Character.class));
	}
	
	public void testConvertInvalidToCharacter() {
		try {
			StringConverter.convert("ABC", Character.class);
			fail(ConversionException.class.getSimpleName() + " expected");
		} catch (ConversionException e) {
			// expected
		}
	}
	
    public void testString2Enum() {
        assertEquals(SomeEnum.ONE, StringConverter.convert("ONE", SomeEnum.class));
    }

    public void testString2Number() {
        assertEquals(new BigDecimal("1"), StringConverter.convert("1", BigDecimal.class));
    }
    
    public void testString2Date() {
        assertEquals(new GregorianCalendar(1970, 0, 1).getTimeInMillis(), StringConverter.convert("1970-01-01", Date.class).getTime());
    }
    
    public void testString2StringArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray("A", "B", "C"), 
        		StringConverter.convert("A,B,C", String[].class)));
    }
    
    public void testString2CharArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray('A', 'B', 'C'), 
        		StringConverter.convert("A,B,C", Character[].class)));
    }

    public void testString2IntegerArray() {
        assertTrue(Arrays.equals(
        		ArrayUtil.toArray(1, 2, 3), 
        		StringConverter.convert("1,2,3", Integer[].class)));
    }
    
    public void testString2PrimitiveNumber() {
        assertEquals(1, (int)StringConverter.convert("1", int.class));
        assertEquals((byte)1, (byte)StringConverter.convert("1", byte.class));
    }
    
    public void testString2Locale() {
        assertEquals(Locale.GERMAN, StringConverter.convert("de", Locale.class));
    }
    
    public void testString2StringConverterTest() {
    	try {
    		StringConverter.convert("Bla", StringConverterTest.class);
    		fail(ConversionException.class.getSimpleName() + " expected");
    	} catch (ConversionException e) {
    		// expected
    	}
    }
    
}
