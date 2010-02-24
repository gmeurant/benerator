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

package org.databene.domain.product;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.factory.GeneratorFactory;
import org.databene.benerator.wrapper.GeneratorWrapper;

/**
 * Generates 8-digit EAN codes.<br/>
 * <br/>
 * Created: 30.07.2007 21:47:30
 * @author Volker Bergmann
 */
public class EAN8Generator extends GeneratorWrapper<String, String> {

    private boolean unique;

    public EAN8Generator() {
        this(false);
    }

    public EAN8Generator(boolean unique) {
        super(null);
        setUnique(unique);
    }

    public boolean isUnique() {
        return unique;
    }

    private void setUnique(boolean unique) {
        this.unique = unique;
    }

    // Generator interface --------------------------------------------------------------------
    
    public Class<String> getGeneratedType() {
        return String.class;
    }

    @Override
    public synchronized void init(BeneratorContext context) {
        if (unique)
            setSource(GeneratorFactory.getUniqueRegexStringGenerator("[0-9]{7}", 7, 7));
        else
            setSource(GeneratorFactory.getRegexStringGenerator("[0-9]{7}", 7, 7));
        super.init(context);
    }
    
    public String generate() {
        char[] chars = new char[8];
        source.generate().getChars(0, 7, chars, 0);
        chars[7] = chars[6];
        chars[6] = '0';
        int sum = 0;
        for (int i = 0; i < 8; i++)
            sum += (chars[i] - '0') * (1 + (i % 2) * 2);
        if (sum % 10 == 0)
            chars[6] = '0';
        else
            chars[6] = (char)('0' + 10 - (sum % 10));
        return new String(chars);
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return getClass().getSimpleName() + (unique ? "[unique]" : "");
    }

}
