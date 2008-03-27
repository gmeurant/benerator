/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

import junit.framework.TestCase;

/**
 * Tests the String2TimeConverter.<br/><br/>
 * Created: 14.03.2008 22:23:51
 * @author Volker Bergmann
 */
public class String2TimeConverterTest extends TestCase {

    public void testMillis() {
        check("00:00:00.000", 0);
        check("00:00:00.001", 1);
        check("00:00:00.123", 123);
        check("00:00:01.001", 1001);
        check("00:01:00.001", 60001);
        check("01:00:00.001", 3600001);
    }

    public void testSeconds() {
        check("00:00:00", 0);
        check("00:00:01", 1000);
        check("00:01:00", 60000);
        check("01:01:01", 3661000);
    }

    public void testMinutes() {
        check("00:00", 0);
        check("00:01", 60000);
        check("01:00", 3600000);
    }

    private void check(String timeString, long expectedMillis) {
        assertEquals(expectedMillis, new String2TimeConverter().convert(timeString).getTime());
    }
}
