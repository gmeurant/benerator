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

import junit.framework.TestCase;

import java.util.Locale;
import java.util.Set;

/**
 * (c) Copyright 2006 by Volker Bergmann
 * Created: 27.08.2006 21:13:29
 */
public class LocaleUtilTest extends TestCase {

    public void testLetters() {
        check("de");
        check("de", "ch");
        check("de", "at");
        check("en");
        check("en", "us");
        check("en", "uk");
        check("fr");
        check("fr", "ca");
        check("en", "ch");
        check("it");
        check("it", "ch");
        check("es");
    }

    public void testParent() {
        assertEquals(null, LocaleUtil.parent(new Locale("de")));
        assertEquals(new Locale("de"), LocaleUtil.parent(new Locale("de", "DE")));
        assertEquals(new Locale("de", "DE"), LocaleUtil.parent(new Locale("de", "DE", "BY")));
        assertEquals(new Locale("de", "DE", "BY"), LocaleUtil.parent(new Locale("de", "DE", "BY_MUC")));
        assertEquals(new Locale("de", "DE", "BY_MUC"), LocaleUtil.parent(new Locale("de", "DE", "BY_MUC_SCHWABING")));
    }

    public void testGetFallbackLocale() {
        assertEquals(Locale.US, LocaleUtil.getFallbackLocale());
    }

    public void testGetLocale() {
        assertEquals(Locale.GERMAN, LocaleUtil.getLocale("de"));
        assertEquals(Locale.GERMANY, LocaleUtil.getLocale("de_DE"));
        assertEquals(new Locale("de", "DE", "BY"), LocaleUtil.getLocale("de_DE_BY"));
        assertEquals(new Locale("de", "DE", "BY_MUC"), LocaleUtil.getLocale("de_DE_BY_MUC"));
        assertEquals(new Locale("de", "DE", "BY_MUC_SCHWABING"), LocaleUtil.getLocale("de_DE_BY_MUC_SCHWABING"));
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void check(String language) {
        check(new Locale(language));
    }

    private void check(String language, String country) {
        check(new Locale(language, country));
    }

    private void check(Locale locale) {
        Set<Character> set = LocaleUtil.letters(locale);
        assertTrue(set.contains('A'));
        assertTrue(set.contains('a'));
        if ("DE".equals(locale.getCountry()))
            assertTrue(set.contains('ß'));
        if ("de".equals(locale.getLanguage()) && "CH".equals(locale.getCountry()))
            assertFalse(set.contains('ß'));
    }

}
