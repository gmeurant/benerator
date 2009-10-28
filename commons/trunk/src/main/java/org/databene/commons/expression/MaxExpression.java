/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

import java.util.Comparator;

import org.databene.commons.ComparableComparator;
import org.databene.commons.Context;
import org.databene.commons.Expression;

/**
 * Calculates the maximum value of several arguments. 
 * <code>null</code> values are ignored <br/><br/>
 * Created: 19.10.2009 01:31:42
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class MaxExpression<E> implements Expression<E> {

	private Expression<E>[] argumentExpressions;
	private Comparator<E> comparator;

	@SuppressWarnings("unchecked")
    public MaxExpression(Expression<E>... argumentExpressions) {
	    this(new ComparableComparator(), argumentExpressions);
    }

	public MaxExpression(Comparator<E> comparator, Expression<E>... argumentExpressions) {
	    this.argumentExpressions = argumentExpressions;
	    this.comparator = comparator;
    }

    public E evaluate(Context context) {
    	E max = argumentExpressions[0].evaluate(context);
	    for (int i = 1; i < argumentExpressions.length; i++) {
	    	E tmp = argumentExpressions[i].evaluate(context);
	    	if (comparator.compare(tmp, max) > 0)
	    		max = tmp;
	    }
	    return max;
    }
	
}
