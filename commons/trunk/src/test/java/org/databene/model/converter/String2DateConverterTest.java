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

package org.databene.model.converter;

import junit.framework.TestCase;

import org.databene.commons.TimeUtil;

/**
 * Tests the String2DateConverter.<br/>
 * <br/>
 * Created: 07.09.2007 18:00:32
 */
public class String2DateConverterTest extends TestCase {

    public void testStandardDates() {
        assertEquals(TimeUtil.date(2007, 8, 6), new String2DateConverter().convert("2007-09-06"));
        assertEquals(TimeUtil.date(2007, 8, 6, 13, 28, 0, 0), new String2DateConverter().convert("2007-09-06T13:28"));
        assertEquals(TimeUtil.date(2007, 8, 6, 13, 28, 56, 0), new String2DateConverter().convert("2007-09-06T13:28:56"));
        assertEquals(TimeUtil.date(2007, 8, 6, 13, 28, 56, 123), new String2DateConverter().convert("2007-09-06T13:28:56.123"));
    }

    public void testStrangeDates() {
        String2DateConverter converter = new String2DateConverter();
        assertEquals(null, converter.convert(null));
        assertEquals(null, converter.convert(""));
        assertEquals(TimeUtil.date(1234, 2, 5), converter.convert("1234-3-5"));
        assertEquals(TimeUtil.date(12345, 11, 1), converter.convert("12345-12-1"));
        assertEquals(TimeUtil.date(-10000, 3, 1), converter.convert("-10000-4-1"));
    }
}
