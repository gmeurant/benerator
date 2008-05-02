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

import java.util.Arrays;

import org.databene.commons.StringUtil;
import org.databene.commons.ArrayUtil;

/**
 * (c) Copyright 2006 by Volker Bergmann
 * Created: 21.07.2006 17:31:42
 */
public class StringUtilTest extends TestCase {

    public void testEmpty() {
        assertTrue(StringUtil.isEmpty(null));
        assertTrue(StringUtil.isEmpty(""));
        assertFalse(StringUtil.isEmpty(" "));
        assertFalse(StringUtil.isEmpty("null"));
    }

    public void testSuffix() {
        assertEquals(null, StringUtil.suffix("a", '.'));
        assertEquals(null, StringUtil.suffix("a.", '.'));
        assertEquals("b", StringUtil.suffix("a.b", '.'));
        assertEquals("b", StringUtil.suffix(".b", '.'));
        assertEquals(null, StringUtil.suffix(null, '.'));
    }

    public void testLastToken() {
        assertEquals("a", StringUtil.lastToken("a", ','));
        assertEquals(null, StringUtil.lastToken("a,", ','));
        assertEquals("b", StringUtil.lastToken("a,b", ','));
        assertEquals("b", StringUtil.lastToken(",b", ','));
        assertEquals(null, StringUtil.lastToken(null, ','));
    }

    public void testEndsWithSequence() {
        assertTrue(ArrayUtil.endsWithSequence(new String[] {"a", "b", "c"}, new String[] {"c"}));
        assertTrue(ArrayUtil.endsWithSequence(new String[] {"a", "b", "c"}, new String[] {"b", "c"}));
        assertTrue(ArrayUtil.endsWithSequence(new String[] {"a", "b", "c"}, new String[] {"a", "b", "c"}));
        assertFalse(ArrayUtil.endsWithSequence(new String[] {"a", "b", "c"}, new String[] {"b", "a"}));
        assertFalse(ArrayUtil.endsWithSequence(new String[] {"a", "b", "c"}, new String[] {"a", "b"}));
    }

    public void testTokenize() {
        assertNull(StringUtil.tokenize(null, ','));
        assertTrue(Arrays.equals(new String[] { "" }, StringUtil.tokenize("", ',')));
        assertTrue(Arrays.equals(new String[] { "a", "b", "" }, StringUtil.tokenize("a,b,", ',')));
        assertTrue(Arrays.equals(new String[] { "", "b", "c" }, StringUtil.tokenize(",b,c", ',')));
        assertTrue(Arrays.equals(new String[] { "", "b", "" }, StringUtil.tokenize(",b,", ',')));
        assertTrue(Arrays.equals(new String[] { "a", "b", "c" }, StringUtil.tokenize("a,b,c", ',')));
        assertTrue(Arrays.equals(new String[] { "a", "b", "c" }, StringUtil.tokenize("a.b.c", '.')));
        assertTrue(Arrays.equals(new String[] { "a,b,c" }, StringUtil.tokenize("a,b,c", '.')));
    }

    public void testNormalize() {
        assertEquals("#Abc!", StringUtil.normalize("#Abc!"));
    }

    public void testIncrement() {
        assertEquals("0", StringUtil.increment(null));
        assertEquals("0", StringUtil.increment(""));
        assertEquals("1", StringUtil.increment("0"));
        assertEquals("a", StringUtil.increment("9"));
        assertEquals("b", StringUtil.increment("a"));
        assertEquals("10", StringUtil.increment("z"));
        assertEquals("11", StringUtil.increment("10"));
        assertEquals("12", StringUtil.increment("11"));
        assertEquals("20", StringUtil.increment("1z"));
        assertEquals("100", StringUtil.increment("zz"));
    }

    public void testNormalizeSpace() {
        assertEquals("abc", StringUtil.normalizeSpace("abc"));
        assertEquals("", StringUtil.normalizeSpace(""));
        assertEquals("", StringUtil.normalizeSpace(" "));
        assertEquals("", StringUtil.normalizeSpace("\r\n"));
        assertEquals("abc", StringUtil.normalizeSpace(" abc "));
        assertEquals("abc def", StringUtil.normalizeSpace("abc def"));
        assertEquals("abc def", StringUtil.normalizeSpace(" abc \r\n def \r\n"));
    }

    public void testTrimEnd() {
        assertEquals(null, StringUtil.trimEnd(null));
        assertEquals("", StringUtil.trimEnd(""));
        assertEquals("", StringUtil.trimEnd(" \r\n"));
        assertEquals("abc", StringUtil.trimEnd("abc"));
        assertEquals("abc", StringUtil.trimEnd("abc "));
        assertEquals(" abc", StringUtil.trimEnd(" abc"));
        assertEquals(" abc", StringUtil.trimEnd(" abc "));
    }

    public void testPadRight() {
        assertEquals("", StringUtil.padRight(null, 0, ' '));
        assertEquals(" ", StringUtil.padRight(null, 1, ' '));
        assertEquals("", StringUtil.padRight("", 0, ' '));
        assertEquals(" ", StringUtil.padRight("", 1, ' '));
        assertEquals("ab", StringUtil.padRight("ab", 2, ' '));
        assertEquals("ab ", StringUtil.padRight("ab", 3, ' '));
        assertEquals("ab    ", StringUtil.padRight("ab", 6, ' '));
        try {
            StringUtil.padRight("abcde", 2, ' ');
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    public void testPadLeft() {
        assertEquals("", StringUtil.padLeft(null, 0, ' '));
        assertEquals(" ", StringUtil.padLeft(null, 1, ' '));
        assertEquals("", StringUtil.padLeft("", 0, ' '));
        assertEquals(" ", StringUtil.padLeft("", 1, ' '));
        assertEquals("ab", StringUtil.padLeft("ab", 2, ' '));
        assertEquals(" ab", StringUtil.padLeft("ab", 3, ' '));
        assertEquals("    ab", StringUtil.padLeft("ab", 6, ' '));
        try {
            StringUtil.padLeft("abcde", 2, ' ');
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // this is expected
        }
    }

    public void testTrimRight() {
        assertEquals(null, StringUtil.trimRight(null, '0'));
        assertEquals("", StringUtil.trimRight("", '0'));
        assertEquals("", StringUtil.trimRight("0", '0'));
        assertEquals("1", StringUtil.trimRight("1", '0'));
        assertEquals("1", StringUtil.trimRight("10", '0'));
        assertEquals("1", StringUtil.trimRight("1000", '0'));
        assertEquals("01", StringUtil.trimRight("0100", '0'));
    }

    public void testTrimLeft() {
        assertEquals(null, StringUtil.trimLeft(null, '0'));
        assertEquals("", StringUtil.trimLeft("", '0'));
        assertEquals("", StringUtil.trimLeft("0", '0'));
        assertEquals("1", StringUtil.trimLeft("1", '0'));
        assertEquals("1", StringUtil.trimLeft("01", '0'));
        assertEquals("1", StringUtil.trimLeft("001", '0'));
        assertEquals("100", StringUtil.trimLeft("0100", '0'));
    }

    public void testTrim() {
        assertEquals(null, StringUtil.trim(null, '0'));
        assertEquals("", StringUtil.trim("", '0'));
        assertEquals("", StringUtil.trim("0", '0'));
        assertEquals("1", StringUtil.trim("1", '0'));
        assertEquals("1", StringUtil.trim("10", '0'));
        assertEquals("1", StringUtil.trim("100", '0'));
        assertEquals("1", StringUtil.trim("00100", '0'));
    }
    
    public void testStartsWithIgnoreCase() {
    	assertFalse(StringUtil.startsWithIgnoreCase("", null));
    	assertTrue(StringUtil.startsWithIgnoreCase(null, null));
    	assertFalse(StringUtil.startsWithIgnoreCase(null, ""));
    	assertTrue(StringUtil.startsWithIgnoreCase("", ""));
    	assertTrue(StringUtil.startsWithIgnoreCase("A", ""));
    	assertTrue(StringUtil.startsWithIgnoreCase("Alice", "Alice"));
    	assertTrue(StringUtil.startsWithIgnoreCase("Alice", "aLICE"));
    	assertTrue(StringUtil.startsWithIgnoreCase("Alice", "Ali"));
    	assertTrue(StringUtil.startsWithIgnoreCase("Alice", "aLI"));
    	assertTrue(StringUtil.startsWithIgnoreCase("aLICE", "Ali"));
    	assertFalse(StringUtil.startsWithIgnoreCase("Ali", "Alice"));
    }
    
    public void testEndsWithIgnoreCase() {
    	assertFalse(StringUtil.endsWithIgnoreCase("", null));
    	assertTrue(StringUtil.endsWithIgnoreCase(null, null));
    	assertFalse(StringUtil.endsWithIgnoreCase(null, ""));
    	assertTrue(StringUtil.endsWithIgnoreCase("", ""));
    	assertTrue(StringUtil.endsWithIgnoreCase("A", ""));
    	assertTrue(StringUtil.endsWithIgnoreCase("Alice", "Alice"));
    	assertTrue(StringUtil.endsWithIgnoreCase("Alice", "aLICE"));
    	assertTrue(StringUtil.endsWithIgnoreCase("Alice", "ice"));
    	assertTrue(StringUtil.endsWithIgnoreCase("Alice", "ICE"));
    	assertTrue(StringUtil.endsWithIgnoreCase("aLICE", "ice"));
    	assertFalse(StringUtil.endsWithIgnoreCase("ice", "Alice"));
    }
    
    public void testNormalizeName() {
    	assertEquals("Alice", StringUtil.normalizeName("Alice"));
    	assertEquals("Alice", StringUtil.normalizeName("ALICE"));
    	assertEquals("Alice", StringUtil.normalizeName("alice"));
    	assertEquals("Alice", StringUtil.normalizeName("aLICE"));
    	assertEquals("Alice", StringUtil.normalizeName(" \r\n\tAlice"));
    	assertEquals("Alice", StringUtil.normalizeName(" \r\n\tALICE"));
    	assertEquals("Alice", StringUtil.normalizeName(" \r\n\talice"));
    	assertEquals("Alice", StringUtil.normalizeName(" \r\n\taLICE"));
    	assertEquals("Alice Smith", StringUtil.normalizeName(" \r\n\taLICE \r sMITH \n\t "));
    }
    
    public void testSplitOnLastSeparator() {
    	checkSplit(null , null, null   );
    	checkSplit(null , "c" , "c"    );
    	checkSplit("b"  , ""  , "b."  );
    	checkSplit(""   , "c" , ".c"  );
    	checkSplit("b"  , "c" , "b.c"  );
    	checkSplit("a.b", "c" , "a.b.c");
    }

	private void checkSplit(String parent, String child, String path) {
		assertTrue(Arrays.equals(ArrayUtil.buildArrayOfType(String.class, parent, child), StringUtil.splitOnLastSeparator(path, '.')));
	}
}
