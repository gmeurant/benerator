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

package org.databene.script.quickscript;

import org.databene.commons.Accessor;
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringCharacterIterator;
import org.databene.commons.accessor.ConstantAccessor;
import org.databene.commons.accessor.FallbackAccessor;
import org.databene.commons.accessor.FeatureAccessor;
import org.databene.commons.accessor.GraphAccessor;

import java.util.List;
import java.util.ArrayList;

/**
 * Tokenizes a QuickScript text.<br/>
 * <br/>
 * Created: 12.06.2007 17:41:17
 * @author Volker Bergmann
 */
class QuickScriptTokenizer {

    public static Accessor[] tokenize(String text) {
        List<Accessor> tokens = new ArrayList<Accessor>();
        StringCharacterIterator iterator = new StringCharacterIterator(text);
        while (iterator.hasNext())
            tokens.add(nextToken(iterator));
        return CollectionUtil.toArray(tokens, Accessor.class);
    }

    private static Accessor nextToken(StringCharacterIterator iterator) {
        if (!iterator.hasNext())
            return null;
        if (iterator.next() == '$') {
            if (iterator.hasNext() && iterator.next() == '{') {
                // variable
                return parseVariable(iterator);
            } else {
                // text
                iterator.pushBack();
                iterator.pushBack();
                return parseText(iterator);
            }
        } else {
            // text
            iterator.pushBack();
            return parseText(iterator);
        }
    }

    private static Accessor parseText(StringCharacterIterator iterator) {
        StringBuffer buffer = new StringBuffer();
        char c = ' ';
        while (iterator.hasNext() && (c = iterator.next()) != '$')
            buffer.append(c);
        if (c == '$')
            iterator.pushBack();
        return new ConstantAccessor<String>(buffer.toString());
    }

    private static Accessor parseVariable(StringCharacterIterator iterator) {
        StringBuffer buffer = new StringBuffer();
        char c = ' ';
        while (iterator.hasNext() && (c = iterator.next()) != '}')
            buffer.append(c);
        String expression = buffer.toString().trim();
        if (c == '}') {
            if (expression.contains("."))
                return new FallbackAccessor(
                        new FeatureAccessor(expression), 
                        new GraphAccessor(expression)
                    );
            else
                return new FeatureAccessor(expression);
        } else
            return new ConstantAccessor<String>("${" + expression);
    }
}
