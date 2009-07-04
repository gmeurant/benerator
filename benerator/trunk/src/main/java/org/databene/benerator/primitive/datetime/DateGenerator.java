/*
 * (c) Copyright 2006-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.primitive.datetime;

import org.databene.benerator.Generator;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.distribution.Sequence;
import org.databene.commons.Period;
import org.databene.commons.converter.DateString2DurationConverter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * creates date values by a LongGenerator.
 * <br/>
 * Created: 07.06.2006 22:54:28
 * @author Volker Bergmann
 */
public class DateGenerator implements Generator<Date> {
    
    private DateString2DurationConverter dateConverter = new DateString2DurationConverter();

    /** The generator to use for generating millisecond values */
    private Generator<Long> source;
    
    private long min;
    private long max;
    private long precision;
    private Distribution distribution;
    
    private boolean initialized = false;

    // constructors ----------------------------------------------------------------------------------------------------

    /** Initializes the generator to create days within about the last 80 years with a one-day resolution */
    public DateGenerator() {
        this(defaultStartDate(), currentDay(), Period.DAY.getMillis());
    }

    /** Initializes the generator to create dates with a uniform distribution */
    public DateGenerator(Date min, Date max, long precision) {
        this(min, max, precision, Sequence.RANDOM);
    }

    /** Initializes the generator to create dates of a Sequence or WeightFunction */
    public DateGenerator(Date min, Date max, long precision, Distribution distribution) {
        this.distribution = distribution;
		this.min = (min != null ? min.getTime() : Long.MIN_VALUE);
		this.max = (max != null ? max.getTime() : Long.MAX_VALUE);
		this.precision = precision;
        source = distribution.createGenerator(Long.class, this.min, this.max, this.precision);
    }

    // config properties -----------------------------------------------------------------------------------------------

    /** Sets the earliest date to generate */
    public void setMin(Date min) {
        this.min = min.getTime();
    }

    /** Sets the latest date to generate */
    public void setMax(Date max) {
        this.max = max.getTime();
    }

    /** Sets the date precision in milliseconds */
    public void setPrecision(String precision) {
        this.precision = dateConverter.convert(precision);
    }

    /** Sets the distribution to use */
    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    // source interface ---------------------------------------------------------------------------------------------

    public Class<Date> getGeneratedType() {
        return Date.class;
    }

    public void validate() {
    	if (!initialized) {
    		init();
    		source.validate();
    		this.initialized = true;
    	}
    }

    public boolean available() {
    	if (!initialized)
    		validate();
        return source.available();
    }

    /** Generates a Date by creating a millisecond value from the source generator and wrapping it into a Date */
    public Date generate() {
    	if (!initialized)
    		validate();
        return new Date(source.generate());
    }

    public void reset() {
        source.reset();
    }

    public void close() {
        source.close();
    }

    // implementation --------------------------------------------------------------------------------------------------

    private void init() {
    	this.source = distribution.createGenerator(Long.class, min, max, precision);
    }
    
    /** Returns the default start date as 80 years ago */
    private static Date defaultStartDate() {
        return new Date(currentDay().getTime() - 80L * 365 * Period.DAY.getMillis());
    }

    /** Returns the current day as Date value rounded to midnight */
    private static Date currentDay() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.MONTH),
                calendar.get(GregorianCalendar.DAY_OF_MONTH),
                0,
                0,
                0);
        calendar.set(GregorianCalendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public String toString() {
    	return getClass().getSimpleName() + '[' + source + ']';
    }
}
