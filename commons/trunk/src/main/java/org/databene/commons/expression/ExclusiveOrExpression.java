/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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
import org.databene.commons.converter.AnyConverter;
import org.databene.commons.expression.BinaryExpression;

/**
 * Boolean {@link Expression} that checks if the result of one 
 * expressions is less than the result of another one.<br/><br/>
 * Created: 24.11.2010 14:23:13
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class ExclusiveOrExpression extends BinaryExpression<Boolean> {
	
	public ExclusiveOrExpression(Expression<?> term1, Expression<?> term2) {
		super("XOR", term1, term2);
	}

	public Boolean evaluate(Context context) {
	    Boolean arg1 = AnyConverter.convert(term1.evaluate(context), Boolean.class);
		Boolean arg2 = AnyConverter.convert(term2.evaluate(context), Boolean.class);
		return arg1 ^ arg2;
	}
	
}