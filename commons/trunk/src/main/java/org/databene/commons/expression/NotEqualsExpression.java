/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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
import org.databene.commons.expression.BinaryExpression;
import org.databene.commons.math.ArithmeticEngine;

/**
 * Boolean {@link Expression} that checks for inequality.<br/><br/>
 * Created: 24.11.2010 14:02:10
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class NotEqualsExpression extends BinaryExpression<Boolean> {

	public NotEqualsExpression(Expression<?> term1, Expression<?> term2) {
        this("!=", term1, term2);
    }

	public NotEqualsExpression(String symbol, Expression<?> term1, Expression<?> term2) {
        super(symbol, term1, term2);
    }

	public Boolean evaluate(Context context) {
        return !ArithmeticEngine.defaultInstance().equals(term1.evaluate(context), term2.evaluate(context));
    }
	
}
