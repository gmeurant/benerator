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

package org.databene.commons.expression;

import org.databene.commons.Context;
import org.databene.commons.Expression;
import org.databene.commons.math.ArithmeticEngine;

/**
 * Boolean {@link Expression} that tells if a value lies within a given range.<br/><br/>
 * Created: 07.06.2011 22:07:56
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class InRangeExpression extends CompositeExpression<Object, Boolean> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InRangeExpression(Expression value, Expression min, Expression max) {
		super(value, min, max);
	}

	public Boolean evaluate(Context context) {
		Object value = terms[0].evaluate(context);
		Object minValue = terms[1].evaluate(context);
		Object maxValue = terms[2].evaluate(context);
		return ArithmeticEngine.defaultInstance().lessOrEquals(minValue, value) &&
			ArithmeticEngine.defaultInstance().lessOrEquals(value, maxValue);
	}

	@Override
	public String toString() {
		return "(" + terms[0] + " <= " + terms[1] + " <= " + terms[2] + ")";
	}

}
