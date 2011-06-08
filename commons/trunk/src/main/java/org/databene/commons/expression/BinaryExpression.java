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

package org.databene.commons.expression;

import org.databene.commons.Expression;

/**
 * Abstract parent class for expression that evaluate two terms.<br/>
 * <br/>
 * Created at 06.10.2009 14:26:04
 * @since 0.5.0
 * @author Volker Bergmann
 */

public abstract class BinaryExpression<E> implements WrapperExpression<E> {

	protected String symbol;
	protected Expression<?> term1;
	protected Expression<?> term2;

	public BinaryExpression(Expression<?> term1, Expression<?> term2) {
		this(null, term1, term2);
    }
	
	public BinaryExpression(String symbol, Expression<?> term1, Expression<?> term2) {
		this.symbol = symbol;
	    this.term1 = term1;
	    this.term2 = term2;
    }
	
	public Expression<?>[] getSourceExpressions() {
		return new Expression[] { term1, term2 };
	}
	
	public boolean isConstant() {
	    return term1.isConstant() && term2.isConstant();
	}
	
	@Override
	public String toString() {
		return "(" + term1 + " " + symbol + " " + term2 + ")";
	}
	
}
