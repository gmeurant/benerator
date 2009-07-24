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

import java.sql.Time;
import java.sql.Timestamp;
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
 * @since 0.1
 * @author Volker Bergmann
 */
public class ToStringConverter extends FixedSourceTypeConverter<Object, String> {

	// constants -------------------------------------------------------------------------------------------------------
	
	private static final String DEFAULT_NULL_STRING = "";

	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    
    private static ToStringConverter singletonInstance = new ToStringConverter();
    
    // attributes ------------------------------------------------------------------------------------------------------

	/** The string used to represent null values */
    private String nullString;
    
    private String datePattern;

    private String timestampPattern;
    
    private String timePattern;

    private NumberFormatConverter decimalConverter;
    
    private NumberFormatConverter integralConverter;
    
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
        this.decimalConverter = null;
    }
    
    // properties ------------------------------------------------------------------------------------------------------

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullResult) {
        this.nullString = nullResult;
    }
    
    public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String pattern) {
		this.datePattern = pattern;
	}

	public String getDateTimePattern() {
		return timestampPattern;
	}

	public void setDateTimePattern(String pattern) {
		this.timestampPattern = pattern;
	}

	public String getDecimalPattern() {
		return decimalConverter.getPattern();
	}

	public void setDecimalPattern(String pattern) {
		if (decimalConverter == null)
			decimalConverter = new NumberFormatConverter(pattern);
		decimalConverter.setPattern(pattern);
	}

	public char getDecimalSeparator() {
    	return decimalConverter.getDecimalSeparator();
    }

	public void setDecimalSeparator(char separator) {
		if (decimalConverter == null)
			decimalConverter = new NumberFormatConverter();
		decimalConverter.setDecimalSeparator(separator);
    }

	public String getTimePattern() {
		return timePattern;
	}
	
    public void setTimePattern(String timePattern) {
	    this.timePattern = timePattern;
    }

    public String getIntegralPattern() {
    	return integralConverter.getPattern();
    }

    public void setIntegralPattern(String pattern) {
    	if (integralConverter == null)
    		integralConverter = new NumberFormatConverter();
    	integralConverter.setPattern(pattern);
    }

    // Converter interface implementation ------------------------------------------------------------------------------

	public String convert(Object source) throws ConversionException {
        if (source == null) {
            return nullString;
        } else if (source instanceof String) {
        	return (String) source;
        } else if (integralConverter != null && JavaType.isDecimalType(source)) {
        	return decimalConverter.convert((Number) source);
        } else if (decimalConverter != null && JavaType.isDecimalType(source)) {
        	return decimalConverter.convert((Number) source);
        } else if (source.getClass().isArray()) {
        	Class<?> componentType = source.getClass().getComponentType();
			if (componentType == byte.class)
        		return Base64Codec.encode((byte[]) source);
        	else if (componentType == char.class)
        		return String.valueOf((char[])source);
        	else
        		return ArrayFormat.format((Object[])source);
        } else if (source instanceof Class) {
            return ((Class<?>) source).getName();
        } else if (source instanceof Timestamp) {
        	if (timestampPattern != null)
        		return new SimpleDateFormat(timestampPattern).format((Date) source);
        	else
        		return new SimpleDateFormat().format((Date) source);
        } else if (source instanceof Time) {
        	if (timePattern != null)
        		return new SimpleDateFormat(timePattern).format((Date) source);
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

	// utility methods -------------------------------------------------------------------------------------------------
	
    public static <TT> String convert(TT source, String nullString) {
    	if (source == null)
    		return nullString;
    	return singletonInstance.convert(source);
    }

}
