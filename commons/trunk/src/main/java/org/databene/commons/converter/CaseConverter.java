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

package org.databene.commons.converter;

import org.databene.commons.Converter;

import java.util.Locale;

/**
 * Converts a String's characters to upper or lower case.<br/>
 * <br/>
 * Created: 12.06.2006 19:05:09
 */
public class CaseConverter extends NullsafeConverterProxy<String, String> {

    public CaseConverter(boolean toUpper, Locale locale) {
        this(toUpper, locale, null);
    }

    public CaseConverter(boolean toUpper, Locale locale, String nullResult) {
        super(new ConverterImpl(toUpper, locale), nullResult);
    }

    private static final class ConverterImpl implements Converter<String, String> {
        /** Mode flag for the Converter. If set to true, it converts to upper case, else to lower case */
        private boolean toUpper;

        private Locale locale;

        /**
         * @param toUpper If set to true, the instance converts to upper case, else to lower case
         */
        public ConverterImpl(boolean toUpper, Locale locale) {
            this.toUpper = toUpper;
            this.locale = locale;
        }

        public Class<String> getTargetType() {
            return String.class;
        }

        /**
         * @see org.databene.commons.Converter
         */
        public String convert(String source) {
            if (source == null)
                return null;
            return (toUpper ? source.toUpperCase(locale) : source.toLowerCase(locale));
        }
    }
}
