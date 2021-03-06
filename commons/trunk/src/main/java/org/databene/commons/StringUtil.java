/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

import java.util.*;

/**
 * Provides String related utility operations.<br/>
 * <br/>
 * Created: 05.07.2006 22:45:12
 * @author Volker Bergmann
 */
public final class StringUtil {

	/** Bell character (BEL, 0x07) */
	public static final String BEL = String.valueOf((char) 7);
	
	/** Backspace character (BS, 0x08) */
	public static final String BS = String.valueOf((char) 8);

	/** Horizontal Tab character (HT, 0x09) */
	public static final String HT = String.valueOf('\t');

	/** Line Feed character (LF, 0x0A) */
	public static final String LF = String.valueOf('\n');

	/** Vertical Tab character (VT, 0x0B) */
	public static final String VT = String.valueOf('\u000B');

	/**  character (FF, 0x0C) */
	public static final String FF = String.valueOf('\f');

	/**  character (CR, 0x0D) */
	public static final String CR = String.valueOf('\r');
	
	
	
    /**
     * Tells if a String is null or isEmpty.
     * @param s the string argument to check
     * @return true if the String is null or isEmpty, otherwise false.
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * Returns the suffix of a String. If the last character is a
     * separator, or if no separator was found, the string is assumed
     * to have no suffix.
     * @param name the String to check
     * @param separator the character that separates name from suffix
     * @return a suffix if one exists, else null.
     */
    public static String suffix(String name, char separator) {
        if (name == null)
            return null;
        int separatorIndex = name.lastIndexOf(separator);
        if (separatorIndex < 0 || separatorIndex == name.length() - 1)
            return null;
        else
            return name.substring(separatorIndex + 1);
    }

    /**
     * Returns the last token of a list in string representation.
     * If the string does not contain the separator, the string itself
     * is the token. If the string ends with a separator, the token is
     * null.
     * @param name
     * @param separator the character that separates the tokens
     * @return the last token
     */
    public static String lastToken(String name, char separator) {
        if (name == null)
            return null;
        int separatorIndex = name.lastIndexOf(separator);
        if (separatorIndex < 0)
            return name;
        else if (separatorIndex == name.length() - 1)
            return null;
        else
            return name.substring(separatorIndex + 1);
    }

    /**
     * Splits a list's String representaion into tokens.
     * @param text the String representation of a list.
     * @param separator the character used to separate tokens
     * @return an array of tokens.
     */
    public static String[] tokenize(String text, char separator) {
        if (text == null)
            return null;
        if (text.length() == 0)
            return new String[] { "" };
        int i = 0;
        int sep;
        List<String> list = new ArrayList<String>();
        while ((sep = text.indexOf(separator, i)) >= 0) {
            if (sep == i) {
                list.add("");
                i++;
            } else {
                list.add(text.substring(i, sep));
                i = sep + 1;
            }
        }
        if (i < text.length())
            list.add(text.substring(i));
        else if (text.endsWith("" + separator))
            list.add("");
        String[] tokens = new String[list.size()];
        for (i = 0; i < tokens.length; i++)
            tokens[i] = list.get(i);
        return tokens;
    }

    public static String normalize(String s) {
        char[] srcBuffer = new char[s.length()];
        s.getChars(0, s.length(), srcBuffer, 0);
        char[] dstBuffer = new char[s.length()];
        int dstIndex = 0;
        for (char c : srcBuffer)
            if (c >= 16)
                dstBuffer[dstIndex++] = c;
        return new String(dstBuffer, 0, dstIndex);
    }

    public static StringBuilder appendLeftAligned(StringBuilder builder, String text, int columns) {
        builder.append(text);
        int columnsToInsert = columns - text.length();
        for (int i = 0; i < columnsToInsert; i++)
            builder.append(' ');
        return builder;
    }

    public static String increment(String text) {
        if (text == null || text.length() == 0)
            return "0";
        char[] chars = new char[text.length()];
        text.getChars(0, text.length(), chars, 0);
        chars = increment(chars, chars.length - 1);
        return String.valueOf(chars);
    }

    public static char[] increment(char[] chars, int index) {
        char c = chars[index];
        switch (c) {
            case '9':
                chars[index] = 'a';
                break;
            case 'z': case 'Z':
                if (index > 0) {
                    chars[index] = '0';
                    chars = increment(chars, index - 1);
                } else {
                    char[] result = new char[chars.length + 1];
                    Arrays.fill(result, '0');
                    result[0] = '1';
                    return result;
                }
                break;
            default:
                chars[index]++;
        }
        return chars;
    }

    /** interprets an nbsp as space character */
    public static boolean isWhitespace(char c) {
        return Character.isWhitespace(c) || c == 160;
    }

    /** Trims a String by removing white spaces (including nbsp) from left and right. */
    public static String trim(String s) {
        if (s == null || s.length() == 0)
            return s;
        int beginIndex;
        for (beginIndex = 0; beginIndex < s.length() && isWhitespace(s.charAt(beginIndex)); beginIndex++) {
        }
        int endIndex;
        for (endIndex = s.length() - 1; endIndex > 0 && isWhitespace(s.charAt(endIndex)); endIndex--) {
        }
        if (beginIndex > endIndex)
            return "";
        return s.substring(beginIndex, endIndex + 1);
    }

    /**
     * Trims all String in the array.
     * @param array an array of the Strings to trim
     * @since 0.2.05
     */
    public static String[] trimAll(String[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = trim(array[i]);
        return array;
    }

    /** Returns the platform dependent line separator */
    public static String lineSeparator() {
        return System.getProperty("line.separator");
    }

    public static boolean contains(String s, char c) {
        return s.indexOf(c) >= 0;
    }

    public static String remove(String s, String chars) {
        if (s == null)
            return s;
        StringBuilder result = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            if (!(contains(chars, s.charAt(i))))
                result.append(s.charAt(i));
        }
        return result.toString();
    }

    public static String nullToEmpty(String text) {
    	return (text != null ? text : "");
    }

    public static String normalizeSpace(String s) {
        if (s == null || s.length() == 0)
            return s;
        s = trim(s);
        if (s.length() == 0)
            return s;
        char lastChar = s.charAt(0);
        StringBuilder result = new StringBuilder().append(lastChar);
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c))
                result.append(c);
            else if (!(Character.isWhitespace(lastChar)))
                result.append(' ');
            lastChar = c;
        }
        return result.toString();
    }

    public static String trimEnd(String s) {
        if (s == null || s.length() == 0)
            return s;
        int lastIndex = s.length() - 1;
        while (lastIndex >= 0 && isWhitespace(s.charAt(lastIndex)))
            lastIndex--;
        if (lastIndex < 0)
            return "";
        return s.substring(0, lastIndex + 1);
    }

    public static int countChars(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == c)
                count++;
        return count;
    }

    public static String[] toArray(List<String> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            result[i] = list.get(i);
        return result;
    }

    public static String[][] toArrayArray(List<List<String>> list) {
        String[][] result = new String[list.size()][];
        for (int i = 0; i < list.size(); i++)
            result[i] = toArray(list.get(i));
        return result;
    }

    public static String padLeft(String text, int length, char c) {
        if (text == null)
            text = "";
        int textLength = text.length();
        if (textLength > length)
            throw new IllegalArgumentException("Text is too long (" + textLength + ") to be padded to " + length + " columns");
        char[] chars = new char[length];
        int offset = length - textLength;
        fill(chars, 0, offset, c);
        getChars(0, textLength, text, chars, offset);
        return new String(chars);
    }

    public static String padRight(String text, int length, char c) {
        if (text == null)
            text = "";
        int textLength = text.length();
        if (textLength > length)
            throw new IllegalArgumentException("Text is too long (" + textLength + ") to be padded to " + length + " columns");
        char[] chars = new char[length];
        fill(chars, textLength, length, c);
        getChars(0, textLength, text, chars, 0);
        return new String(chars);
/*
        int textLength = text.length();
        if (textLength > length)
            throw new IllegalArgumentException("Text is too long (" + textLength + ") to be padded to " + length + " columns");
        int padLength = length - textLength;
        char[] padChars = new char[padLength];
        if (padLength < 20) {
            for (int i = 0; i < padLength; i++)
                padChars[i] = c;
        } else if (c == ' ' && padLength <= WHITESPACE_BUFFER.length) {
            System.arraycopy(WHITESPACE_BUFFER, 0, padChars, 0, padLength);
        } else {
            Arrays.fill(padChars, 0, padLength, c);
        }
        return text.concat(new String(padChars));
*/
    }

    private static final int BUFFER_SIZE = 1024;
    private static char[] WHITESPACE_BUFFER;

    static {
        WHITESPACE_BUFFER = new char[BUFFER_SIZE];
        for (int i = 0; i < WHITESPACE_BUFFER.length; i++)
            WHITESPACE_BUFFER[i] = ' ';
    }

    public static char[] fill(char[] chars, int fromIndex, int toIndex, char c) {
        int length = toIndex - fromIndex;
        if (length < 20) {
            for (int i = fromIndex; i < toIndex; i++)
                chars[i] = c;
        } else if (c != ' ' || length > WHITESPACE_BUFFER.length) {
            Arrays.fill(chars, fromIndex, toIndex, c);
        } else {
            System.arraycopy(WHITESPACE_BUFFER, 0, chars, fromIndex, length);
        }
        return chars;
    }

    public static char[] getChars(int srcBegin, int srcEnd, String text, char[] chars, int dstBegin) {
        int textLength = text.length();
        if (textLength >= 6)
            text.getChars(srcBegin, srcEnd, chars, dstBegin);
        else
            for (int i = 0; i < textLength; i++)
                chars[i + dstBegin] = text.charAt(i);
        return chars;
    }

	public static char[] getChars(String s) {
		char[] chars  = new char[s.length()];
		return getChars(0, chars.length, s, chars, 0);
	}

	public static char[] getChars(StringBuilder builder) {
		char[] chars = new char[builder.length()];
		builder.getChars(0, builder.length(), chars, 0);
		return chars;
	}

	public static String padString(char c, int length) {
        if (length < 0)
            throw new IllegalArgumentException("Negative pad length: " + length);
        char[] chars = new char[length];
        Arrays.fill(chars, 0, length, c);
        return new String(chars);
    }

    public static String trimRight(String source) {
        if (source == null)
            return null;
        int i = source.length() - 1;
        while (i >= 0 && isWhitespace(source.charAt(i)))
            i--;
        return source.substring(0, i + 1);
    }

    public static String trimRight(String source, char padChar) {
        if (source == null)
            return null;
        int i = source.length() - 1;
        while (i >= 0 && source.charAt(i) == padChar)
            i--;
        return source.substring(0, i + 1);
    }

    public static String trimLeft(String source, char padChar) {
        if (source == null)
            return null;
        int i = 0;
        while (i < source.length() && source.charAt(i) == padChar)
            i++;
        return source.substring(i, source.length());
    }

    public static Object trim(String source, char padChar) {
        if (source == null)
            return null;
        int i0 = 0;
        while (i0 < source.length() && source.charAt(i0) == padChar)
            i0++;
        if (i0 == source.length())
            return "";
        int i1 = source.length() - 1;
        while (i1 > i0 && source.charAt(i1) == padChar)
            i1--;
        return source.substring(i0, i1 + 1);
    }

    public static String[] toLowerCase(String[] src) {
        String[] dst = new String[src.length];
        for (int i = 0; i < src.length; i++)
            dst[i] = src[i].toLowerCase();
        return dst;
    }

    public static int indexOfIgnoreCase(String searched, String ... candidates) {
        for (int i = 0; i < candidates.length; i++)
            if (searched.equalsIgnoreCase(candidates[i]))
                return i;
        return -1;
    }

    public static char lastChar(String word) {
        return word.charAt(word.length() - 1);
    }

    /**
     * Makes the first character of a String uppercase.
     * @param text the text to convert
     * @return a text that starts with a uppercase letter
     */
    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * Makes the first character of a String lowercase.
     * @param text the text to convert
     * @return a text that starts with a lowercase letter
     * @since 0.2.04
     */
    public static String uncapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    public static String[] splitOnFirstSeparator(String path, char separator) {
        if (path == null)
            return new String[] { null, null };
        int sepIndex = path.indexOf(separator);
        return splitAroundSeparator(path, sepIndex);
    }

    public static String[] splitOnLastSeparator(String path, char separator) {
        if (path == null)
            return new String[] { null, null };
        int sepIndex = path.lastIndexOf(separator);
        return splitAroundSeparator(path, sepIndex);
    }

	public static String[] splitAroundSeparator(String path, int sepIndex) {
	    if (sepIndex < 0)
            return new String[] { null, path };
        else if (sepIndex == 0)
            return new String[] { "", path.substring(1) };
        else if (sepIndex == path.length() - 1)
            return new String[] { path.substring(0, path.length() - 1), "" };
        else
            return new String[] { path.substring(0, sepIndex), path.substring(sepIndex + 1) };
    }

	public static String concat(Character separator, String... parts) {
		if (parts == null)
			return "";
		StringBuilder builder = new StringBuilder();
		for (String part : parts) {
			if (!isEmpty(part)) {
				if (builder.length() > 0 && separator != null)
					builder.append(separator);
				builder.append(part);
			}
		}
		return builder.toString();
	}

	public static boolean equalsIgnoreCase(String s1, String s2) {
        return (s1 != null ? s1.equalsIgnoreCase(s2) : s2 == null);
	}

	public static boolean equalsIgnoreCase(String[] a1, String[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (!equalsIgnoreCase(a1[i], a2[i]))
				return false;
		return true;
	}

    public static boolean containsIgnoreCase(String searchedWord, Collection<String> words) {
        for (String name : words)
            if (name.equalsIgnoreCase(searchedWord))
                return true;
        return false;
    }

    public static boolean containsIgnoreCase(String searchedWord, String[] words) {
        for (String name : words)
            if (name.equalsIgnoreCase(searchedWord))
                return true;
        return false;
    }

    public static boolean startsWithIgnoreCase(String text, String prefix) {
    	if (text == null)
    		return (prefix == null);
    	if (prefix == null)
    		return false;
        return text.toLowerCase().startsWith(prefix.toLowerCase());
    }

    public static boolean endsWithIgnoreCase(String text, String suffix) {
    	if (text == null)
    		return (suffix == null);
    	if (suffix == null)
    		return false;
        return text.toLowerCase().endsWith(suffix.toLowerCase());
    }

	public static String normalizeName(final String name) {
		final int NONE = -1;
		final int WS = 0;
		final int INITIAL = 1;
		final int SUBSEQUENT = 2;
		StringBuilder builder = new StringBuilder(name.length());
		StringCharacterIterator iterator = new StringCharacterIterator(name);
		iterator.skipWhitespace();
		int prevType = NONE;
		while (iterator.hasNext()) {
			char c = iterator.next();
			int type;
			if (Character.isWhitespace(c))
				type = WS;
			else if (prevType == INITIAL)
				type = SUBSEQUENT;
			else if (prevType == NONE || prevType == WS)
				type = INITIAL;
			else
				type = prevType;
			if (prevType == WS && type == INITIAL)
				builder.append(' ');
			switch (type) {
				case INITIAL:
					builder.append(Character.toUpperCase(c));
					break;
				case SUBSEQUENT: 
					builder.append(Character.toLowerCase(c));
					break;
				case WS:
					break;
				default: throw new RuntimeException("Internal error");
			}
			prevType = type;
		}
		return builder.toString();
	}

	public static String escape(String text) {
		return escape(text, false, false);
	}

	/**
	 * @see "http://en.wikipedia.org/wiki/ASCII"
	 */
	public static String escape(String text, boolean escapeSingleQuotes, boolean escapeDoubleQuotes) {
		if (text == null)
			return null;
		text = text.replace("\\", "\\\\"); // keep this first, otherwise all other escapes will be doubled
		text = text.replace(BEL, "\\u0007");
		text = text.replace(BS, "\\u0008");
		text = text.replace(CR, "\\r");
		text = text.replace(LF, "\\n");
		text = text.replace(HT, "\\t");
		text = text.replace(VT, "\\u000B");
		text = text.replace(FF, "\\f");
		if (escapeSingleQuotes)
			text = text.replace("'", "\\'");
		if (escapeDoubleQuotes)
			text = text.replace("\"", "\\\"");
		return text;
	}

	/**
	 * @see "http://en.wikipedia.org/wiki/ASCII"
	 */
	public static String unescape(String text) {
		if (text == null)
			return null;
		StringBuilder builder = new StringBuilder(text.length());
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != '\\')
				builder.append(c);
			else if (i < text.length() - 1) {
				c = text.charAt(++i);
				switch (c) {
					case '\'' : builder.append('\''); break;
					case '"' : builder.append('"'); break;
					case '{' : builder.append('{'); break;
					case '}' : builder.append('}'); break;
					case 'a' : builder.append(BEL); break;
					case 'b' : builder.append(BS); break;
					case 'r' : builder.append(CR); break;
					case 'n' : builder.append(LF); break;
					case 't' : builder.append(HT); break;
					case 'f' : builder.append(FF); break;
					case 'u' : 
						long n = Long.parseLong(text.substring(i + 1, i + 5), 16);
						builder.append((char) n); 
						i += 4; 
						break;
					default  : builder.append(c); break;
				}
			} else
				builder.append('\\');
		}
		return builder.toString();
	}

	public static String replaceTokens(String src, String token, String ... values) {
		StringBuilder builder = new StringBuilder();
		int paramIndex = 0;
		int srcIndex = 0;
		while (srcIndex < src.length()) {
			int i = src.indexOf(token, srcIndex);
			if (i >= 0) {
				builder.append(src.substring(srcIndex, i));
				builder.append(values[paramIndex++]);
				srcIndex = i + token.length();
			} else {
				builder.append(src.substring(srcIndex));
				break;
			}
		}
		return builder.toString();
	}

	public static String emptyToNull(String s) {
		if (s == null || s.length() == 0)
			return null;
		String trimmed = trim(s);
		return (trimmed.length() != 0 ? s : null);
	}

    public static String removeSection(String text, String beginMark, String endMark) {
    	if (StringUtil.isEmpty(text))
    		return text;
	    int beginIndex = text.indexOf(beginMark);
	    int endIndex = text.indexOf(endMark);
	    if (beginIndex < 0 || endIndex < 0 || beginIndex + beginMark.length() > endIndex + endMark.length())
	    	return text;
	    return text.substring(0, beginIndex) + text.substring(endIndex + endMark.length());
    }

	public static String normalizeLineSeparators(String text, String lineSeparator) {
		if (StringUtil.isEmpty(text))
			return text;
		StringBuilder builder = new StringBuilder();
		StringCharacterIterator iterator = new StringCharacterIterator(text);
		while (iterator.hasNext()) {
			char c = iterator.next();
			if (c != '\r' && c != '\n')
				builder.append(c);
			else {
				// swallow the \n part of of \r\n
				if (c == '\r' && iterator.hasNext()) {
					char c2 = iterator.next();
					if (c2 != '\n') // oops, it was only a \r
						iterator.pushBack();
				}
				builder.append(lineSeparator);
			}
		}
	    return builder.toString();
    }

	public static String extract(String text, String beginMark, String endMark) {
	    int beginIndex = (beginMark != null ? text.indexOf(beginMark) + beginMark.length() : 0);
	    if (endMark != null) {
		    int endIndex = text.indexOf(endMark, beginIndex + 1);
		    return text.substring(beginIndex, endIndex);
	    } else
	    	return text.substring(beginIndex);
    }

	public static String buildPhrase(String... parts) {
		if (parts == null)
			return "";
		StringBuilder builder = new StringBuilder();
		for (String part : parts) {
			if (!StringUtil.isEmpty(part)) {
				if (builder.length() > 0)
					builder.append(' ');
				builder.append(part);
			}
		}
		return builder.toString();
	}
	
	public static String trimLineSeparators(String text) {
		if (text == null)
			return null;
		int start = 0;
		while (start < text.length() && isLineSeparatorChar(text.charAt(start)))
			start++;
		int end = text.length();
		while (end > 0 && (text.charAt(end - 1) == '\r' || text.charAt(end - 1) == '\n'))
			end--;
	    return text.substring(start, end);
    }

	public static List<String> splitLines(String text) { // TODO v0.5.x this leaves out empty lines in between
		if (text == null)
			return null;
	    List<String> lines = new ArrayList<String>();
	    int TEXT_MODE = 0;
	    int LF_MODE = 1;
	    int mode = TEXT_MODE;
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < text.length(); i++) {
	    	char c = text.charAt(i);
	    	if (isLineSeparatorChar(c)) {
	    		if (mode == TEXT_MODE) {
	    			lines.add(builder.toString());
	    			builder.delete(0, builder.length());
	    			mode = LF_MODE;
	    		}
	    	} else {
	    		if (mode == LF_MODE)
	    			mode = TEXT_MODE;
	    		builder.append(c);
	    	}
	    }
	    if (text.length() == 0 || mode == LF_MODE || builder.length() > 0)
	    	lines.add(builder.toString());
	    return lines;
    }

	public static String removeEmptyLines(String text) {
		if (text == null)
			return null;
		List<String> lines = splitLines(text);
		Iterator<String> iterator = lines.iterator();
		while (iterator.hasNext())
			if (iterator.next().trim().length() == 0)
				iterator.remove();
		String sep = lineSeparatorUsedIn(text);
		return ArrayFormat.format(sep, lines.toArray());
	}
	
	private static String lineSeparatorUsedIn(String text) {
		int i = 0;
		while (i < text.length() && !isLineSeparatorChar(text.charAt(i))) // search the first line separator char
			i++;
		if (i == text.length()) // if no line sep was found, then return null
			return null;
		char c1 = text.charAt(i);
		if (i == text.length() - 1)
			return String.valueOf(c1); // if the found char is the last one in the string, return it, ...
		char c2 = text.charAt(i + 1);  // ... otherwise check the char that follows
		if (isLineSeparatorChar(c2) && c1 != c2)
			return "" + c1 + c2; // the line separator consists of two characters
		return String.valueOf(c1);
	}

	public static boolean isLineSeparatorChar(char c) {
	    return c == '\r' || c == '\n';
    }

	public static String quoteIfNotNull(String text) {
		return (text != null ? "'" + text + "'" : text);
	}

}
