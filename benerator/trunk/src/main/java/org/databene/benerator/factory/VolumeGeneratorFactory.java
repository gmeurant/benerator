/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.benerator.factory;

import java.util.Collection;
import java.util.Set;

import org.databene.benerator.Generator;
import org.databene.benerator.GeneratorProvider;
import org.databene.benerator.NonNullGenerator;
import org.databene.benerator.composite.StochasticArrayGenerator;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.distribution.SequenceManager;
import org.databene.benerator.primitive.BooleanGenerator;
import org.databene.benerator.primitive.DistributedLengthStringGenerator;
import org.databene.benerator.primitive.UniqueStringGenerator;
import org.databene.benerator.sample.AttachedWeightSampleGenerator;
import org.databene.benerator.sample.ConstantGenerator;
import org.databene.benerator.sample.OneShotGenerator;
import org.databene.benerator.sample.WeightedSample;
import org.databene.benerator.script.BeneratorScriptParser;
import org.databene.benerator.wrapper.AlternativeGenerator;
import org.databene.benerator.wrapper.CompositeStringGenerator;
import org.databene.benerator.wrapper.IteratingGenerator;
import org.databene.benerator.wrapper.UniqueCompositeArrayGenerator;
import org.databene.commons.ConfigurationError;
import org.databene.commons.converter.ConverterManager;
import org.databene.commons.iterator.ArrayIterable;
import org.databene.model.data.Uniqueness;

/**
 * {@link GeneratorFactory} implementation that generates docile data in order to avoid functional failures 
 * and combines them randomly and repetitively for generating large data volumes. Its primary purpose is 
 * data generation for performance tests.<br/>
 * <br/>
 * Created: 04.07.2011 09:34:34
 * @since 0.7.0
 * @author Volker Bergmann
 */
public class VolumeGeneratorFactory extends GeneratorFactory {

	public VolumeGeneratorFactory() {
		super(new GentleDefaultsProvider());
	}

	@Override
	public <T> Generator<T[]> createCompositeArrayGenerator(Class<T> componentType, Generator<T>[] sources, boolean unique) {
        if (unique)
        	return new UniqueCompositeArrayGenerator<T>(componentType, sources);
        else
        	return new StochasticArrayGenerator<T>(componentType, sources);
	}

	@Override
	public <T> Generator<T> createSampleGenerator(Collection<? extends T> values,
			Class<T> generatedType, boolean unique) {
		// TODO uniqueness
        return new AttachedWeightSampleGenerator<T>(generatedType, values);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Generator<T> createFromWeightedLiteralList(String valueSpec, Class<T> targetType,
            Distribution distribution, boolean unique) {
	    WeightedSample<T>[] samples = (WeightedSample<T>[]) BeneratorScriptParser.parseWeightedLiteralList(valueSpec);
	    if (distribution == null && !unique && weightsUsed(samples)) {
	    	AttachedWeightSampleGenerator<T> generator = new AttachedWeightSampleGenerator<T>(targetType);
	    	for (int i = 0; i < samples.length; i++) {
	    		WeightedSample<T> sample = samples[i];
	    		if (sample.getValue() == null)
	    			throw new ConfigurationError("null is not supported in values='...', drop it from the list and use a nullQuota instead");
	    		generator.addSample(sample);
	    	}
	    	return generator;
	    } else {
	    	String[] values = new String[samples.length];
	    	for (int i = 0; i < samples.length; i++) {
	    		T rawValue = samples[i].getValue();
	    		if (rawValue == null)
	    			throw new ConfigurationError("null is not supported in values='...', drop it from the list and use a nullQuota instead");
				String value = String.valueOf(rawValue);
	    		values[i] = value;
	    	}
	        IteratingGenerator<String> source = new IteratingGenerator<String>(new ArrayIterable<String>(values, String.class));
	        if (distribution == null)
	        	distribution = SequenceManager.RANDOM_SEQUENCE;
	        Generator<T> gen = GeneratorFactoryUtil.createConvertingGenerator(source, ConverterManager.getInstance().createConverter(String.class, targetType));
	    	return distribution.applyTo(gen, unique);
	    }
    }
	
    @Override
	public Generator<Boolean> createBooleanGenerator(double trueQuota) {
        return new BooleanGenerator(trueQuota);
    }

    private boolean weightsUsed(WeightedSample<?>[] samples) {
	    for (WeightedSample<?> sample : samples)
	    	if (sample.getWeight() != 1)
	    		return true;
	    return false;
    }

	@Override
	public NonNullGenerator<String> createStringGenerator(Set<Character> chars,
			Integer minLength, Integer maxLength, Distribution lengthDistribution, boolean unique) {
        if (unique) {
            return new UniqueStringGenerator(minLength, maxLength, chars);
        } else {
    		Generator<Character> charGenerator = createCharacterGenerator(chars);
    		if (lengthDistribution == null)
    			lengthDistribution = SequenceManager.RANDOM_SEQUENCE;
    		Generator<Integer> lengthGenerator = lengthDistribution.createNumberGenerator(Integer.class, minLength, maxLength, 1, false);
    		return new DistributedLengthStringGenerator(charGenerator, lengthGenerator);
        }
	}

	@Override
	public Generator<?> applyNullSettings(Generator<?> source, Boolean nullable, Double nullQuota)  {
    	if (nullQuota == null) {
    		if (nullable == null)
    			nullable = defaultsProvider.defaultNullable();
    		nullQuota = (nullable ?  defaultsProvider.defaultNullQuota() : 0);
    	}
		return GeneratorFactoryUtil.injectNulls(source, nullQuota);
	}

    @Override
	public <T> Generator<T> createSingleValueGenerator(T value, boolean unique) {
    	if (unique)
    		return new OneShotGenerator<T>(value);
    	else
    		return new ConstantGenerator<T>(value);
    }
/*
    @Override
	public boolean shouldNullifyEachNullable() {
		return true;
	}
*/
	@Override
	protected double defaultTrueQuota() {
		return 0.5;
	}

	@Override
	protected Distribution defaultLengthDistribution(Uniqueness uniqueness, boolean required) {
    	switch (uniqueness) {
	    	case ORDERED: 	return SequenceManager.STEP_SEQUENCE;
	    	case SIMPLE: 	return SequenceManager.EXPAND_SEQUENCE;
	    	default: 		if (required)
	    						return SequenceManager.RANDOM_SEQUENCE;
	    					else
	    						return null;
		}
	}

	@Override
	public Distribution defaultDistribution(Uniqueness uniqueness) {
		if (uniqueness == null)
			return SequenceManager.STEP_SEQUENCE;
    	switch (uniqueness) {
        	case ORDERED: 	return SequenceManager.STEP_SEQUENCE;
        	case SIMPLE: 	return SequenceManager.EXPAND_SEQUENCE;
        	default: 		return SequenceManager.RANDOM_SEQUENCE;
    	}
	}

	@Override
	protected boolean defaultUnique() {
		return false;
	}

	@Override
	public <T> Generator<T> createNullGenerator(Class<T> generatedType) {
		return new ConstantGenerator<T>(null, generatedType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NonNullGenerator<String> createCompositeStringGenerator(
			GeneratorProvider<?> partGeneratorProvider, int minParts, int maxParts, boolean unique) {
		AlternativeGenerator<String> result = new AlternativeGenerator<String>(String.class);
		for (int partCount = minParts; partCount <= maxParts; partCount++) {
			Generator<String>[] sources = new Generator[partCount];
			for (int i = 0; i < partCount; i++)
				sources[i] = GeneratorFactoryUtil.stringGenerator(partGeneratorProvider.create());
			result.addSource(new CompositeStringGenerator(unique, sources));
		}
		return GeneratorFactoryUtil.asNonNullGenerator(result);
	}

}
