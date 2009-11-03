/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.BeanUtil;
import org.databene.commons.Context;
import org.databene.commons.Expression;

/**
 * {@link Expression} implementation that performs a static method call.<br/>
 * <br/>
 * Created at 27.07.2009 08:58:30
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class StaticMethodCallExpression implements Expression<Object> {
	
	private Class<?> targetClass;
	private String methodName;
	private Expression<?>[] argExpressions;
	
	public StaticMethodCallExpression(Class<?> targetClass, String methodName, Expression<?> ... argExpressions) {
	    this.targetClass = targetClass;
	    this.methodName = methodName;
	    this.argExpressions = argExpressions;
    }

	public Object evaluate(Context context) {
    	Object[] args = ExpressionUtil.evaluateAll(argExpressions, context);
		return BeanUtil.invokeStatic(targetClass, methodName, args);
    }

}
