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

import org.databene.commons.Context;
import org.databene.commons.Expression;

/**
 * {@link Expression} implementation that calculates the minimum of several values.<br/>
 * <br/>
 * Created at 27.07.2009 09:06:36
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class MinExpression implements Expression {

	private Expression[] argumentExpressions;

	public MinExpression(Expression... argumentExpressions) {
	    this.argumentExpressions = argumentExpressions;
    }

    @SuppressWarnings("unchecked")
    public Object evaluate(Context context) {
    	Comparable min = (Comparable) argumentExpressions[0].evaluate(context);
	    for (int i = 1; i < argumentExpressions.length; i++) {
	    	Comparable tmp = (Comparable) argumentExpressions[i].evaluate(context);
	    	if (min == null)
	    		min = tmp;
	    	else if (tmp != null && tmp.compareTo(min) < 0)
	    		min = tmp;
	    }
	    return min;
    }
	
}
