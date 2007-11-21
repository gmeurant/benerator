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

package org.databene.commons;

import junit.framework.TestCase;
import static org.databene.commons.NumberUtil.*;

/**
 * Created: 21.06.2007 08:32:34
 */
public class NumberUtilTest extends TestCase {

    public void test() {
        assertEquals("1.00", NumberUtil.format(1));
        assertEquals("1.23", NumberUtil.format(1.23, 2));
        assertEquals("1", NumberUtil.format(1.23, 0));
        assertEquals("001", NumberUtil.formatZeroPadded(1, 3));
        assertEquals("1", NumberUtil.formatZeroPadded(1, 1));
    }

    public void testBitsUsed() {
        assertEquals(1, bitsUsed(0));
        assertEquals(1, bitsUsed(1));
        assertEquals(2, bitsUsed(2));
        assertEquals(8, bitsUsed(0xff));
        assertEquals(9, bitsUsed(0x100));
        assertEquals(16, bitsUsed(0xffff));
        assertEquals(17, bitsUsed(0x10000));
        assertEquals(32, bitsUsed(0xffffffffL));
        assertEquals(63, bitsUsed(0x7fffffffffffffffL));
        assertEquals(64, bitsUsed(0xffffffffffffffffL));
        assertEquals(64, bitsUsed(-1L));
    }

    public void testFormatHex() {
        assertEquals("0001", NumberUtil.formatHex( 1, 4));
        assertEquals("ffff", NumberUtil.formatHex(-1, 4));
    }
}
