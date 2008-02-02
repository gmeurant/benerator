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

import java.util.*;

/**
 * Provides String related utility operations.<br/>
 * <br/>
 * Created: 05.07.2006 22:45:12
 * @author Volker Bergmann
 */
public final class StringUtil {

    /**
     * Tells if a String is null or isEmpty.
     * @param s the string argument to check
     * @return true if the String is null or isEmpty, otherwise false.
     */
    public static boolean isEmpty(String s) {
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
    public static void trimAll(String[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = trim(array[i]);
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
        StringBuffer result = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            if (!(contains(chars, s.charAt(i))))
                result.append(s.charAt(i));
        }
        return result.toString();
    }

    public static String normalizeSpace(String s) {
        if (s == null || s.length() == 0)
            return s;
        s = trim(s);
        if (s.length() == 0)
            return s;
        char lastChar = s.charAt(0);
        StringBuffer result = new StringBuffer().append(lastChar);
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!(Character.isWhitespace(lastChar) && Character.isWhitespace(c)))
                result.append(c);
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

    public static void fill(char[] chars, int fromIndex, int toIndex, char c) {
        int length = toIndex - fromIndex;
        if (length < 20) {
            for (int i = fromIndex; i < toIndex; i++)
                chars[i] = c;
        } else if (c != ' ' || length > WHITESPACE_BUFFER.length) {
            Arrays.fill(chars, fromIndex, toIndex, c);
        } else {
            System.arraycopy(WHITESPACE_BUFFER, 0, chars, fromIndex, length);
        }
    }

    public static void getChars(int srcBegin, int srcEnd, String text, char[] chars, int dstBegin) {
        int textLength = text.length();
        if (textLength >= 6)
            text.getChars(srcBegin, srcEnd, chars, dstBegin);
        else
            for (int i = 0; i < textLength; i++)
                chars[i + dstBegin] = text.charAt(i);
    }

    public static String padString(char c, int length) {
        if (length < 0)
            throw new IllegalArgumentException("Negative pad length: " + length);
        char[] chars = new char[length];
        Arrays.fill(chars, 0, length, c);
        return new String(chars);
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

}
