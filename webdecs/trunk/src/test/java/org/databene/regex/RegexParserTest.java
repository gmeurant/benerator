/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.regex;

import junit.framework.TestCase;

import java.text.ParseException;

import org.databene.commons.CharSet;
import org.databene.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link RegexParser}
 * Created: 18.08.2006 21:56:01
 * @author Volker Bergmann
 */
public class RegexParserTest extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(RegexParserTest.class);

    public void testEmpty() throws ParseException {
        check(null);
        check("",      new RegexPart(new CharSet().getSet(), new Quantifier(0, 0)));
    }

    public void testSpecialCharacters() throws ParseException {
        checkSpecialCharacter("\\+", '+');
        checkSpecialCharacter("\\-", '-');
        checkSpecialCharacter("\\\\", '\\');
        checkSpecialCharacter("\\.", '.');
        checkSpecialCharacter("\\t", '\t');
        checkSpecialCharacter("\\n", '\n');
        checkSpecialCharacter("\\r", '\r');
        checkSpecialCharacter("\\f", '\u000C');
        checkSpecialCharacter("\\a", '\u0007');
        checkSpecialCharacter("\\e", '\u001B');
    }

    public void testSpecialCharacterFormats() throws ParseException {
        checkSpecialCharacter("\\x1", (char) 0x1);
        checkSpecialCharacter("\\u11", (char) 0x11);
        checkSpecialCharacter("\\01", (char) 1);
        checkSpecialCharacter("\\cB", (char) 1);
    }

	private void checkSpecialCharacter(String pattern, char c) throws ParseException {
		check(pattern, new RegexPart(new CharSet(c, c).getSet(), new Quantifier(1, 1)));
	}

    public void testCustomClasses() throws ParseException {
        check("[a-c]", new RegexPart(new CharSet('a', 'c').getSet(), new Quantifier(1, 1)));
        check("[a-cA-C]", new RegexPart(new CharSet('a', 'c').addRange('A', 'C').getSet(), new Quantifier(1, 1)));
        check(".", new RegexPart(new CharSet().addAnyCharacters().getSet(), new Quantifier(1, 1)));
    }

    public void testPredefClasses() throws ParseException {
        check("\\d", new RegexPart(new CharSet().addDigits().getSet(), new Quantifier(1, 1)));
        check("\\s", new RegexPart(new CharSet().addWhitespaces().getSet(), new Quantifier(1, 1)));
        check("\\w", new RegexPart(new CharSet().addWordChars().getSet(), new Quantifier(1, 1)));
        check("[^\\w]", new RegexPart(new CharSet().addAnyCharacters().removeWordChars().getSet(), new Quantifier(1, 1)));
    }

    public void testInvalidClass() {
        try {
			new RegexParser().parse("\\/");
			fail("ParseException expected");
		} catch (ParseException e) {
			// this is expected
		}
    }

    public void testClassAndQuantifierCombinations() throws ParseException {
        check("\\w+\\d+", new RegexPart[] {
                new RegexPart(new CharSet().addWordChars().getSet(), new Quantifier(1, -1)),
                new RegexPart(new CharSet().addDigits().getSet(), new Quantifier(1, -1))
        });
        check("[a-c][A-C]", new RegexPart[] {
                new RegexPart(new CharSet().addRange('a', 'c').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet().addRange('A', 'C').getSet(), new Quantifier(1, 1))
        });

        check("\\+[1-9]\\d{1,2}/\\d+/\\d+", new RegexPart[] {
                new RegexPart(new CharSet('+').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('1', '9').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet().addDigits().getSet(), new Quantifier(1, 2)),
                new RegexPart(new CharSet('/').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet().addDigits().getSet(), new Quantifier(1, -1)),
                new RegexPart(new CharSet('/').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet().addDigits().getSet(), new Quantifier(1, -1))
        });

        check("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", new RegexPart[] {
                new RegexPart(new CharSet('0', '9').getSet(), new Quantifier(1, 3)),
                new RegexPart(new CharSet('.').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('0', '9').getSet(), new Quantifier(1, 3)),
                new RegexPart(new CharSet('.').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('0', '9').getSet(), new Quantifier(1, 3)),
                new RegexPart(new CharSet('.').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('0', '9').getSet(), new Quantifier(1, 3)),
        });
    }

    public void testQuantifiers() throws ParseException {
        check("a",      new RegexPart(new CharSet('a').getSet(), new Quantifier(1,  1)));
        check("a?",     new RegexPart(new CharSet('a').getSet(), new Quantifier(0,  1)));
        check("a*",     new RegexPart(new CharSet('a').getSet(), new Quantifier(0, -1)));
        check("a+",     new RegexPart(new CharSet('a').getSet(), new Quantifier(1, -1)));
        check("[a]",    new RegexPart(new CharSet('a').getSet(), new Quantifier(1,  1)));

        check("a{3}",   new RegexPart(new CharSet('a').getSet(), new Quantifier(3,  3)));
        check("a{3,}",  new RegexPart(new CharSet('a').getSet(), new Quantifier(3, -1)));
        check("a{3,5}", new RegexPart(new CharSet('a').getSet(), new Quantifier(3,  5)));
    }

    public void testGroups() throws ParseException {
        check("(a)", new RegexPart(new Group(new Regex(
                new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 1))
        ))));

        check("(ab)",      new RegexPart(new Group(new Regex(
                new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('b').getSet(), new Quantifier(1, 1))
        ))));

        check("(a)*",      new RegexPart(new Group(new Regex(
                new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 1))
        )), new Quantifier(0, -1)));

        check("(a?b+)*",      new RegexPart(new Group(new Regex(
                new RegexPart(new CharSet('a').getSet(), new Quantifier(0, 1)),
                new RegexPart(new CharSet('b').getSet(), new Quantifier(1, -1))
        )), new Quantifier(0, -1)));

        check("(a{1}b{2,3}){4,5}",      new RegexPart(new Group(new Regex(
                new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 1)),
                new RegexPart(new CharSet('b').getSet(), new Quantifier(2, 3))
        )), new Quantifier(4, 5)));
    }

    public void testAlternatives() throws ParseException {
        check("(a|b)", new RegexPart(new AlternativePattern(
                new Regex(new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 1))),
                new Regex(new RegexPart(new CharSet('b').getSet(), new Quantifier(1, 1)))
        )));
        check("(a?|b+)*", new RegexPart(new AlternativePattern(
                new Regex(new RegexPart(new CharSet('a').getSet(), new Quantifier(0,  1))),
                new Regex(new RegexPart(new CharSet('b').getSet(), new Quantifier(1, -1)))
        ), new Quantifier(0, -1)));
    }

    public void testRecursions() throws ParseException {
        check("(a{1,2}|b){1,3}", new RegexPart(
            new AlternativePattern(
                new Regex(new RegexPart(new CharSet('a').getSet(), new Quantifier(1, 2))),
                new Regex(new RegexPart(new CharSet('b').getSet(), new Quantifier(1, 1)))), 
            new Quantifier(1, 3)));
    }

    private void check(String pattern, RegexPart expectedPart) throws ParseException {
        logger.debug("checking " + pattern);
        Regex regex = new RegexParser().parse(pattern);
        logger.debug("parsed as: " + StringUtil.normalize(String.valueOf(regex)));
        if (pattern == null)
            assertEquals(expectedPart, regex);
        else if (regex == null)
            assertNull(expectedPart);
        else {
            RegexPart[] foundParts = regex.getParts();
            assertEquals("Only one RegexPart expected in " + pattern, 1, foundParts.length);
            assertEquals(expectedPart, foundParts[0]);
        }
    }

    private void check(String pattern, RegexPart ... expectedParts) throws ParseException {
        logger.debug("checking " + pattern);
        Regex regex = new RegexParser().parse(pattern);
        if (pattern == null) {
            assertEquals(0, expectedParts.length);
            return;
        }
        RegexPart[] foundParts = regex.getParts();
        assertEquals("Size of result list does not match ", expectedParts.length, foundParts.length);
        for (int i = 0; i < expectedParts.length; i++)
            assertEquals("Error in " + (i + 1) + ". part:", expectedParts[i], foundParts[i]);
    }

}
