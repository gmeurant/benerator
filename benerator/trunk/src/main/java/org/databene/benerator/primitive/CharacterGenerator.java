/*
 * (c) Copyright 2006 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.primitive;

import org.databene.benerator.*;
import org.databene.regex.RegexParser;
import org.databene.regex.RegexTokenizer;
import org.databene.benerator.sample.WeightedSampleGenerator;
import org.databene.commons.LocaleUtil;

import java.util.*;
import java.text.ParseException;

/**
 * Generates Character values from a character set or a regular expression.<br/>
 * <br/>
 * Created: 09.06.2006 20:34:55
 * @author Volker Bergmann
 */
public class CharacterGenerator implements Generator<Character> {

    /** The regular exception */
    private String pattern;

    /** The locale */
    private Locale locale;

    /** The set of characters to generate from */
    private Set<Character> values;

    /** dirty flag for consistency control */
    private boolean dirty;

    /** The SampleGenerator to choose from the character set */
    private WeightedSampleGenerator<Character> source;

    // constructors ----------------------------------------------------------------------------------------------------

    /**
     * initializes the generator to use letters of the fallback locale.
     * @see org.databene.commons.LocaleUtil#getFallbackLocale()
     */
    public CharacterGenerator() {
        this("\\w");
    }

    /**
     * initializes the generator to create character that match a regular expressions and the fallback locale.
     * @see org.databene.commons.LocaleUtil#getFallbackLocale()
     */
    public CharacterGenerator(String pattern) {
        this(pattern, LocaleUtil.getFallbackLocale());
    }

    /**
     * initializes the generator to create character that match a regular expressions and a locale.
     * @see org.databene.commons.LocaleUtil#getFallbackLocale()
     */
    public CharacterGenerator(String pattern, Locale locale) {
        this.pattern = pattern;
        this.locale = locale;
        this.values = new HashSet<Character>();
        this.dirty = true;
    }

    /**
     * initializes the generator to create characters from a character collection.
     * @see org.databene.commons.LocaleUtil#getFallbackLocale()
     */
    public CharacterGenerator(Collection<Character> values) {
        this.pattern = null;
        this.locale = LocaleUtil.getFallbackLocale();
        this.values = new HashSet<Character>(values);
        this.dirty = true;
    }

    // config properties -----------------------------------------------------------------------------------------------

    /** Returns the regular expression to match */
    public String getPattern() {
        return pattern;
    }

    /** Sets the regular expression to match */
    public void setPattern(String pattern) throws ParseException {
        this.pattern = pattern;
        this.dirty = true;
    }

    /** Returns the locale of which letters are taken */
    public Locale getLocale() {
        return locale;
    }

    /** Sets the locale of which letters are taken */
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.dirty = true;
    }

    /** Returns the available values */
    public Set<Character> getValues() {
        return values;
    }

    /** Sets the available values */
    public void setValues(Set<Character> set) {
        source.setValues(set);
        this.dirty = true;
    }

    // source interface ------------------------------------------------------------------------------------------------

    public Class<Character> getGeneratedType() {
        return Character.class;
    }

    /**
     * Initializes the generator's state.
     */
    public void validate() {
        if (dirty) {
            try {
                if (pattern != null)
                    values = new RegexParser(locale).parseCharSet(new RegexTokenizer(pattern));
                this.source = new WeightedSampleGenerator<Character>(Character.class, values);
                source.validate();
                this.dirty = false;
            } catch (ParseException e) {
                throw new IllegalGeneratorStateException(e);
            }
        }
    }

    /** @see org.databene.benerator.Generator#generate() */
    public Character generate() {
        if (dirty)
            validate();
        return source.generate();
    }

    public void reset() {
        if (dirty)
            validate();
        source.reset();
    }

    public void close() {
        if (dirty)
            validate();
        source.close();
    }

    public boolean available() {
        if (dirty)
            validate();
        return source.available();
    }

    public String toString() {
        return getClass().getSimpleName() + values;
    }
}
