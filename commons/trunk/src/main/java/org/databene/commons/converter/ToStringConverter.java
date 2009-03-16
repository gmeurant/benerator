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

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.databene.commons.ArrayFormat;
import org.databene.commons.Base64Codec;
import org.databene.commons.ConversionException;

/**
 * Converts an object to a String by using its toString() method.
 * Null values can be mapped to an individual String.<br/>
 * <br/>
 * Created: 31.08.2006 18:44:59
 */
public class ToStringConverter extends FixedSourceTypeConverter<Object, String> {

    private static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	private static final String DEFAULT_DECIMAL_PATTERN = null;

	private static final String DEFAULT_NULL_STRING = "";

	private static final char DEFAULT_DECIMAL_SEPARATOR = '.';

	/** The String used to replace null values */
    private String nullString = DEFAULT_NULL_STRING;
    
    private String datePattern = DEFAULT_DATE_PATTERN;

    private String timestampPattern = DEFAULT_TIMESTAMP_PATTERN;
    
    private String decimalPattern = DEFAULT_DECIMAL_PATTERN;
    
    private char decimalSeparator = DEFAULT_DECIMAL_SEPARATOR;
    
    private DecimalFormat decimalFormat;
    
    // constructors ----------------------------------------------------------------------------------------------------

    /** Default constructor that uses an isEmpty String as null representation */
    public ToStringConverter() {
        this(DEFAULT_NULL_STRING);
    }

    /**
     * Constructor that initializes the null replacement to the specified parameter.
     * @param nullString the String to use for replacing null values.
     */
    public ToStringConverter(String nullString) {
        this(nullString, DEFAULT_DATE_PATTERN, DEFAULT_TIMESTAMP_PATTERN);
    }

    public ToStringConverter(String nullString, String datePattern, String timestampPattern) {
    	super(Object.class, String.class);
        this.nullString = nullString;
        this.datePattern = datePattern;
        this.timestampPattern = timestampPattern;
        decimalFormat = null;
    }
    
    // properties ------------------------------------------------------------------------------------------------------

    /** @deprecated replaced by {@link #getNullString()} */
    @Deprecated
    public String getNullResult() {
        return getNullString();
    }

    /** @deprecated replaced by {@link #setNullString(String)} */
    @Deprecated
    public void setNullResult(String nullResult) {
        setNullString(nullResult);
    }

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullResult) {
        this.nullString = nullResult;
    }
    
    public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getTimestampPattern() {
		return timestampPattern;
	}

	public void setTimestampPattern(String timestampPattern) {
		this.timestampPattern = timestampPattern;
	}

	public String getDecimalPattern() {
		return decimalPattern;
	}

	public void setDecimalPattern(String decimalPattern) {
		this.decimalPattern = decimalPattern;
		decimalFormat = new DecimalFormat(decimalPattern);
		setDecimalSeparator(decimalSeparator);
	}

	public char getDecimalSeparator() {
    	return decimalSeparator;
    }

	public void setDecimalSeparator(char decimalSeparator) {
    	this.decimalSeparator = decimalSeparator;
		DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
		newSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormat.setDecimalFormatSymbols(newSymbols);

    }

	// Converter interface implementation ------------------------------------------------------------------------------

	public String convert(Object source) throws ConversionException {
        return convert(source, nullString, datePattern, timestampPattern, decimalFormat);
    }

	// utility methods -------------------------------------------------------------------------------------------------
	
    public static <TT> String convert(TT source, String nullString) {
    	return convert(source, nullString, null, null, null);
    }

    @SuppressWarnings("unchecked")
	public static <TT> String convert(TT source, String nullString, String datePattern, String timestampPattern, NumberFormat numberFormat) {
        if (source == null) {
            return nullString;
        } else if (source instanceof String) {
        	return (String) source;
        } else if (numberFormat != null && JavaType.isDecimalType(source)) {
        	return numberFormat.format(source);
        } else if (source.getClass().isArray()) {
        	Class<?> componentType = source.getClass().getComponentType();
			if (componentType == byte.class)
        		return Base64Codec.encode((byte[]) source);
        	else if (componentType == char.class)
        		return String.valueOf((char[])source);
        	else
        		return ArrayFormat.format((Object[])source);
        } else if (source instanceof Class) {
            return ((Class) source).getName();
        } else if (source instanceof Timestamp) {
        	if (timestampPattern != null)
        		return new SimpleDateFormat(timestampPattern).format((Date) source);
        	else
        		return new SimpleDateFormat().format((Date) source);
        } else if (source instanceof Date) {
        	if (datePattern != null)
        		return new SimpleDateFormat(datePattern).format((Date) source);
        	else
        		return new SimpleDateFormat().format((Date) source);
        } else
            return source.toString();
    }
}
