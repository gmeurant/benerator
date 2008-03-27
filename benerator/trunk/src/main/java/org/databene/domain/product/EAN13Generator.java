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

package org.databene.domain.product;

import org.databene.benerator.factory.GeneratorFactory;
import org.databene.benerator.wrapper.GeneratorWrapper;

/**
 * Generates 13-digits EAN codes.<br/>
 * <br/>
 * Created: 30.07.2007 21:47:30
 */
public class EAN13Generator extends GeneratorWrapper<String, String> {

    private boolean unique;

    public EAN13Generator() {
        this(false);
    }

    public EAN13Generator(boolean unique) {
        super(null);
        setUnique(unique);
    }

    private void setUnique(boolean unique) {
        this.unique = unique;
        if (unique)
            setSource(GeneratorFactory.getUniqueRegexStringGenerator("[0-9]{12}", 12, 12, null));
        else
            setSource(GeneratorFactory.getRegexStringGenerator("[0-9]{12}", 12, 12, null, 0));
    }

    // Generator interface ---------------------------------------------------------------------------------------------

    public Class<String> getGeneratedType() {
        return String.class;
    }

    public String generate() {
        char[] chars = new char[13];
        int sum = 0;
        source.generate().getChars(0, 12, chars, 0);
        for (int i = 0; i < 12; i++)
            sum += (chars[i] - '0') * (1 + (i % 2) * 2);
        if (sum % 10 == 0)
            chars[12] = '0';
        else
            chars[12] = (char)('0' + 10 - (sum % 10));
        return new String(chars);

    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    public String toString() {
        return getClass().getSimpleName() + (unique ? "[unique]" : "");
    }
}
