/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.regex;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.databene.commons.CharSet;
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.databene.formats.regex.CharRange;
import org.databene.formats.regex.Choice;
import org.databene.formats.regex.CustomCharClass;
import org.databene.formats.regex.Factor;
import org.databene.formats.regex.Group;
import org.databene.formats.regex.RegexChar;
import org.databene.formats.regex.RegexCharClass;
import org.databene.formats.regex.RegexParser;
import org.databene.formats.regex.RegexPart;
import org.databene.formats.regex.RegexString;
import org.databene.formats.regex.Sequence;
import org.databene.formats.regex.SimpleCharSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link RegexParser}.<br/><br/>
 * Created: 18.08.2006 21:56:01
 * @since 0.1
 * @author Volker Bergmann
 */
public class RegexParserTest {

	private static Logger logger = LoggerFactory.getLogger(RegexParserTest.class);
    
	@Test
    public void testEmpty() throws Exception {
        check(null, null, 0, 0);
        check("", new RegexString(""), 0, 0);
    }

	@Test
    public void testSpecialCharacters() throws Exception {
        checkChar("\\+", '+');
        checkChar("\\-", '-');
        checkChar("\\*", '*');
        checkChar("\\?", '?');
        checkChar("\\\\", '\\');
        checkChar("\\.", '.');
        checkChar("\\,", ',');
        checkChar("\\?", '?');
        checkChar("\\&", '&');
        checkChar("\\^", '^');
        checkChar("\\$", '$');
        checkChar("\\t", '\t');
        checkChar("\\n", '\n');
        checkChar("\\r", '\r');
        checkChar("\\f", '\u000C');
        checkChar("\\a", '\u0007');
        checkChar("\\e", '\u001B');
    }

	@Test
    public void testHexCharacter() throws Exception {
        checkChar("\\xfe",   (char) 0xfe);
        checkChar("\\ufedc", (char) 0xfedc);
    }

	@Test
    public void testOctalCharacter() throws Exception {
        checkChar("\\0123",  (char) 0123);
    }

	@Test
    public void testCodedCharacter() throws Exception {
        checkChar("\\cB",    (char) 1);
    }

	@Test
    public void testCustomClasses() throws Exception {
        check("[a-c]", new CustomCharClass(CollectionUtil.toList(new CharRange("a-c", 'a', 'c'))), 1, 1);
        check("[a-cA-C]", new CustomCharClass(
        		CollectionUtil.toList(new CharRange("a-c", 'a', 'c'), new CharRange("A-C", 'A', 'C'))), 1, 1);
        check("[^\\w]", new CustomCharClass(
        		CollectionUtil.toList(new SimpleCharSet(".", new CharSet().addAnyCharacters().getSet())),
        		CollectionUtil.toList(new SimpleCharSet("\\w", new CharSet().addWordChars().getSet()))
        	), 1, 1);
    }

	@Test(expected = SyntaxError.class)
    public void testInvalidCustomClass() {
		new RegexParser().parseRegex("[a-f");
    }

	@Test
    public void testPredefClasses() throws Exception {
        check(".", new SimpleCharSet(".", new CharSet().addAnyCharacters().getSet()), 1, 1);
        check("\\d", new SimpleCharSet("\\d", new CharSet().addDigits().getSet()), 1, 1);
        check("\\s", new SimpleCharSet("\\s", new CharSet().addWhitespaces().getSet()), 1, 1);
        check("\\w", new SimpleCharSet("\\w", new CharSet().addWordChars().getSet()), 1, 1);
    }

	@Test(expected = SyntaxError.class)
    public void testInvalidPredefClass() {
		new RegexParser().parseRegex("\\X");
    }

	@Test
    public void testQuantifiers() throws Exception {
        check("a",      new RegexChar('a'),                      1, 1);
        check("a?",     new Factor(new RegexChar('a'), 0, 1),    0, 1);
        check("a*",     new Factor(new RegexChar('a'), 0, null), 0, null);
        check("a+",     new Factor(new RegexChar('a'), 1, null), 1, null);

        check("a{3}",   new Factor(new RegexChar('a'), 3,  3),   3, 3);
        check("a{3,}",  new Factor(new RegexChar('a'), 3, null), 3, null);
        check("a{3,5}", new Factor(new RegexChar('a'), 3,  5),   3, 5);
    }
    
	@Test(expected = SyntaxError.class)
    public void testInvalidQuantifier() throws Exception {
		new RegexParser().parseRegex("a{,4}");
    }

	@Test
    public void testClassAndQuantifierSequences() throws Exception {
        check("\\w+\\d+", new Sequence(
                new Factor(new SimpleCharSet("\\w", new CharSet().addWordChars().getSet()), 1, null),
                new Factor(new SimpleCharSet("\\d", new CharSet().addDigits().getSet()), 1, null)
        	), 2, null);
        check("[a-c][A-C]", new Sequence(
                new CustomCharClass(CollectionUtil.toList(new CharRange("a-c", 'a', 'c'))),
                new CustomCharClass(CollectionUtil.toList(new CharRange("A-C", 'A', 'C')))
    	), 2, 2);
		
		RegexCharClass CS_DIGIT = new SimpleCharSet("\\d", new CharSet().addDigits().getSet());
		CharRange CS_0_9 = new CharRange("0-9", '0', '9');
	    CustomCharClass CC_0_9 = new CustomCharClass(CollectionUtil.toList(CS_0_9));

	    RegexCharClass CS_POS_DIGIT = new CharRange("1-9", '1', '9');
	    CustomCharClass CC_POS_DIGIT = new CustomCharClass(CollectionUtil.toList(CS_POS_DIGIT));

        check("\\+[1-9]\\d{1,2}/\\d+/\\d+", new Sequence(
                new RegexChar('+'),
                CC_POS_DIGIT,
                new Factor(CS_DIGIT, 1, 2),
                new RegexChar('/'),
                new Factor(CS_DIGIT, 1, null),
                new RegexChar('/'),
                new Factor(CS_DIGIT, 1, null)
    	), 7, null);

        check("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", new Sequence(
                new Factor(CC_0_9, 1, 3),
                new RegexChar('.'),
                new Factor(CC_0_9, 1, 3),
                new RegexChar('.'),
                new Factor(CC_0_9, 1, 3),
                new RegexChar('.'),
                new Factor(CC_0_9, 1, 3)
    	), 7, 15);
    }

	@Test
    public void testGroups() throws Exception {
        check("(a)", new Group(new RegexChar('a')), 1, 1);

        check("(ab)", new Group(new Sequence(new RegexChar('a'), new RegexChar('b'))), 2, 2);

        check("(a)*", new Factor(new Group(new RegexChar('a')), 0, null), 0, null);

        check("(a?b+)*",
        	new Factor(new Group(new Sequence(
                new Factor(new RegexChar('a'), 0, 1),
                new Factor(new RegexChar('b'), 1, null)
        	)), 
        	0, null), 0, null);

        check("(a{1}b{2,3}){4,5}", 
        	new Factor(new Group(
	        	new Sequence(
	                new Factor(new RegexChar('a'), 1, 1),
	                new Factor(new RegexChar('b'), 2, 3)
	        	)), 
	        	4, 5
        	), 12, 20
        );
    }

	@Test
    public void testChoices() throws Exception {
        check("(a|b)", new Group(new Choice(new RegexChar('a'), new RegexChar('b'))), 1, 1);
        check("(a?|b+)*", 
        	new Factor(
        		new Group(new Choice(
        				new Factor(new RegexChar('a'), 0, 1),
        				new Factor(new RegexChar('b'), 1, null)
        		)), 
        		0, null
        	), 0, null);
        check("(a{1,2}|b)", 
            new Group(new Choice(
                new Factor(new RegexChar('a'), 1, 2),
                new RegexChar('b'))
            ), 1, 2);
    }
	
	@Test
    public void testRecursion() throws Exception {
        check("(a{1,2}|b){1,3}", 
        	new Factor(
	            new Group(new Choice(
	                new Factor(new RegexChar('a'), 1, 2),
	                new RegexChar('b'))), 
	            1, 3
	        ), 1, 6);
    }

    private static void checkChar(String pattern, char expectedChar) throws Exception {
    	check(pattern, new RegexChar(expectedChar), 1, 1);
    }

    private static void check(String pattern, Object expectedPart, int expMinLength, Integer expMaxLength) throws Exception {
        logger.debug("checking " + pattern);
        RegexPart result = new RegexParser().parseRegex(pattern);
        logger.debug("parsed as: " + StringUtil.normalize(String.valueOf(result)));
        if (pattern == null)
            assertEquals(expectedPart, result);
        else if (result == null)
            assertNull(expectedPart);
        else
            assertEquals(expectedPart, result);
        if (pattern != null) {
	        assertEquals("Wrong minLength.", expMinLength, result.minLength());
	        assertEquals("Wrong maxLength.", expMaxLength, result.maxLength());
        }
    }

}
