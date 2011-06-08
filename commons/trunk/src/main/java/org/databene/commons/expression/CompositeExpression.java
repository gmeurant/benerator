/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayFormat;
import org.databene.commons.ArrayUtil;
import org.databene.commons.Expression;

/**
 * Expression that evaluates the results of other Expressions.<br/>
 * <br/>
 * Created: 18.06.2007 17:02:17
 * @author Volker Bergmann
 */
public abstract class CompositeExpression<S, R> implements WrapperExpression<R> {

	protected String symbol;
    protected Expression<S>[] terms;

    protected CompositeExpression(Expression<S>... terms) {
    	this(null, terms);
    }

    protected CompositeExpression(String symbol, Expression<S>... terms) {
    	this.symbol = symbol;
        this.terms = terms;
    }

	public Expression<S>[] getTerms() {
		return terms;
	}
	
	public Expression<?>[] getSourceExpressions() {
		return getTerms();
	}
	
    public void addTerm(Expression<S> term) {
    	this.terms = ArrayUtil.append(term, this.terms);
    }
    
    public boolean isConstant() {
        for (Expression<?> term : terms)
        	if (!term.isConstant())
        		return false;
        return true;
    }
    
	@Override
	public String toString() {
	    return "(" + ArrayFormat.format(" " + symbol + " ", terms) + ")";
	}
	
}
