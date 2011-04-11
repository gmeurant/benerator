/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.expression;

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.Context;
import org.databene.commons.Expression;

/**
 * Provides {@link Expression}-related utility methods.<br/>
 * <br/>
 * Created at 07.10.2009 22:33:14
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class ExpressionUtil {

    public static Object[] evaluateAll(Expression<?>[] expressions, Context context) {
	    Object[] result = new Object[expressions.length];
		for (int i = 0; i < expressions.length; i++)
			result[i] = expressions[i].evaluate(context);
	    return result;
    }

    public static boolean isNull(Expression<?> ex) {
    	if (ex == null)
    		return true;
    	return (ex instanceof ConstantExpression && ((ConstantExpression<?>) ex).getValue() == null);   
    }

	public static List<Object> evaluateAll(List<Expression<?>> expressions, Context context) {
	    List<Object> result = new ArrayList<Object>(expressions.size());
		for (Expression<?> expression : expressions)
			result.add(expression.evaluate(context));
	    return result;
    }

	public static <T> T evaluate(Expression<T> expression, Context context) {
	    return (expression != null ? expression.evaluate(context) : null);
    }

	public static <T> Expression<T> constant(T value) {
	    return new ConstantExpression<T>(value);
    }
	
	public Expression<String> unescape(Expression<String> source) {
		return new UnescapingExpression(source);
	}
	
	public static <T> Expression<T> simplify(Expression<T> expression, Context context) {
		// TODO v0.5.x apply where appropriate
		if (expression.isConstant() && !(expression instanceof ConstantExpression)) 
			return new ConstantExpression<T>(evaluate(expression, context));
		else
			return expression;
	}
	
}
