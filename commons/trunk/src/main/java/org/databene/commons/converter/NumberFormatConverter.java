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

import java.text.DecimalFormat;
import java.text.ParseException;

import org.databene.commons.ConversionException;

/**
 * Converts Strings to Numbers using a {@link DecimalFormat}.<br/>
 * <br/>
 * Created: 23.06.2008 18:49:17
 * @since 0.4.4
 * @author Volker Bergmann
 */
public class NumberFormatConverter extends AbstractBidirectionalConverter<Number, String> {
	
	private String pattern;
	private DecimalFormat format;

	public NumberFormatConverter() {
		this("#");
	}

	public NumberFormatConverter(String pattern) {
		super(Number.class, String.class);
		this.pattern = pattern;
	}
	
	// properties ------------------------------------------------------------------------------------------------------

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.format = new DecimalFormat(pattern);
	}

	// Converter interface implementation ------------------------------------------------------------------------------

	public String convert(Number sourceValue) throws ConversionException {
		return format.format(sourceValue);
	}
	
	public Number revert(String target) throws ConversionException {
		try {
			return format.parse(target);
		} catch (ParseException e) {
			throw new ConversionException("Error converting " + target);
		}
	}
}
