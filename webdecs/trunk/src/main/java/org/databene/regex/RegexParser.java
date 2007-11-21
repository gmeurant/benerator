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
import org.databene.commons.LocaleUtil;
import org.databene.commons.CharSet;

import java.util.*;
import java.text.ParseException;

/**
 * Parses a regular expression into an object model.<br/>
 * <br/>
 * Created: 18.08.2006 19:10:42
 */
public class RegexParser {

    /**
     * The locale to use for letters.
     */
    private Locale locale;

    // constructors ----------------------------------------------------------------------------------------------------

    public RegexParser() {
        this(LocaleUtil.getFallbackLocale());
    }

    public RegexParser(Locale locale) {
        this.locale = locale;
    }

    // interface -------------------------------------------------------------------------------------------------------

    /**
     * Parses a regular expression from a String and transforms it into an object representation.
     */
    public Regex parse(String pattern) throws ParseException {
        if (pattern == null)
            return null;
        else if (pattern.length() == 0)
            return new Regex(new RegexPart[] {
                    new RegexPart(new CharSetPattern(new HashSet<Character>()), new Quantifier(0, 0))
            });
        RegexTokenizer tokenizer = new RegexTokenizer(pattern);
        return parseRegex(tokenizer);
    }

    /**
     * Parses a regular expression string that represents a character set.
     * @see #parseCharSet(RegexTokenizer)
     */
    public Set<Character> parseCharSet(String regex) throws ParseException {
        return parseCharSet(new RegexTokenizer(regex));
    }

    /**
     * Parses a regular expression that represents a character set.
     * E.g. ., x, \d, \\, [ab], [a-z], [a-zA-Z], [^ab], [^a-zA-Z], [\da-z]
     */
    public Set<Character> parseCharSet(RegexTokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext())
            return null;
        RegexTokenType tokenType = tokenizer.next();
        if (tokenType == SET_START) {
            tokenizer.pushBack();
            return parseCustomClass(tokenizer);
        } else if (tokenType == PREDEFINED_CLASS) {
            tokenizer.pushBack();
            return parsePredefinedClass(tokenizer);
        } else if (tokenType == CHARACTER) {
            return new CharSet(tokenizer.cval).getSet();
        } else if (tokenType == DIGIT) {
            return new CharSet(tokenizer.cval).getSet();
        } else if (tokenType == GROUP_END) {
            tokenizer.pushBack();
            return null;
        } else if (tokenType == ALTERNATIVE_SEPARATOR) {
            tokenizer.pushBack();
            return null;
        } else
            throw new IllegalStateException("Unexpected token: " + tokenType);
    }

    // implementation --------------------------------------------------------------------------------------------------

    /**
     * Parses a regular expression into an object model.
     * &lt;regex&gt; ::= &lt;regex-part&gt; [&lt;regex&gt;]
     */
    private Regex parseRegex(RegexTokenizer tokenizer) throws ParseException {
        List<RegexPart> parts = new ArrayList<RegexPart>();
        RegexPart part;
        while ((part = parsePart(tokenizer)) != null)
            parts.add(part);
        RegexPart[] partArray = new RegexPart[parts.size()];
        return (parts.size() > 0 ? new Regex(parts.toArray(partArray)) : null);
    }

    /**
     * Parses a regular expression part.
     * &lt;regex-part&gt; ::= &lt;sub-pattern&gt; [&lt;quantifier&gt;]
     */
    private RegexPart parsePart(RegexTokenizer tokenizer) throws ParseException {
        SubPattern pattern;
        pattern = parseSubPattern(tokenizer);
        if (pattern == null)
            return null;
        Quantifier quantifier = parseOptionalQuantifier(tokenizer);
        return new RegexPart(pattern, quantifier);
    }

    /**
     * Parses a sub pattern.
     * &lt;sub-pattern&gt; ::= (&lt;group&gt; | &lt;char-set&gt;)
     */
    private SubPattern parseSubPattern(RegexTokenizer tokenizer) throws ParseException {
        SubPattern subPattern;
        if (tokenizer.next() == END) {
            return null;
        } else if (tokenizer.ttype == ALTERNATIVE_SEPARATOR) {
            tokenizer.pushBack();
            return null;
        } else if (tokenizer.ttype == GROUP_END) {
            tokenizer.pushBack();
            return null;
        } else if (tokenizer.ttype == GROUP_START) {
            tokenizer.pushBack();
            subPattern = parseGroup(tokenizer);
        } else {
            tokenizer.pushBack();
            subPattern = new CharSetPattern(parseCharSet(tokenizer));
        }
        if (subPattern == null)
            return null;
        return subPattern;
    }

    /**
     * Parses a group
     */
    private SubPattern parseGroup(RegexTokenizer tokenizer) throws ParseException {
        tokenizer.assertNext(GROUP_START);
        List<Regex> subExpressions = new ArrayList<Regex>();
        Regex subExpression;
        while ((subExpression = parseRegex(tokenizer)) != null) {
            subExpressions.add(subExpression);
            if (tokenizer.next() != ALTERNATIVE_SEPARATOR)
                tokenizer.pushBack();
        }
        tokenizer.assertNext(GROUP_END);
        switch (subExpressions.size()) {
            case 0 : return null;
            case 1 : return new Group(subExpressions.get(0));
            default: return new AlternativePattern(subExpressions);
        }
    }

    /**
     * Parses a custom class.
     * samples: [ab], [a-z], [a-zA-Z], [^ab], [^a-zA-Z]
     * &lt;char-set&gt; ::= [^] &lt;range&gt; [&lt;char-set&gt;]
     */
    private Set<Character> parseCustomClass(RegexTokenizer tokenizer) throws ParseException {
        CharSet set = new CharSet(locale);
        RegexTokenType type = tokenizer.next();
        if (type != SET_START)
            throw new ParseException("Expected '[', found: " + type, tokenizer.index());
        type = tokenizer.next();
        // parse optional negation
        boolean additive;
        if (type == NEGATION) {
            additive = false;
            set.addAnyCharacters();
        } else {
            additive = true;
            tokenizer.pushBack();
        }
        // parse ranges
        while (parseRange(tokenizer, set, additive)) {
            // isEmpty loop
        }

        type = tokenizer.next();
        if (type != SET_END)
            throw new ParseException("Expected ']', found: " + type, tokenizer.index());
        return set.getSet();
    }

    /**
     * Parses a character range.
     * &lt;range&gt; ::= &lt;predef-char-class&gt; | &lt;letter&gt; [- &lt;letter&gt;]
     */
    private boolean parseRange(RegexTokenizer tokenizer, CharSet set, boolean additive) throws ParseException {
        RegexTokenType type = tokenizer.next();
        if (type == SET_END) {
            tokenizer.pushBack();
            return false;
        }

        // check for predefined character classes
        if (type == PREDEFINED_CLASS) {
            tokenizer.pushBack();
            if (additive)
                addPredefCharClass(set, tokenizer);
            else
                removePredefCharClass(set, tokenizer);
            return true;
        }
        tokenizer.pushBack();

        // check for range start or single char
        tokenizer.next();
        char min = tokenizer.cval;

        type = tokenizer.next();
        if (!(type == CHARACTER && tokenizer.cval == '-')) {
            // [a] | [ab]
            tokenizer.pushBack();
            set.add(min);
            return true;
        }
        // [a-z]
        tokenizer.next();
        char max = tokenizer.cval;
        if (additive)
            set.addRange(min, max);
        else
            set.removeRange(min, max);
        return true;
    }

    /**
     * Parses a predefined character class.
     */
    private Set<Character> parsePredefinedClass(RegexTokenizer tokenizer) throws ParseException {
        CharSet set = new CharSet(locale);
        return addPredefCharClass(set, tokenizer).getSet();
    }

    /**
     * Adds all characters of a predefined character class to a set, according to the internal locale.
     */
    private CharSet addPredefCharClass(CharSet set, RegexTokenizer tokenizer) throws ParseException {
        tokenizer.assertNext(PREDEFINED_CLASS);
        char charClass = tokenizer.cval;
        return set.add(getPredefCharClass(charClass, locale));
    }

    /**
     * Removes all characters of a predefined character class to a set, according to the internal locale.
     */
    private CharSet removePredefCharClass(CharSet set, RegexTokenizer tokenizer) throws ParseException {
        tokenizer.assertNext(PREDEFINED_CLASS);
        char charClass = tokenizer.cval;
        return set.remove(getPredefCharClass(charClass, locale));
    }

    /**
     * Returns a set of characters that represent a predefined character class.
     */
    private static Set<Character> getPredefCharClass(char charClass, Locale locale) {
        switch (charClass) {
            case '.' : return CharSet.getAnyCharacters();
            case 'd' : return CharSet.getDigits();
            case 'D' : return CharSet.getNonDigits();
            case 's' : return CharSet.getWhitespaces();
            case 'S' : return CharSet.getNonWhitespaces();
            case 'w' : return CharSet.getWordChars(locale);
            case 'W' : return CharSet.getNonWordChars();
            default: throw new IllegalArgumentException("Unsupported character class: " + charClass);
        }
    }

    /**
     * Parses an optional quantifier, for example ?, *, +, {3}, {2,}, {3,5}
     */
    private Quantifier parseOptionalQuantifier(RegexTokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext())
            return new Quantifier(1, 1);
        RegexTokenType type = tokenizer.next();
        if (type == QUANTIFIER) {
            switch (tokenizer.cval) {
                case '?' : return new Quantifier(0, 1);
                case '*' : return new Quantifier(0, -1);
                case '+' : return new Quantifier(1, -1);
            }
        }

        if (type != QUANTIFIER_START) {
            tokenizer.pushBack();
            return new Quantifier(1, 1);
        }
        tokenizer.assertNext(DIGIT);
        tokenizer.pushBack();
        int min = parseInt(tokenizer);
        int max;
        type = tokenizer.next();
        if (type == CHARACTER && tokenizer.cval == ',') {
            // {n,} or {n,m}
            if (tokenizer.next() == DIGIT) {
                // {n,m}
                tokenizer.pushBack();
                max = parseInt(tokenizer);
            } else {
                // {n,}
                max = -1;
                tokenizer.pushBack();
            }
        } else {
            // {n}
            max = min;
            tokenizer.pushBack();
        }
        tokenizer.assertNext(QUANTIFIER_END);
        return new Quantifier(min, max);
    }

    /**
     * Parses an integral number
     */
    private int parseInt(RegexTokenizer tokenizer) throws ParseException {
        int result = 0;
        while (tokenizer.next() == DIGIT)
            result = result * 10 + tokenizer.cval - '0';
        tokenizer.pushBack();
        return result;
    }

}
