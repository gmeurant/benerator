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
import java.io.IOException;

/**
 * Provides locale operations like determining the parent of a locale,
 * finding a locale by code, providing the characters of a locale
 * and a so-called 'fallback locale'.
 *
 * Created: 26.09.2006 23:34:41
 */
public final class LocaleUtil {

    /** The locale to use by default */
    private static final Locale FALLBACK_LOCALE = Locale.US;

    /** collects the special letters of locales as set of Characters, indexed by the locale. */
    private static Map<Locale, Set<Character>> specialLetters;

    /** Static initializer calls readConfigFile(). @see #readConfigFile() */
    static {
        readConfigFile();
    }

    // interface -------------------------------------------------------------------------------------------------------

    /**
     * Returns a set that contains all letters of the specified locale.
     * @param locale the locale of which the character set is requested
     * @return a set of characters that contains all letters of the specified locale
     * @exception UnsupportedOperationException if the locale is not supported
     */
    public static Set<Character> letters(Locale locale) {
        Set<Character> set = nullTolerantLetters(locale);
        if (set == null)
            throw new UnsupportedOperationException("Locale not supported: " + locale);
        return set;
    }

    /**
     * Determines a locale's parent, e.g. for a locale 'de_DE' it returns 'de'.
     * @param locale the locale of which to determine the parent
     * @return the locale's parent, or null if the locale has no parent.
     */
    public static Locale parent(Locale locale) {
        String variant = locale.getVariant();
        if (!StringUtil.isEmpty(variant)){
            if (variant.contains("_")) {
                String[] variantPath = StringUtil.tokenize(variant, '_');
                variant = ArrayFormat.formatPart("_", 0, variantPath.length - 1, variantPath);
                return new Locale(locale.getLanguage(), locale.getCountry(), variant);
            } else
                return new Locale(locale.getLanguage(), locale.getCountry());
        } else if (!StringUtil.isEmpty(locale.getCountry()))
            return new Locale(locale.getLanguage());
        else
            return null;
    }

    /**
     * Returns the fallback locale.
     * This differs from the default locale for cases in which it is desirable to
     * restrict the used character ranges to an unproblematic minimum.
     * @return the fallback locale.
     */
    public static Locale getFallbackLocale() {
        return FALLBACK_LOCALE;
    }

    /**
     * Maps the locale code to a locale, e.g. de_DE to Locale.GERMANY.
     * @param code the locale colde to map
     * @return a locale instance the represents the code
     */
    public static Locale getLocale(String code) {
        if (StringUtil.isEmpty(code))
            throw new IllegalArgumentException("code is empty");
        String[] path = StringUtil.tokenize(code, '_');
        switch (path.length) {
            case 1 : return new Locale(path[0]);
            case 2 : return new Locale(path[0], path[1]);
            case 3 : return new Locale(path[0], path[1], path[2]);
            default : return new Locale(path[0], path[1], ArrayFormat.formatPart("_", 2, path.length - 2, path));
        }
    }

    // private helpers -------------------------------------------------------------------------------------------------

    /**
     * @param locale the locale of which to get the letters.
     * @return the letters of a locale, null if the locale is unknown.
     */
    private static Set<Character> nullTolerantLetters(Locale locale) {
        if (locale == null)
            return null;
        Set<Character> set = specialLetters.get(locale);
        if (set == null) {
            Locale parent = parent(locale);
            if (parent != null)
                return nullTolerantLetters(parent);
        }
        return set;
    }

    /**
     * Reads the config file org/databene/commons/special-letters.properties from the file system or the path
     * and initializes the internal specialLetters map.
     */
    private static void readConfigFile() {
        try {
            specialLetters = new HashMap<Locale, Set<Character>>();
            Properties properties = IOUtil.readProperties("org/databene/commons/special-letters.properties");
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Locale locale = getLocale(String.valueOf(entry.getKey()));
                String specialChars = String.valueOf(entry.getValue());
                Set<Character> charSet = new CharSet('A', 'Z').addRange('a', 'z').getSet();
                for (int i = 0; i < specialChars.length(); i++)
                    charSet.add(specialChars.charAt(i));
                specialLetters.put(locale, charSet);
            }
        } catch (IOException e) {
            throw new ConfigurationError("Setup file for locale-specific letters is missing", e);
        }
    }

}
