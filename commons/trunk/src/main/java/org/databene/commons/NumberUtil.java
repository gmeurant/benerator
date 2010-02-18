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

package org.databene.commons;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.databene.commons.converter.NumberConverter;

/**
 * Provides convenience methods for the Java number types.
 * Created: 12.02.2005 18:24:47
 * @since 0.1
 * @author Volker Bergmann
 */
@SuppressWarnings("unchecked")
public class NumberUtil {
	
	// constants -------------------------------------------------------------------------------------------------------
	
	private static final Map<Class<? extends Number>, ? extends Number> maxValues = 
		CollectionUtil.buildMap(
				byte.class,       Byte.MAX_VALUE, 
				Byte.class,       Byte.MAX_VALUE, 
				short.class,      Byte.MAX_VALUE, 
				Short.class,      Short.MAX_VALUE, 
				int.class,        Short.MAX_VALUE, 
				Integer.class,    Integer.MAX_VALUE, 
				long.class,       Integer.MAX_VALUE, 
				Long.class,       Byte.MAX_VALUE, 
				float.class,      Float.MAX_VALUE, 
				Float.class,      Float.MAX_VALUE, 
				double.class,     Double.MAX_VALUE, 
				Double.class,     Double.MAX_VALUE, 
				BigDecimal.class, Double.MAX_VALUE, 
				BigInteger.class, Long.MAX_VALUE 
		);
	
	public static <T extends Number> Byte toByte(T value) {
	    return NumberConverter.convert(value, Byte.class);
    }
	
	public static <T extends Number> Integer toInteger(T value) {
	    return NumberConverter.convert(value, Integer.class);
    }
	
	public static <T extends Number> Long toLong(T value) {
	    return NumberConverter.convert(value, Long.class);
    }
	
	public static <T extends Number> Float toFloat(T value) {
	    return NumberConverter.convert(value, Float.class);
    }
	
	public static <T extends Number> Double toDouble(T value) {
	    return NumberConverter.convert(value, Double.class);
    }
	
    public static String formatHex(int value, int digits) {
        String tmp = Integer.toHexString(value);
        if (tmp.length() > digits)
            return tmp.substring(tmp.length() - digits, tmp.length());
        return StringUtil.padLeft(tmp, digits, '0');
    }

    public static int bitsUsed(long value) {
        if (value < 0)
            return 64;
        for (int i = 62; i > 0; i--)
            if (((value >> i) & 1) == 1)
                return i + 1;
        return 1;
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++)
            result = (result << 8) - Byte.MIN_VALUE + bytes[i];
        return result;
    }

    public static String format(double number, int digits) {
        NumberFormat nf;
        nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(digits);
        nf.setMaximumFractionDigits(digits);
        return nf.format(number);
    }

    public static <T extends Number> T maxValue(Class<T> numberType) {
    	Number value = maxValues.get(numberType);
    	if (value == null)
    		throw new IllegalArgumentException("Not a supported number type: " + numberType);
		return (T) value;
    }
    
}
