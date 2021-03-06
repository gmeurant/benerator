/*
 * (c) Copyright 2007-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.html.parser;

import org.databene.commons.CharSet;
import org.databene.commons.OrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Default implementation of an {@link HTMLTokenizer}.<br/>
 * <br/>
 * Created: 15.06.2007 05:56:21
 * @author Volker Bergmann
 */
public class DefaultHTMLTokenizer implements HTMLTokenizer {

	private static Logger logger = LoggerFactory.getLogger(DefaultHTMLTokenizer.class);

    private static final int TEXT_BUFFER_SIZE = 65536;
	private static final int ATTRIBUT_BUFFER_SIZE = 256;

    private static final CharSet ELEMENT_NAME_CHARS = new CharSet('A','Z').addRange('a', 'z').addRange('0', '9').add('_').add(':').add('-');
    private static final CharSet ATTR_NAME_CHARS = new CharSet('A','Z').addRange('a', 'z').addRange('0', '9').add('_').add('-').add(':');

    // parser state
    private PushbackReader reader;
    private boolean script;

    // token state
    private int tokenType;

    private int cursor;
    private String text;

    private int nameStart;
    private int nameLength;
    private String name;

    private int attribCount;
    private Map<String, String> attributeMap;

    // buffers
    private char[] textBuffer;
    private int[] attribNameFrom;
    private int[] attribNameUntil;
    private int[] attribValueFrom;
    private int[] attribValueUntil;

    public DefaultHTMLTokenizer(Reader reader) {
        // create buffers
        textBuffer = new char[TEXT_BUFFER_SIZE];
        attribNameFrom   = new int[ATTRIBUT_BUFFER_SIZE];
        attribNameUntil  = new int[ATTRIBUT_BUFFER_SIZE];
        attribValueFrom  = new int[ATTRIBUT_BUFFER_SIZE];
        attribValueUntil = new int[ATTRIBUT_BUFFER_SIZE];
        // init parsing state
        this.reader = new PushbackReader(reader, 256);
        this.script = false;
    }

    @Override
	public int nextToken() throws IOException, ParseException {
        // init token state
        cursor = 0;
        nameStart = -1;
        nameLength = 0;
        name = null;
        text = null;
        attributeMap = null;
        attribCount = 0;
        // we've reached the EOF before - do nothing more!
        if (this.tokenType == END)
            return END;
        if (script) {
            // if the last start tag was <script>, then consider everything before the next </script> to be script text
            parseScript();
        } else {
            int nextChar = peek(reader);
            switch (nextChar) {
                case -1  : this.tokenType = END; break;
                case '<' :
                    reader.read();
                    int c = peek(reader);
                    if (Character.isLetter(c) || c == '!' || c=='?' | c=='/') {
                        // it's the start of a tag
                        reader.unread(nextChar);
                        parseTag();
                    } else {
                        reader.unread(nextChar);
                        parseText();
                    }
                    break;
                default  : parseText(); break; // must be text
            }
            if (logger.isDebugEnabled())
                logger.debug(text());
        }
        return this.tokenType;
    }

    /** @return if it's a kind of tag then the tag name, else null */
    @Override
	public int tokenType() {
        return this.tokenType;
    }

    @Override
	public String name() {
        if (name == null && nameStart >= 0)
            name = new String(textBuffer, nameStart, nameLength).intern();
        return name;
    }

    /** @return the text that constitutes the current token as read from the source */
    @Override
	public String text() {
        if (text == null)
            text = new String(textBuffer, 0, cursor);
        return text;
    }

    /**
     * In case of non-tag tokens or empty tags, an empty map is returned.
     * @return a map with all attributes of the token.
     */
    @Override
	public Map<String, String> attributes() {
        if (attributeMap == null) {
            attributeMap = new OrderedMap<String, String>();
            for (int i = 0; i < attribCount; i++) {
                String attribName = new String(textBuffer, attribNameFrom[i], attribNameUntil[i] - attribNameFrom[i]);
                attribName = attribName.intern();
                String attribValue = null;
                if (attribValueFrom[i] >= 0)
                    attribValue = new String(textBuffer, attribValueFrom[i], attribValueUntil[i] - attribValueFrom[i]);
                attributeMap.put(attribName, attribValue);
            }
        }
        return attributeMap;
    }

    // parser implementation -------------------------------------------------------------------------------------------

    /**
     * parses anything that follows until it hits a &lt;/script&gt;
     * @throws IOException
     */
    private void parseScript() throws IOException {
        readUntil("</script>", false, false);
        this.script = false;
        this.tokenType = SCRIPT;
    }

    private void parseTag() throws IOException, ParseException {
        assertChar('<');
        switch (peek(reader)) {
            case '!' : // comment or doctype
                assertChar('!');
                if (peek(reader) == '-') {
                    // comment
                    assertChar('-');
                    assertChar('-');
                    readUntil("-->", true);
                    this.tokenType = COMMENT;
                } else{
                    // doctype
                    parseElementName();
                    readUntil('>'); // no detailed paring for the doctype
                    assertChar('>');
                    this.tokenType = DOCUMENT_TYPE;
                }
                break;
            case '/' :
                assertChar('/');
                parseElementName();
                expectChar('>');
                this.tokenType = END_TAG;
                break;
            case '?' : // processing instruction
                assertChar('?');
                parseElementName();
                parseAttributes();
                assertChar('?');
                assertChar('>');
                this.tokenType = PROCESSING_INSTRUCTION;
                break;
            default :
            	// regular tag
                parseElementName();
                parseAttributes();
                skipWhitespace();
                if (peek(reader) == '/') {
                    assertChar('/');
                    this.tokenType = CLOSED_TAG;
                } else {
                    this.tokenType = START_TAG;
                }
                expectChar('>');
                if ("SCRIPT".equalsIgnoreCase(name())) // if it's a script start tag,
                                                        // set a marker to parse the following stuff specially
                    script = true;
                break;
        }
    }

    private void parseAttributes() throws IOException, ParseException {
        while (parseAttribute()) {
        	// skip any further attribute
        }
        readUntilOneOf("?/>");
    }

    private void parseElementName() throws IOException {
        nameStart = cursor;
        parseString(ELEMENT_NAME_CHARS);
        nameLength = cursor - nameStart;
    }

    private boolean parseAttribute() throws IOException, ParseException {
        skipWhitespace();
        attribNameFrom[attribCount] = cursor;
        if (!parseAttributeName())
            return false;
        attribNameUntil[attribCount] = cursor;
        while (textBuffer[attribNameUntil[attribCount] - 1] == ':') // fix for bad HTML: remove trailing colons
        	attribNameUntil[attribCount]--;
        skipWhitespace();
        if (peek(reader) == '=') {
            parseAttributeValueAssignment();
        } else {
            attribValueFrom[attribCount] = -1;
            attribValueUntil[attribCount] = -1;
        }
        attribCount++;
        return true;
    }

    private void parseAttributeValueAssignment() throws IOException, ParseException {
        assertChar('=', true);
        skipWhitespace();
        parseAttributeValue();
    }

    private void parseAttributeValue() throws IOException, ParseException {
        char quoteChar;
        int c = peek(reader);
        if (c == '\'' || c == '"') {
            quoteChar = (char) c;
            textBuffer[cursor++] = (char)c;
            parseQuotedAttributeValue(String.valueOf(quoteChar));
        } else {
            attribValueFrom[attribCount] = cursor;
            readUntilOneOf(" >");
            attribValueUntil[attribCount] = cursor;
        }
    }

    private void parseQuotedAttributeValue(String quoteChars) throws IOException, ParseException {
        int quoteChar = reader.read();
        attribValueFrom[attribCount] = cursor;
        if (quoteChars.indexOf(quoteChar) < 0)
            throw new ParseException("Expected quotation like " + quoteChars + ", found: " + quoteChar, 0);
        readUntil((char)quoteChar);
        attribValueUntil[attribCount] = cursor;
        assertChar((char)quoteChar);
    }

    private boolean parseAttributeName() throws IOException {
        return parseString(ATTR_NAME_CHARS);
    }

    private boolean parseString(CharSet charSet) throws IOException {
        boolean stringFound = false;
        int c;
        while ((c = reader.read()) != -1 && charSet.contains((char)c)) {
            textBuffer[cursor++] = (char)c;
            stringFound = true;
        }
        if (c != -1)
            reader.unread(c);
        return stringFound;
    }

    private void parseText() throws IOException {
        this.tokenType = TEXT;
        boolean end = false;
        do {
            readUntil('<');
            int c = reader.read();
            if (c == -1)
                end = true;
            else if (c == '<') {
                int next = reader.read();
                if (next == '/' || next == '!' || next == '?' || Character.isLetter(next)) {
                    reader.unread(next);
                    reader.unread(c);
                    end = true;
                } else {
                    textBuffer[cursor++] = (char)c;
                    textBuffer[cursor++] = (char)next;
                }
            } else
                throw new RuntimeException("Unexpected token: " + (char)c);
        } while (!end);
    }

    public void readUntil(String endText) throws IOException {
        readUntil(endText, false);
    }

    public void readUntil(String delimiter, boolean includeDelimiter) throws IOException {
        readUntil(delimiter, true, includeDelimiter);
    }

    public void readUntil(String delimiter, boolean caseSensitive, boolean includeDelimiter) throws IOException {
        String cmp = (caseSensitive ? delimiter : delimiter.toUpperCase());
        char[] endChars = new char[cmp.length()];
        cmp.getChars(0, delimiter.length(), endChars, 0);
        do {
            int c;
            while((c = reader.read()) != -1 && (caseSensitive ? c : Character.toUpperCase(c)) != endChars[0]) {
                textBuffer[cursor++] = (char)c;
            }
            if (c == -1)
                return;
            int tmpCursor = cursor;
            textBuffer[tmpCursor++] = (char)c;
            int i;
            if (endChars.length == 1) {
                if (includeDelimiter) {
                    cursor++;
                } else
                    reader.unread(c);
                return;
            } else {
                for (i = 1; i < endChars.length; i++) {
                    c = reader.read();
                    textBuffer[tmpCursor++] = (char)c;
                    if ((caseSensitive ? c : Character.toUpperCase(c)) != endChars[i]) {
                        cursor += i + 1;
                        break;
                    }
                }
                if (i == delimiter.length()) {
                    if (includeDelimiter) {
                        //System.arraycopy(endChars, 0, textBuffer, cursor, endChars.length);
                        cursor += endChars.length;
                    } else
                        reader.unread(textBuffer, cursor, delimiter.length());
                    return;
                }
            }
        } while (true);
    }

    private int readUntilOneOf(String delimiters) throws IOException {
        int c;
        while ((c = reader.read()) != -1 && delimiters.indexOf(c) < 0)
            textBuffer[cursor++] = (char)c;
        if (c != -1)
            reader.unread(c);
        return c;
    }

    private void readUntil(char delimiter) throws IOException {
        int c;
        boolean escaped = false;
        while((c = reader.read()) != -1 && (c != delimiter || escaped)) {
            textBuffer[cursor++] = (char)c;
            escaped = (c == '\\');
        }
        if (c != -1)
            reader.unread(c);
    }

    private static int peek(PushbackReader reader) throws IOException {
        int c = reader.read();
        reader.unread(c);
        return c;
    }

    private void assertChar(char expectedChar) throws ParseException, IOException {
        int c = reader.read();
        if (c != expectedChar)
            throw new ParseException("Expected: '" + expectedChar + "', found: '" + (char)c + "'", 0);
        textBuffer[cursor++] = expectedChar;
    }

    private void expectChar(char expectedChar) throws IOException {
        int c = reader.read();
        if (c != expectedChar) {
            String message = "Expected: '" + expectedChar + "', found: '" + (char)c + "'";
            logger.error(message, new ParseException(message, -1));
            reader.unread(c);
        }
        textBuffer[cursor++] = expectedChar;
    }

    private void assertChar(char expectedChar, boolean skipSpace) throws ParseException, IOException {
        int c;
        do {
            c = reader.read();
            if (c != -1)
                textBuffer[cursor++] = (char)c;
        } while (c != -1 && skipSpace && Character.isWhitespace(c));
        if (c != expectedChar)
            throw new ParseException("Expected: '" + expectedChar + "', found: '" + (char)c + "'", 0);
    }

    private void skipWhitespace() throws IOException {
        int c;
        while ((c = reader.read()) != -1 && Character.isWhitespace(c)) {
            textBuffer[cursor++] = (char)c;
        }
        if (c != -1)
            reader.unread(c);
    }

}
