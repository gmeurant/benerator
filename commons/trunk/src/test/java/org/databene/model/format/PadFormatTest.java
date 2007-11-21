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

package org.databene.model.format;

import junit.framework.TestCase;

import java.text.ParseException;

/**
 * Created: 29.06.2007 19:44:11
 */
public class PadFormatTest extends TestCase {

    public void testFormatLeftAligned() {
        PadFormat format = new PadFormat(3, Alignment.LEFT, '0');
        assertEquals("100", format.format("1"));
        assertEquals("000", format.format("0"));
        assertEquals("000", format.format(null));
    }

    public void testFormatRightAligned() {
        PadFormat format = new PadFormat(3, Alignment.RIGHT, '0');
        assertEquals("001", format.format("1"));
        assertEquals("000", format.format("0"));
        assertEquals("000", format.format(null));
        assertEquals("-01", format.format("-1"));
    }

    public void testFormatCentered() {
        PadFormat format = new PadFormat(3, Alignment.CENTER, '0');
        assertEquals("010", format.format("1"));
        assertEquals("000", format.format("0"));
        assertEquals("000", format.format(null));
    }

    public void testFormatNumber() {
        PadFormat format = new PadFormat(5, 2, Alignment.RIGHT, '0');
        assertEquals("00.01", format.format(0.01));
        assertEquals("00.10", format.format(0.1));
        assertEquals("01.00", format.format(1));
        assertEquals("01.00", format.format(1.));
        format = new PadFormat(5, 0, 2, Alignment.RIGHT, '0');
        assertEquals("00.01", format.format(0.01));
        assertEquals("000.1", format.format(0.1));
        assertEquals("00001", format.format(1));
        assertEquals("00001", format.format(1.));
    }


    public void testParseLeftAligned() throws ParseException {
        PadFormat format = new PadFormat(3, Alignment.LEFT, '0');
        assertEquals(null, format.parseObject(null));
        assertEquals("", format.parseObject(""));
        assertEquals("1", format.parseObject("1"));
        assertEquals("1", format.parseObject("100"));
        assertEquals("01", format.parseObject("0100"));
    }

    public void testParseRightAligned() throws ParseException {
        PadFormat format = new PadFormat(3, Alignment.RIGHT, '0');
        assertEquals(null, format.parseObject(null));
        assertEquals("", format.parseObject(""));
        assertEquals("1", format.parseObject("1"));
        assertEquals("1", format.parseObject("001"));
        assertEquals("10", format.parseObject("0010"));
        assertEquals("-1", format.parseObject("-01"));
    }

    public void testParseCentered() throws ParseException {
        PadFormat format = new PadFormat(3, Alignment.CENTER, '0');
        assertEquals(null, format.parseObject(null));
        assertEquals("", format.parseObject(""));
        assertEquals("1", format.parseObject("1"));
        assertEquals("1", format.parseObject("00100"));
    }

}
