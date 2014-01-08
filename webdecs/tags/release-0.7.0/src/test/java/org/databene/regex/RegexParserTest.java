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

package org.databene.regex;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.databene.commons.CharSet;
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link RegexParser}.<br/><br/>
 * Created: 18.08.2006 21:56:01
 * @since 0.1
 * @author Volker Bergmann
 */
public class RegexParserTest {

	private static final CharSet CS_DIGIT = new CharSet("\\d", '0', '9');
    private static final CharSet CS_POS_DIGIT = new CharSet("1-9", '1', '9');

    private static final CustomCharClass CC_DIGIT = new CustomCharClass(CollectionUtil.toList(CS_DIGIT));
    private static final CustomCharClass CC_POS_DIGIT = new CustomCharClass(CollectionUtil.toList(CS_POS_DIGIT));

	private static Logger logger = LoggerFactory.getLogger(RegexParserTest.class);
    
	@Test
    public void testEmpty() throws Exception {
        check(null, null);
        check("", "");
    }

	@Test
    public void testSpecialCharacters() throws Exception {
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

	@Test
    public void testHexCharacter() throws Exception {
        check("\\xfe",   (char) 0xfe);
        check("\\ufedc", (char) 0xfedc);
    }

	@Test
    public void testOctalCharacter() throws Exception {
        check("\\0123",  (char) 0123);
    }

	@Test
    public void testCodedCharacter() throws Exception {
        check("\\cB",    (char) 1);
    }

	@Test
    public void testCustomClasses() throws Exception {
        check("[a-c]", new CustomCharClass(CollectionUtil.toList(new CharSet('a', 'c'))));
        check("[a-cA-C]", new CustomCharClass(CollectionUtil.toList(new CharSet('a', 'c'), new CharSet('A', 'C'))));
        check("[^\\w]", new CustomCharClass(
        		CollectionUtil.toList(new CharSet().addAnyCharacters()),
        		CollectionUtil.toList(new CharSet().addWordChars())
        	));
    }

	@Test(expected = SyntaxError.class)
    public void testInvalidCustomClass() {
		new RegexParser().parseRegex("[a-f");
    }

	@Test
    public void testPredefClasses() throws Exception {
        check(".", new CharSet().addAnyCharacters());
        check("\\d", new CharSet().addDigits());
        check("\\s", new CharSet().addWhitespaces());
        check("\\w", new CharSet().addWordChars());
    }

	@Test(expected = SyntaxError.class)
    public void testInvalidPredefClass() {
		new RegexParser().parseRegex("\\X");
    }

	@Test
    public void testQuantifiers() throws Exception {
        check("a",      'a');
        check("a?",     new Factor('a', 0, 1));
        check("a*",     new Factor('a', 0, null));
        check("a+",     new Factor('a', 1, null));

        check("a{3}",   new Factor('a', 3,  3));
        check("a{3,}",  new Factor('a', 3, null));
        check("a{3,5}", new Factor('a', 3,  5));
    }
    
	@Test(expected = SyntaxError.class)
    public void testInvalidQuantifier() throws Exception {
		new RegexParser().parseRegex("a{,4}");
    }

	@Test
    public void testClassAndQuantifierSequences() throws Exception {
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

	@Test
    public void testGroups() throws Exception {
        check("(a)", new Group('a'));

        check("(ab)", new Group(new Sequence('a', 'b')));

        check("(a)*", new Factor(new Group('a'), 0, null));

        check("(a?b+)*",
        	new Factor(new Group(new Sequence(
                new Factor('a', 0, 1),
                new Factor('b', 1, null)
        	)), 
        	0, null));

        check("(a{1}b{2,3}){4,5}", 
        	new Factor(new Group(
	        	new Sequence(
	                new Factor('a', 1, 1),
	                new Factor('b', 2, 3)
	        	)), 
	        	4, 5
        	)
        );
    }

	@Test
    public void testChoices() throws Exception {
        check("(a|b)", new Group(new Choice('a', 'b')));
        check("(a?|b+)*", 
        	new Factor(
        		new Group(new Choice(
        				new Factor('a', 0, 1),
        				new Factor('b', 1, null)
        		)), 
        		0, null
        	));
    }

	@Test
    public void testRecursion() throws Exception {
        check("(a{1,2}|b){1,3}", 
        	new Factor(
	            new Group(new Choice(
	                new Factor('a', 1, 2),
	                'b')), 
	            1, 3
	        ));
    }

    private void check(String pattern, Object expectedPart) throws Exception {
        logger.debug("checking " + pattern);
        Object result = new RegexParser().parseRegex(pattern);
        logger.debug("parsed as: " + StringUtil.normalize(String.valueOf(result)));
        if (pattern == null)
            assertEquals(expectedPart, result);
        else if (result == null)
            assertNull(expectedPart);
        else
            assertEquals(expectedPart, result);
    }

}
