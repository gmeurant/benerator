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

import java.util.Locale;

/**
 * Created: 20.06.2007 08:52:26
 */
public class ArrayFormatTest extends TestCase {

    private static final Locale[] LOCALES = { Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH };

    public void testInstanceFormat() {
        assertEquals("1, 2, 3", new ArrayFormat().format(new Integer[] {1, 2, 3}));
    }

    public void testParse() {
        String[] tokens = ArrayFormat.parse("a, b, c", ", ", String.class);
        assertTrue(ArrayUtil.equals(new String[] {"a", "b", "c"}, tokens));
    }

    public void testFormatSimple() {
        assertEquals("1, 2, 3", ArrayFormat.format(1, 2, 3));
    }

    public void testFormatWithSeparator() {
        assertEquals("de_DE_BY", ArrayFormat.formatStrings("_", "de", "DE", "BY"));
    }

    public void testFormatWithFormatAndSeparator() {
        assertEquals("de/en/fr", ArrayFormat.format(new ToStringFormat(""), "/", LOCALES));
    }

    public void testFormatPartSimple() {
        assertEquals("de, en", ArrayFormat.formatPart(0, 2, LOCALES));
    }

    public void testFormatPartWithSeparator() {
        assertEquals("de/en", ArrayFormat.formatPart("/", 0, 2, LOCALES));
    }

    public void testFormatPartWithFormatAndSeparator() {
        assertEquals("de/en", ArrayFormat.formatPart(ToStringFormat.getDefault(), "/", 0, 2, LOCALES));
    }

    public void testFormatIntArray() {
        assertEquals("1.2.3", ArrayFormat.formatInts(".", 1, 2, 3));
    }
}
