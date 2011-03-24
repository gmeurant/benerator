/*
 * (c) Copyright 2005-2009 by Volker Bergmann. All rights reserved.
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

import org.junit.Test;
import static junit.framework.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Tests the {@link TimeUtil} class.
 * Created: 17.02.2005 21:19:01
 * @since 0.1
 * @author Volker Bergmann
 */
public class TimeUtilTest {

	@Test
	public void testEndOfMonth() {
		assertEquals(TimeUtil.date(1970, 0, 31), TimeUtil.endOfMonth(TimeUtil.date(0)));
		assertEquals(TimeUtil.date(2008, 1, 29), TimeUtil.endOfMonth(TimeUtil.date(2008, 1, 15)));
		assertEquals(TimeUtil.date(2009, 1, 28), TimeUtil.endOfMonth(TimeUtil.date(2009, 1, 28)));
	}
	
	@Test
    public void testMax() {
        Date now = new Date();
        Date later = new Date(now.getTime() + Period.DAY.getMillis());
        assertEquals(later, TimeUtil.max(now, later));
        assertEquals(later, TimeUtil.max(later, now));
    }

	@Test
    public void testMin() {
        Date now = new Date();
        Date later = new Date(now.getTime() + Period.DAY.getMillis());
        assertEquals(now, TimeUtil.min(now, later));
        assertEquals(now, TimeUtil.min(later, now));
    }
    
	@Test
    public void testIsMidnight() {
    	TimeZone zone = TimeZone.getDefault();
    	try {
    		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    		assertTrue(TimeUtil.isMidnight(new Date(0)));
    	} finally {
    		TimeZone.setDefault(zone);
    	}
    }
    
	@Test
    public void testDate() {
    	Date date = TimeUtil.date(2009, 6, 19);
    	Calendar calendar = new GregorianCalendar(2009, 6, 19);
    	assertEquals(calendar.getTime(), date);
    }
    
	@Test
    public void testTimestamp() {
    	Timestamp timestamp = TimeUtil.timestamp(2009, 6, 19, 1, 34, 45, 123456789);
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(new Date(timestamp.getTime()));
    	assertEquals(2009, cal.get(Calendar.YEAR));
    	assertEquals(6, cal.get(Calendar.MONTH));
    	assertEquals(19, cal.get(Calendar.DAY_OF_MONTH));
    	assertEquals(1, cal.get(Calendar.HOUR_OF_DAY));
    	assertEquals(34, cal.get(Calendar.MINUTE));
    	assertEquals(45, cal.get(Calendar.SECOND));
    	assertEquals(123, cal.get(Calendar.MILLISECOND));
    }
    
	@Test
	public void testYearsBetween() {
		Date base = TimeUtil.date(2008, 2, 24);
		Date threeLater = TimeUtil.date(2011, 2, 24);
		assertEquals(3, TimeUtil.yearsBetween(base, threeLater));
		Date lessThanThreeLater = TimeUtil.date(2011, 2, 23);
		assertEquals(2, TimeUtil.yearsBetween(base, lessThanThreeLater));
	}
	
	@Test
	public void testAdd_epoche() {
		assertEquals(TimeUtil.date(2010, 7, 31), TimeUtil.add(TimeUtil.date(2010, 7, 30), TimeUtil.date(1970, 0, 2)));
	}

	@Test
	public void testAdd_AC() {
		assertEquals(TimeUtil.date(2010, 7, 31), TimeUtil.add(TimeUtil.date(2010, 7, 30), TimeUtil.date(0, 0, 1)));
	}

}
