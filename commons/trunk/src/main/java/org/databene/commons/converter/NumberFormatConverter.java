/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import org.databene.commons.ConversionException;
import org.databene.commons.NullSafeComparator;

/**
 * Holds a {@link NumberFormat} and exhibits properties for its configuration.<br/><br/>
 * Created: 26.02.2010 08:37:23
 * @since 0.5.0
 * @author Volker Bergmann
 */
public abstract class NumberFormatConverter<S, T> extends AbstractConverter<S, T> {
	
	// constants -------------------------------------------------------------------------------------------------------
	
	protected static final String DEFAULT_DECIMAL_PATTERN = "";
    
	protected static final char DEFAULT_DECIMAL_SEPARATOR = '.';

	protected static final String DEFAULT_NULL_STRING = "";
	
	// attributes ------------------------------------------------------------------------------------------------------

	private String pattern;
	private char decimalSeparator;
	protected DecimalFormat format;

	/** The string used to represent null values */
    private String nullString;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public NumberFormatConverter(Class<S> sourceType, Class<T> targetType) {
		this(sourceType, targetType, DEFAULT_DECIMAL_PATTERN);
	}

	public NumberFormatConverter(Class<S> sourceType, Class<T> targetType, String pattern) {
		super(sourceType, targetType);
		setPattern(pattern);
		setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR);
		setNullString(DEFAULT_NULL_STRING);
	}
	
	// properties ------------------------------------------------------------------------------------------------------

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.format = new DecimalFormat(pattern);
		setDecimalSeparator(decimalSeparator);
	}

	public char getDecimalSeparator() {
    	return decimalSeparator;
    }

	public void setDecimalSeparator(char decimalSeparator) {
    	this.decimalSeparator = decimalSeparator;
		DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
		newSymbols.setDecimalSeparator(decimalSeparator);
		format.setDecimalFormatSymbols(newSymbols);
    }

	public String getNullString() {
    	return nullString;
    }

	public void setNullString(String nullString) {
    	this.nullString = nullString;
    }
	
	protected String format(Number input) {
		return (input != null ? format.format(input) : nullString);
	}

	protected Number parse(String input) throws ConversionException {
		if (NullSafeComparator.equals(input, nullString))
			return null;
		try {
			Number result = format.parse(input);
			return result;
		} catch (ParseException e) {
			throw new ConversionException("Error parsing " + input + " as number");
		}
	}
	
	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
	    return getClass().getSimpleName() + '[' + pattern + ']';
	}
	
}
