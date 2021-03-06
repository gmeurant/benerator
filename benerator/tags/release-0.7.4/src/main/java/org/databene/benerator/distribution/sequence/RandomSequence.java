/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.distribution.sequence;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.databene.benerator.Generator;
import org.databene.benerator.NonNullGenerator;
import org.databene.benerator.distribution.Sequence;
import org.databene.benerator.distribution.SequenceManager;
import org.databene.benerator.wrapper.WrapperFactory;
import org.databene.commons.BeanUtil;
import org.databene.commons.NumberUtil;

import static org.databene.commons.NumberUtil.*;

/**
 * {@link Sequence} implementation that creates generators with a random uniform distribution.<br/>
 * <br/>
 * Created at 27.07.2009 06:31:25
 * @since 0.6.0
 * @author Volker Bergmann
 */

public class RandomSequence extends Sequence {

    public <T extends Number> NonNullGenerator<T> createNumberGenerator(Class<T> numberType, T min, T max, T granularity, boolean unique) {
    	NonNullGenerator<? extends Number> base;
    	if (unique) {
    		return SequenceManager.EXPAND_SEQUENCE.createNumberGenerator(numberType, min, max, granularity, unique);
    	} else if (BeanUtil.isIntegralNumberType(numberType)) {
        	long lMax = (max != null ? max.longValue() :
        			Math.min(RandomLongGenerator.DEFAULT_MAX, NumberUtil.maxValue(numberType).longValue()));
			if (Integer.class.equals(numberType.getClass()))
				base = new RandomIntegerGenerator(toInteger(min), toInteger(lMax), toInteger(granularity));
			else if (BigInteger.class.equals(numberType.getClass()))
				base = new RandomBigIntegerGenerator(toBigInteger(min), toBigInteger(lMax), toBigInteger(granularity));
			else if (BigDecimal.class.equals(numberType.getClass()))
				base = new RandomBigDecimalGenerator(toBigDecimal(min), toBigDecimal(lMax), toBigDecimal(granularity));
			else
				base = new RandomLongGenerator(toLong(min), lMax, toLong(granularity));
    	} else {
    		double dMax = (max != null ? max.doubleValue() : Long.MAX_VALUE);
			base = new RandomDoubleGenerator(toDouble(min), dMax, toDouble(granularity));
    	}
		return WrapperFactory.asNonNullNumberGeneratorOfType(numberType, base, min, granularity);
	}

    @Override
    public <T> Generator<T> applyTo(Generator<T> source, boolean unique) {
    	if (unique)
    		return SequenceManager.EXPAND_SEQUENCE.applyTo(source, unique);
    	else
    		return super.applyTo(source, unique);
    }

	@Override
	public String toString() {
	    return BeanUtil.toString(this);
	}
	
}
