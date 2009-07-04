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

package org.databene.benerator.distribution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.databene.benerator.Generator;
import org.databene.benerator.primitive.number.adapter.SequenceFactory;
import org.databene.benerator.primitive.number.distribution.RandomWalkSequence;
import org.databene.benerator.primitive.number.distribution.ShuffleSequence;
import org.databene.benerator.sample.SequencedSampleGenerator;
import org.databene.benerator.util.GeneratorUtil;
import org.databene.commons.ConfigurationError;

/**
 * Provides access to specific Sequence number Generators.<br/>
 * <br/>
 * Created: 11.09.2006 21:12:57
 * @author Volker Bergmann
 */
public class Sequence implements Distribution {
	
    private static Map<String, Sequence> instances = new HashMap<String, Sequence>();

    public static final Sequence RANDOM      = new Sequence("random");
    public static final Sequence SHUFFLE     = new ShuffleSequence();
    public static final Sequence CUMULATED   = new Sequence("cumulated");
    public static final Sequence RANDOM_WALK = new RandomWalkSequence();
    public static final Sequence STEP        = new Sequence("step");
    public static final Sequence WEDGE       = new Sequence("wedge");
    public static final Sequence BIT_REVERSE = new Sequence("bitReverse");
    
    private String name;
    
    // Construction & lookup -------------------------------------------------------------------------------------------
    
    public static Sequence getInstance(String name, boolean required) {
        Sequence sequence = instances.get(name);
        if (sequence == null && required)
            throw new ConfigurationError("Sequence not defined: " + name);
        return sequence;
    }

    public static Collection<Sequence> getInstances() {
        return instances.values();
    }
    
    protected Sequence(String name) {
        this.name = name;
        if (!instances.containsKey(name))
        	instances.put(name, this);
    }
    
    // interface -------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public <T extends Number> Generator<T> createGenerator(Class<T> numberType, T min, T max, T precision) {
	    return SequenceFactory.createGenerator(getName(), numberType, min, max, precision); 
	    // TODO provide something easier for custom sequences
    }

    @SuppressWarnings("unchecked")
    public <S, P> Generator<P> applyTo(Generator<S> source) {
	    return (Generator<P>) new SequencedSampleGenerator<S>(source.getGeneratedType(), this, GeneratorUtil.allProducts(source));
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Sequence that = (Sequence) obj;
        return this.name.equals(that.name);
    }

}
