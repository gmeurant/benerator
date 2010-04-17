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

import java.sql.Timestamp;
import java.text.DateFormat;

import org.databene.commons.ConversionException;
import org.databene.commons.Patterns;
import org.databene.commons.format.ConcurrentDateFormat;

/**
 * Formats a {@link Timestamp} as {@link String}.<br/><br/>
 * Created: 18.02.2010 17:46:14
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class TimestampFormatter extends ThreadSafeConverter<Timestamp, String> {
	
	private DateFormat prefixFormat;
	
	public TimestampFormatter() {
	    this(Patterns.DEFAULT_DATETIME_SECONDS_PATTERN + '.');
    }

	public TimestampFormatter(String prefixPattern) {
	    this(new ConcurrentDateFormat(prefixPattern));
    }

	public TimestampFormatter(DateFormat format) {
		super(Timestamp.class, String.class);
	    this.prefixFormat = format;
    }

	public String convert(Timestamp sourceValue) throws ConversionException {
	    return format(sourceValue);
    }

	public String format(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		return prefixFormat.format(timestamp) + timestamp.getNanos();
	}

}
