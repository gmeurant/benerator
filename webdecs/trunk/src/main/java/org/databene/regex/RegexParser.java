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

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.databene.commons.Assert;
import org.databene.commons.CharSet;
import org.databene.commons.CollectionUtil;
import org.databene.commons.LocaleUtil;
import org.databene.regex.antlr.RegexLexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a regular expression into an object model.<br/>
 * <br/>
 * Created: 18.08.2006 19:10:42
 * @since 0.1
 * @author Volker Bergmann
 */
public class RegexParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexParser.class);

    private Locale locale;

    // constructors ----------------------------------------------------------------------------------------------------

    public RegexParser() {
        this(LocaleUtil.getFallbackLocale());
    }

    public RegexParser(Locale locale) {
        this.locale = locale;
    }

    // interface -------------------------------------------------------------------------------------------------------

    public Object parseRegex(String pattern) throws ParseException {
        if (pattern == null)
            return null;
        if (pattern.length() == 0)
            return "";
        try {
	        RegexLexer lex = new RegexLexer(new ANTLRReaderStream(new StringReader(pattern)));
	        CommonTokenStream tokens = new CommonTokenStream(lex);
	        org.databene.regex.antlr.RegexParser parser = new org.databene.regex.antlr.RegexParser(tokens);
	        org.databene.regex.antlr.RegexParser.expression_return r = parser.expression();
	        if (parser.getNumberOfSyntaxErrors() > 0)
	        	throw new ParseException("Illegal regex: " + pattern, -1);
	        if (r != null) {
	        	CommonTree tree = (CommonTree) r.getTree();
	        	if (LOGGER.isDebugEnabled())
	        		LOGGER.debug("parsed " + pattern + " to " + tree.toStringTree());
	            return convertNode(tree);
	        } else
	        	return null;
        } catch (RuntimeException e) {
        	if (e.getCause() instanceof RecognitionException)
        		throw mapToParseException((RecognitionException) e.getCause());
        	else
        		throw e;
        } catch (IOException e) {
        	throw new IllegalStateException("Encountered illegal state in regex parsing", e);
        } catch (RecognitionException e) {
        	throw mapToParseException(e);
        }
    }

    public Object parseSingleChar(String pattern) throws ParseException {
        if (pattern == null)
            return null;
        if (pattern.length() == 0)
            return "";
        try {
	        RegexLexer lex = new RegexLexer(new ANTLRReaderStream(new StringReader(pattern)));
	        CommonTokenStream tokens = new CommonTokenStream(lex);
	        org.databene.regex.antlr.RegexParser parser = new org.databene.regex.antlr.RegexParser(tokens);
	        org.databene.regex.antlr.RegexParser.singlechar_return r = parser.singlechar();
	        if (parser.getNumberOfSyntaxErrors() > 0)
	        	throw new ParseException("Illegal regex: " + pattern, -1);
	        if (r != null) {
	        	CommonTree tree = (CommonTree) r.getTree();
	        	if (LOGGER.isDebugEnabled())
	        		LOGGER.debug("parsed " + pattern + " to " + tree.toStringTree());
	            return convertNode(tree);
	        } else
	        	return null;
        } catch (RuntimeException e) {
        	if (e.getCause() instanceof RecognitionException)
        		throw mapToParseException((RecognitionException) e.getCause());
        	else
        		throw e;
        } catch (IOException e) {
        	throw new IllegalStateException("Encountered illegal state in regex parsing", e);
        } catch (RecognitionException e) {
        	throw mapToParseException(e);
        }
    }

	public static CharSet toCharSet(Object o) {
		if (o instanceof CharSet)
			return (CharSet) o;
		else if (o instanceof Character)
			return new CharSet(CollectionUtil.toSet((Character) o));
		else if (o instanceof CustomCharClass)
			return ((CustomCharClass) o).getCharSet();
		else
			throw new IllegalArgumentException("Not a supported character regex type: " + o.getClass());
	}

    public static Set<Character> toSet(Object regex) {
	    return toCharSet(regex).getSet();
    }
	
    private ParseException mapToParseException(RecognitionException e) {
    	return new ParseException("Error parsing regular expression: " + e.getMessage(), e.charPositionInLine);
    }

	private Object convertNode(CommonTree node) throws ParseException {
    	if (node == null)
    		return null;
    	if (node.getToken() == null)
    		return "";
    	switch (node.getType()) {
			case RegexLexer.CHOICE: return convertChoice(node);
			case RegexLexer.GROUP: return convertGroup(node);
			case RegexLexer.SEQUENCE: return convertSequence(node);
			case RegexLexer.FACTOR: return convertFactor(node);
			case RegexLexer.SIMPLEQUANTIFIER: return convertSimpleQuantifier(node);
			case RegexLexer.CLASS: return convertClass(node);
			case RegexLexer.RANGE: return convertRange(node);
			case RegexLexer.ALPHANUM: return convertAlphanum(node);
			case RegexLexer.SPECIALCHARACTER: return convertAlphanum(node);
			case RegexLexer.ESCAPEDCHARACTER: return convertEscaped(node);
			case RegexLexer.NONTYPEABLECHARACTER: return convertNonTypeable(node);
			case RegexLexer.OCTALCHAR: return convertOctal(node);
			case RegexLexer.HEXCHAR: return convertHexChar(node);
			case RegexLexer.CODEDCHAR: return convertCodedChar(node);
			case RegexLexer.PREDEFINEDCLASS: return convertPredefClass(node);
			default: throw new ParseException("Unknown token type: " + node.getToken(), node.getCharPositionInLine());
    	}
    }

	@SuppressWarnings("unchecked")
    private Group convertGroup(CommonTree node) throws ParseException {
    	List<CommonTree> childNodes = node.getChildren();
    	Assert.equals(1, childNodes.size(), "Group is expected to have exactly one child node");
    	return new Group(convertNode(childNodes.get(0)));
    }

	@SuppressWarnings("unchecked")
    private Choice convertChoice(CommonTree node) throws ParseException {
    	List<CommonTree> childNodes = node.getChildren();
    	List<Object> childPatterns = new ArrayList<Object>();
    	if (childNodes != null)
	    	for (CommonTree childNode : childNodes)
	    		childPatterns.add(convertNode(childNode));
    	return new Choice(childPatterns.toArray());
    }

    @SuppressWarnings("unchecked")
    private Sequence convertSequence(CommonTree tree) throws ParseException {
    	List<CommonTree> childNodes = tree.getChildren();
    	List<Object> children = new ArrayList<Object>();
    	if (childNodes != null)
	    	for (Object child : childNodes)
	    		children.add(convertNode((CommonTree) child));
    	return new Sequence(children.toArray());
    }

    @SuppressWarnings("unchecked")
    private Factor convertFactor(CommonTree tree) throws ParseException {
    	List<CommonTree> children = tree.getChildren();
    	Object subPattern = convertNode(children.get(0));
    	Quantifier quantifier = null;
    	if (children.size() > 1)
    		quantifier = convertQuantifier(children.get(1));
    	else
    		quantifier = new Quantifier(1, 1);
    	return new Factor(subPattern, quantifier);
    }

    @SuppressWarnings("unchecked")
    private CustomCharClass convertClass(CommonTree node) throws ParseException {
    	CustomCharClass result = new CustomCharClass();
    	List<CommonTree> childNodes = node.getChildren();
    	if (childNodes != null)
	    	for (CommonTree child : childNodes) {
	    		if (child.getType() == RegexLexer.INCL) {
	    	    	List<CommonTree> gdChildren = child.getChildren();
	    	    	if (gdChildren != null)
	    	    		for (CommonTree gdChild : gdChildren)
	    	    			result.getIncluded().add(convertNode(gdChild));
	    		} else {
	    	    	List<CommonTree> gdChildren = child.getChildren();
	    	    	if (gdChildren != null)
	    	    		for (CommonTree gdChild : gdChildren)
	    	    			result.getExcluded().add(convertNode(gdChild));
	    		}
	    	}
    	if (result.getIncluded().isEmpty())
    		result.getIncluded().add(new CharSet().addAnyCharacters()); // for e.g. [^\d]
    	return result;
    }

    private char convertAlphanum(CommonTree node) {
    	return node.getText().charAt(0);
    }

    private char convertNonTypeable(CommonTree node) throws ParseException {
    	switch (node.getText().charAt(1)) {
	    	case 'r' : return '\r'; // the carriage-return character
	    	case 'n' : return '\n'; // the newline (line feed) character
	    	case 't' : return '\t'; // the tab character
	    	case 'f' : return '\f'; // the form-feed character
	    	case 'a' : return '\u0007'; // the alert (bell) character
	    	case 'e' : return '\u001B'; // the escape character
			default: throw new ParseException("invalid non-typeable char: " + node.getText(), node.getCharPositionInLine());
    	}
    }

    private char convertOctal(CommonTree node) {
	    return (char) Integer.parseInt(node.getText().substring(2), 8);
    }

    private char convertHexChar(CommonTree node) {
	    return (char) Integer.parseInt(node.getText().substring(2), 16);
    }

    private char convertCodedChar(CommonTree node) {
	    return (char) (Character.toUpperCase(node.getText().charAt(2)) - 'A');
    }

    private CharSet convertPredefClass(CommonTree node) throws ParseException {
    	String text = node.getText();
    	if (".".equals(text))
    		return new CharSet(".", CharSet.getAnyCharacters());
		char charClass = text.charAt(1);
        switch (charClass) {
	        case 'd' : return new CharSet("\\d", CharSet.getDigits());
	        case 'D' : return new CharSet("\\D", CharSet.getNonDigits());
	        case 's' : return new CharSet("\\s", CharSet.getWhitespaces());
	        case 'S' : return new CharSet("\\S", CharSet.getNonWhitespaces());
	        case 'w' : return new CharSet("\\w", CharSet.getWordChars(locale));
	        case 'W' : return new CharSet("\\W", CharSet.getNonWordChars());
	        default: throw new ParseException("Unsupported character class: " + text, node.getCharPositionInLine());
	    }
    }

    private Character convertEscaped(CommonTree node) {
    	String text = node.getText();
		return text.charAt(1);
    }

    @SuppressWarnings("unchecked")
    private Object convertRange(CommonTree node) throws ParseException {
    	List<CommonTree> children = node.getChildren();
    	CommonTree fromNode = children.get(0);
		char from = (Character) convertNode(fromNode);
    	CommonTree toNode = children.get(1);
		char to = (Character) convertNode(toNode);
	    return new CharSet(fromNode.getText() + "-" + toNode.getText(), from, to);
    }

    private Quantifier convertQuantifier(CommonTree node) throws ParseException {
    	switch (node.getType()) {
	    	case RegexLexer.SIMPLEQUANTIFIER : return convertSimpleQuantifier(node);
	    	case RegexLexer.QUANT : return convertExplicitQuantifier(node);
		    default: throw new ParseException("Error parsing quantifier: " + node.getText(), node.getCharPositionInLine());
    	}
    }
    
    @SuppressWarnings("unchecked")
    private Quantifier convertExplicitQuantifier(CommonTree tree) {
    	int min = 0;
    	Integer max = null;
    	List<CommonTree> children = tree.getChildren();
    	min = convertInt(children.get(0));
    	if (children.size() > 1)
    		max = convertInt(children.get(1));
    	Quantifier result = new Quantifier(min, max);
    	return result;
    }

    private Integer convertInt(CommonTree node) {
	    Assert.equals(RegexLexer.INT, node.getType(), "node is not an integer");
	    return Integer.parseInt(node.getText());
    }

	private Quantifier convertSimpleQuantifier(CommonTree node) throws ParseException {
	    Assert.equals(RegexLexer.SIMPLEQUANTIFIER, node.getType(), "node is not an simple quantifier");
	    char letter = node.getText().charAt(0);
	    switch (letter) {
		    case '?' : return new Quantifier(0, 1);
		    case '*' : return new Quantifier(0, null);
		    case '+' : return new Quantifier(1, null);
		    default: throw new ParseException("Error parsing simple quantifier: " + letter, node.getCharPositionInLine());
	    }
    }

}
