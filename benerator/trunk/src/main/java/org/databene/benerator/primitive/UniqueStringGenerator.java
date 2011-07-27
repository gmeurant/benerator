/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorContext;
import org.databene.benerator.NonNullGenerator;
import org.databene.benerator.wrapper.MultiGeneratorWrapper;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.commons.CollectionUtil;
import org.databene.commons.CharSet;
import org.databene.commons.ArrayFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generates unique strings of variable length.<br/>
 * <br/>
 * Created: 16.11.2007 11:56:15
 * @author Volker Bergmann
 */
public class UniqueStringGenerator extends MultiGeneratorWrapper<String, String> implements NonNullGenerator<String> {

    private int minLength;
    private int maxLength;
    private char[] charSet;

    // constructors ----------------------------------------------------------------------------------------------------

    public UniqueStringGenerator() {
        this(4, 8, new CharSet('A', 'Z').getSet());
    }

    public UniqueStringGenerator(int minLength, int maxLength, Collection<Character> charSet) {
        this(minLength, maxLength, CollectionUtil.toCharArray(charSet));
    }

	public UniqueStringGenerator(int minLength, int maxLength, char ... charSet) {
    	super(String.class);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.charSet = charSet;
    }
    
    // properties ------------------------------------------------------------------------------------------------------

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    // Generator interface ---------------------------------------------------------------------------------------------

    @Override
    public void init(GeneratorContext context) {
    	assertNotInitialized();
    	// create sub generators
        List<Generator<? extends String>> subGens = new ArrayList<Generator<? extends String>>(maxLength - minLength + 1);
        for (int i = minLength; i <= maxLength; i++)
            subGens.add(new UniqueFixedLengthStringGenerator(i, charSet));
        setSources(subGens);
        super.init(context);
    }

	public ProductWrapper<String> generate(ProductWrapper<String> wrapper) {
    	assertInitialized();
    	return generateFromRandomSource(wrapper);
    }

	public String generate() {
		ProductWrapper<String> wrapper = generate(new ProductWrapper<String>());
		return (wrapper != null ? wrapper.unwrap() : null);
	}

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + minLength + "<=length<=" + maxLength + ", " +
                "charSet=[" + ArrayFormat.formatChars(", ", charSet) + "]]";
    }

}
