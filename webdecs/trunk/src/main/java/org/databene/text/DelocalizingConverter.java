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

package org.databene.text;

import org.databene.document.csv.CSVLineIterator;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Delocalizes a String bye replacing local characters by international latin characters.
 * For example the umlaut '�' is replaced by 'ae'.<br/>
 * <br/>
 * Created: 12.06.2006 18:53:55
 */
public class DelocalizingConverter implements Converter<String, String> {

    /** The logger */
    private static Log logger = LogFactory.getLog(DelocalizingConverter.class);

    /** a Map of replacements. The key indicates the character to replace,
     * the value the character to use for replacement*/
    private Map<Character, String> replacements;

    /** Default constructor */
    public DelocalizingConverter() throws IOException {
        init();
    }

    // Converter implementation ----------------------------------------------------------------------------------------

    public Class<String> getTargetType() {
        return String.class;
    }

    /**
     * Implementation of the Converter interface.
     * @see Converter
     */
    public String convert(String source) {
        String product = source;
        for (Map.Entry<Character, String> entry : replacements.entrySet())
            product = product.replace(String.valueOf(entry.getKey()), entry.getValue());
        return product;
    }

    // private initializers --------------------------------------------------------------------------------------------

    /**
     * Initializes the instance by reading the definition file of replacements
     * @throws IOException when file access fails.
     */
    private void init() throws IOException {
        replacements = new HashMap<Character, String>();
        CSVLineIterator iterator = new CSVLineIterator("org/databene/text/DelocalizingConverter.csv");

        String[] tokens;
        while ((tokens = iterator.next()) != null) {
            addReplacements(tokens);
        }
    }

    /**
     * adds a line from the replacement definition file to the replacement map.
     * @param tokens the tokens of one line in the file.
     * One line contains several replacement pairs.
     */
    private void addReplacements(String[] tokens) {
        if (tokens.length < 2)
            throw new ConfigurationError("At least two tokens needed to define a replacement");
        String replacement = tokens[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i++) {
            String token = tokens[i];
            if (token.length() != 1)
                throw new ConfigurationError("Source token length must be 1, wrong for token: " + token);
            addReplacement(token.charAt(0), replacement);
        }
    }

    /**
     * Adds one replacement pair to the replacement map.
     * @param original the character to replace
     * @param replacement the String to use as replacement
     */
    private void addReplacement(char original, String replacement) {
        String preset = replacements.get(original);
        if (preset != null) {
            if (preset.equals(replacement))
                logger.warn("double definition of replacement: " + original + " -> " + replacement);
            else
                logger.error("ambiguous definition of replacement: " + original + " -> " + replacement + " / " + preset);
        }
        replacements.put(original, replacement);
    }

}
