/*
 * (c) Copyright 2006-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.wrapper;

import org.databene.benerator.Generator;
import org.databene.benerator.distribution.sequence.RandomIntegerGenerator;
import org.databene.benerator.util.GeneratorUtil;

/**
 * On each call to generate(), it chooses a generator from a collection,
 * calls its generate() method and returns the product.<br/>
 * <br/>
 * Created: 30.08.2006 21:56:59
 */
public class AlternativeGenerator<E> extends MultiGeneratorWrapper<E, E> {

    private Generator<Integer> indexGenerator;
    private Class<E> targetType;

    // constructors ----------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public AlternativeGenerator() {
        this((Class<E>) Object.class);
    }

    /** Initializes the generator to a collection of source generators */
    public AlternativeGenerator(Generator<E>... sources) {
        this(GeneratorUtil.commonTargetTypeOf(sources));
    }

	/** Initializes the generator to a collection of source generators */
    public AlternativeGenerator(Class<E> targetType, Generator<E>... sources) {
        super(sources);
        this.targetType = targetType;
    }

    // Generator implementation ----------------------------------------------------------------------------------------

    public Class<E> getGeneratedType() {
        return targetType;
    }

    @Override
    public void validate() {
        if (dirty) {
            super.validate();
            this.indexGenerator = new RandomIntegerGenerator(0, sources.length - 1);
            indexGenerator.validate();
            dirty = false;
        }
    }
    
    @Override
    public boolean available() {
        validate();
        for (Generator<? extends Object> source : sources)
            if (source.available())
                return true;
        return false;
    }

    /** @see org.databene.benerator.Generator#generate() */
    public E generate() {
        if (!available())
            GeneratorUtil.stateException(this);
        for (int i = 0; i < 1000; i++) {
            Generator<E> generator = getSource(indexGenerator.generate());
            if (generator.available())
                return generator.generate();
        }
        throw new IllegalStateException("Unable to choose an available generator");
    }

}
