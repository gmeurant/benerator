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
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link RegexParser}.<br/><br/>
 * Created: 18.08.2006 21:56:01
 * @since 0.1
 * @author Volker Bergmann
 */
public class RegexParserTest extends TestCase {

	private static final CharSet CS_DIGIT = new CharSet("\\d", '0', '9');
    private static final CharSet CS_POS_DIGIT = new CharSet("1-9", '1', '9');

    private static final CustomCharClass CC_DIGIT = new CustomCharClass(CollectionUtil.toList(CS_DIGIT));
    private static final CustomCharClass CC_POS_DIGIT = new CustomCharClass(CollectionUtil.toList(CS_POS_DIGIT));

	private static Logger logger = LoggerFactory.getLogger(RegexParserTest.class);
    
    public void testEmpty() throws ParseException {
        check(null, null);
        check("", "");
    }

    public void testSpecialCharacters() throws ParseException {
        check("\\+", '+');
        check("\\-", '-');
        check("\\*", '*');
        check("\\?", '?');
        check("\\\\", '\\');
        check("\\.", '.');
        check("\\,", ',');
        check("\\?", '?');
        check("\\&", '&');
        check("\\^", '^');
        check("\\$", '$');
        check("\\t", '\t');
        check("\\n", '\n');
        check("\\r", '\r');
        check("\\f", '\u000C');
        check("\\a", '\u0007');
        check("\\e", '\u001B');
    }

    public void testHexCharacter() throws ParseException {
        check("\\xfe",   (char) 0xfe);
        check("\\ufedc", (char) 0xfedc);
    }

    public void testOctalCharacter() throws ParseException {
        check("\\0123",  (char) 0123);
    }

    public void testCodedCharacter() throws ParseException {
        check("\\cB",    (char) 1);
    }

    public void testCustomClasses() throws ParseException {
        check("[a-c]", new CustomCharClass(CollectionUtil.toList(new CharSet('a', 'c'))));
        check("[a-cA-C]", new CustomCharClass(CollectionUtil.toList(new CharSet('a', 'c'), new CharSet('A', 'C'))));
        check("[^\\w]", new CustomCharClass(
        		CollectionUtil.toList(new CharSet().addAnyCharacters()),
        		CollectionUtil.toList(new CharSet().addWordChars())
        	));
    }

    public void testInvalidCustomClass() {
        try {
			new RegexParser().parse("[a-f");
			fail("ParseException expected");
		} catch (ParseException e) {
			// this is expected
		}
    }

    public void testPredefClasses() throws ParseException {
        check(".", new CharSet().addAnyCharacters());
        check("\\d", new CharSet().addDigits());
        check("\\s", new CharSet().addWhitespaces());
        check("\\w", new CharSet().addWordChars());
    }

    public void testInvalidPredefClass() {
        try {
			new RegexParser().parse("\\X");
			fail("ParseException expected");
		} catch (ParseException e) {
			// this is expected
		}
    }

    public void testQuantifiers() throws ParseException {
        check("a",      'a');
        check("a?",     new Factor('a', 0, 1));
        check("a*",     new Factor('a', 0, null));
        check("a+",     new Factor('a', 1, null));

        check("a{3}",   new Factor('a', 3,  3));
        check("a{3,}",  new Factor('a', 3, null));
        check("a{3,5}", new Factor('a', 3,  5));
    }
    
/* TODO make the RegexParser fail on illegal syntax
    public void testInvalidQuantifier() {
        try {
			new RegexParser().parse("a{,4}");
			fail("ParseException expected");
		} catch (ParseException e) {
			// this is expected
		}
    }
*/
    public void testClassAndQuantifierSequences() throws ParseException {
        check("\\w+\\d+", new Sequence(
                new Factor(new CharSet().addWordChars(), 1, null),
                new Factor(new CharSet().addDigits(), 1, null)
        	));
        check("[a-c][A-C]", new Sequence(
                new CustomCharClass(CollectionUtil.toList(new CharSet().addRange('a', 'c'))),
                new CustomCharClass(CollectionUtil.toList(new CharSet().addRange('A', 'C')))
    	));

        check("\\+[1-9]\\d{1,2}/\\d+/\\d+", new Sequence(
                '+',
                CC_POS_DIGIT,
                new Factor(CS_DIGIT, 1, 2),
                '/',
                new Factor(CS_DIGIT, 1, null),
                '/',
                new Factor(CS_DIGIT, 1, null)
    	));

        check("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", new Sequence(
                new Factor(CC_DIGIT, 1, 3),
                '.',
                new Factor(CC_DIGIT, 1, 3),
                '.',
                new Factor(CC_DIGIT, 1, 3),
                '.',
                new Factor(CC_DIGIT, 1, 3)
    	));
    }

    public void testGroups() throws ParseException {
        check("(a)", 'a');

        check("(ab)", new Sequence('a', 'b'));

        check("(a)*", new Factor('a', 0, null));

        check("(a?b+)*",
        	new Factor(new Sequence(
                new Factor('a', 0, 1),
                new Factor('b', 1, null)
        	), 
        	0, null));

        check("(a{1}b{2,3}){4,5}", 
        	new Factor(
	        	new Sequence(
	                new Factor('a', 1, 1),
	                new Factor('b', 2, 3)
	        	), 
	        	4, 5
        	)
        );
    }

    public void testChoices() throws ParseException {
        check("(a|b)", new Choice('a', 'b'));
        check("(a?|b+)*", 
        	new Factor(
        		new Choice(
        				new Factor('a', 0, 1),
        				new Factor('b', 1, null)
        		), 
        		0, null
        	));
    }

    public void testRecursion() throws ParseException {
        check("(a{1,2}|b){1,3}", 
        	new Factor(
	            new Choice(
	                new Factor('a', 1, 2),
	                'b'), 
	            1, 3
	        ));
    }

    private void check(String pattern, Object expectedPart) throws ParseException {
        logger.debug("checking " + pattern);
        Object result = new RegexParser().parse(pattern);
        logger.debug("parsed as: " + StringUtil.normalize(String.valueOf(result)));
        if (pattern == null)
            assertEquals(expectedPart, result);
        else if (result == null)
            assertNull(expectedPart);
        else
            assertEquals(expectedPart, result);
    }

}
