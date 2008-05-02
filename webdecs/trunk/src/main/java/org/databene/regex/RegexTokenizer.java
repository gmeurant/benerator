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

package org.databene.regex;

import static org.databene.regex.RegexTokenType.*;

import java.text.ParseException;
import java.util.Map;

import org.databene.commons.CollectionUtil;
import org.databene.commons.StringCharacterIterator;

/**
 * Tokenizer for regular expressions.<br/>
 * <br/>
 * Created: 20.08.2006 10:33:20
 */
public class RegexTokenizer {

    private static final String QUANTIFIERS = "?*+";
    
    private static final Map<Character, State> escapedTokens = CollectionUtil.buildMap(
    		'd',  new State(PREDEFINED_CLASS, 'd'),
    		'D',  new State(PREDEFINED_CLASS, 'D'),
    		'W',  new State(PREDEFINED_CLASS, 'W'),
    		'w',  new State(PREDEFINED_CLASS, 'w'),
    		's',  new State(PREDEFINED_CLASS, 's'),
    		'S',  new State(PREDEFINED_CLASS, 'S'),

    		'+',  new State(CHARACTER, '+'),
    		'-',  new State(CHARACTER, '-'),
    		'\\', new State(CHARACTER, '\\'),
    		'.',  new State(CHARACTER, '.'),
    
    		't', new State(CHARACTER, '\t'),
    		'n', new State(CHARACTER, '\n'),
    		'r', new State(CHARACTER, '\r'),
    		'f', new State(CHARACTER, '\u000C'),
    		'a', new State(CHARACTER, '\u0007'),
    		'e', new State(CHARACTER, '\u001B')
   		);

    private final StringCharacterIterator iterator;

    public char cval;
    public RegexTokenType ttype;
//    public int nval;
    boolean pushedBack;

    public RegexTokenizer(String regex) {
        this.iterator = new StringCharacterIterator(regex);
        pushedBack = false;
    }

    public RegexTokenType next() throws ParseException {
        if (pushedBack) {
            pushedBack = false;
            return ttype;
        }
        if (!iterator.hasNext())
            return END;
        char c = iterator.next();
        if (c == '.')
            setState(PREDEFINED_CLASS, '.');
        else if (c == '\\') {
        	// parse escaped character
            char c2 = iterator.next();
            State state = escapedTokens.get(c2);
            if (state != null)
            	setState(state.ttype, state.cval);
            else {
	            switch (c2) {
	                case 'x': parseHexCharacter(1); break;
	                case 'u': parseHexCharacter(2); break;
	                case '0': parseOctalCharacters(); break;
	                case 'c': setState(CHARACTER, (char)(Character.toUpperCase(iterator.next()) - 'A')); break;
	                default : throw new ParseException("unknown token: \\" + c2, iterator.index());
	            }
            }
        } else if (QUANTIFIERS.indexOf(c) >= 0)
            setState(QUANTIFIER, c);
        else if (Character.isDigit(c)) {
            setState(DIGIT, c);
        } else {
            switch (c) {
                case '(' : setState(GROUP_START, c); break;
                case ')' : setState(GROUP_END, c); break;
                case '|' : setState(ALTERNATIVE_SEPARATOR, c); break;
                case '[' : setState(SET_START, c); break;
                case ']' : setState(SET_END, c); break;
                case '{' : setState(QUANTIFIER_START, c); break;
                case '}' : setState(QUANTIFIER_END, c); break;
                case '^' : setState(NEGATION, c); break;
                default: setState(CHARACTER, c); break;
            }
        }
        return ttype;
    }

    private void parseHexCharacter(int bytes) {
        int result = 0;
        for (int i = 0; i < bytes; i++)
            result = result * 16 + iterator.next() - '0';
        setState(CHARACTER, (char)result);
    }

    private void parseOctalCharacters() {
        int result = 0;
        char c;
        for (int i = 0; i < 3 && iterator.hasNext() && ((c = iterator.next()) >= '0') && c <='7'; i++)
            result = result * 8 + c - '0';
        setState(CHARACTER, (char)result);
    }

    private void setState(RegexTokenType ttype, char cval) {
        this.ttype = ttype;
        this.cval = cval;
//        this.nval = nval;
    }

    public boolean hasNext() {
        return (pushedBack || iterator.hasNext());
    }

    public void pushBack() {
        if (pushedBack)
            throw new IllegalStateException("Cannot pushBack() twice");
        pushedBack = true;
    }

    public int index() {
        return iterator.index();
    }
/*
    private int parseInt() {
        int result = 0;
        char c;
        while (Character.isDigit(c = iterator.next()))
            result = result * 10 + c - '0';
        iterator.pushBack();
        return result;
    }
*/

    public void assertNext(RegexTokenType expectedType) throws ParseException {
        RegexTokenType type = next();
        if (type != expectedType)
            throw new ParseException("Expected " + expectedType + " found: " + type, iterator.index());
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + iterator + ']';
    }
    
    private static final class State {
    	RegexTokenType ttype; 
    	char cval;
		public State(RegexTokenType ttype, char cval) {
			super();
			this.ttype = ttype;
			this.cval = cval;
		}
    }
}
