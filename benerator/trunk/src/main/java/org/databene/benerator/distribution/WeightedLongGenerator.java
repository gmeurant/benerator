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

import org.databene.benerator.*;
import org.databene.benerator.distribution.function.ConstantFunction;
import org.databene.benerator.primitive.number.adapter.AbstractNumberGenerator;

import java.util.Random;
import java.util.Arrays;

/**
 * Long Generator that supports a weight function.<br/>
 * <br/>
 * Created: 18.06.2006 15:00:41
 * @since 0.1
 * @author Volker Bergmann
 */
public class WeightedLongGenerator extends AbstractNumberGenerator<Long> {

    private WeightFunction function;

    private Random randomizer;
    private float[] probSum;

    // constructors ----------------------------------------------------------------------------------------------------

    public WeightedLongGenerator() {
        this(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public WeightedLongGenerator(long min, long max) {
        this(min, max, 1);
    }

    public WeightedLongGenerator(long min, long max, long precision) {
        this(min, max, precision, new ConstantFunction(1));
    }

    public WeightedLongGenerator(long min, long max, WeightFunction function) {
        this(min, max, 1, function);
    }

    public WeightedLongGenerator(long min, long max, long precision, WeightFunction function) {
        super(Long.class, min, max, precision);
        this.function = function;
        this.randomizer = new Random();
        this.dirty = true;
    }

    // properties ------------------------------------------------------------------------------------------------------

    public Distribution getDistribution() {
        return function;
    }

    public void setDistribution(Distribution distribution) {
        if (!(distribution instanceof WeightFunction))
            throw new IllegalArgumentException("Function expected, found: " + distribution);
        this.function = (WeightFunction) distribution;
    }

    // Generator implementation ----------------------------------------------------------------------------------------

    @Override
	public void validate() {
        if (dirty) {
            normalize();
            super.validate();
            dirty = false;
        }
    }

    public Long generate() throws IllegalGeneratorStateException {
        if (dirty)
            validate();
        float random = randomizer.nextFloat();
        long n = intervallNoOfRandom(random);
        return min + n * precision;
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private long intervallNoOfRandom(float random) {
        int i = Arrays.binarySearch(probSum, random);
        if (i < 0)
            i = - i - 1;
        if (i >= probSum.length)
            return probSum.length - 1;
        return i;
    }

    private void normalize() {
        int sampleCount = (int) ((max - min) / precision) + 1;
        if (sampleCount > 100000)
            throw new InvalidGeneratorSetupException("precision", "too small, resulting in a set of " + sampleCount + " samples");
        probSum = new float[sampleCount];
        if (sampleCount == 1)
            probSum[0] = 1;
        else {
            double sum = 0;
            for (int i = 0; i < sampleCount; i++) {
                long dx = (max - min) / (sampleCount - 1);
                long x = min + i * dx;
                sum += function.value(x);
                probSum[i] = (float) sum;
            }
            if (sum == 0) {
                float avgProp = (float)1./sampleCount;
                for (int i = 0; i < sampleCount; i++)
                    probSum[i] = (i + 1) * avgProp;
            } else if (sum < 0)
                throw new IllegalGeneratorStateException(
                        "Invalid WeightFunction: Sum is negative (" + sum + ") for " + function);
            for (int i = 0; i < sampleCount; i++) {
                probSum[i] /= (float) sum;
            }
        }
    }
    
}
