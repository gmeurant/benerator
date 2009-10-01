/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.Patterns;

/**
 * Holds format strings for date and number objects.<br/>
 * <br/>
 * Created at 01.10.2009 12:18:59
 * @since 0.5.0
 * @author Volker Bergmann
 */

public abstract class AbstractFormatter implements Patterns {

    // attributes ------------------------------------------------------------------------------------------------------

	/** The string used to represent null values */
    protected String nullString;
    
    protected String datePattern;

    protected String dateTimePattern;

    protected String timePattern;

    protected String timestampPattern;
    
    protected NumberFormatConverter decimalConverter;
    
    protected NumberFormatConverter integralConverter;
    
    // constructors ----------------------------------------------------------------------------------------------------

    /** Default constructor that uses an isEmpty String as null representation */
    public AbstractFormatter() {
        this(DEFAULT_NULL_STRING);
    }

    /**
     * Constructor that initializes the null replacement to the specified parameter.
     * @param nullString the String to use for replacing null values.
     */
    public AbstractFormatter(String nullString) {
        this(nullString, DEFAULT_DATE_PATTERN, DEFAULT_TIMESTAMP_PATTERN);
    }

    public AbstractFormatter(String nullString, String datePattern, String timestampPattern) {
        this.nullString = nullString;
        this.datePattern = datePattern;
        this.timestampPattern = timestampPattern;
        this.timePattern = DEFAULT_TIME_PATTERN;
        this.dateTimePattern = DEFAULT_DATE_TIME_PATTERN;
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
		return dateTimePattern;
	}

	public void setDateTimePattern(String pattern) {
		this.dateTimePattern = pattern;
	}

	public String getTimestampPattern() {
		return timestampPattern;
	}

	public void setTimestampPattern(String pattern) {
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
    
}
