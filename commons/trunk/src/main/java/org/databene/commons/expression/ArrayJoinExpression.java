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

import org.databene.commons.ArrayUtil;
import org.databene.commons.Context;
import org.databene.commons.Expression;

/**
 * {@link Expression} implementation which assembles other expression that evaluate to arrays
 * and joins their results to a single array.<br/><br/>
 * Created: 11.09.2010 07:57:38
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class ArrayJoinExpression<E> extends CompositeExpression<E[], E[]> {
	
	private Class<E> componentType;

    public ArrayJoinExpression(Class<E> componentType, Expression<E[]>... terms) {
	    super(terms);
	    this.componentType = componentType;
    }

    @SuppressWarnings("unchecked")
    public E[] evaluate(Context context) {
    	E[][] arrays = (E[][]) ExpressionUtil.evaluateAll(terms, context);
    	int totalLength = totalLength(arrays);
    	E[] result = ArrayUtil.newInstance(componentType(arrays), totalLength);
    	int resultIndex = 0;
    	for (E[] array : arrays)
    		for (int localIndex = 0; localIndex < array.length; localIndex++)
    			result[resultIndex] = array[localIndex];
    	return result;
    }

	@SuppressWarnings("unchecked")
    private Class<E> componentType(E[][] arrays) {
		if (this.componentType == null) {
		    for (E[] array : arrays)
		    	if (array != null) {
		    		this.componentType = (Class<E>) array.getClass().getComponentType();
		    		break;
		    	}
		}
		return (this.componentType != null ? this.componentType : (Class<E>) Object.class);
    }

	private int totalLength(E[][] arrays) {
	    int totalLength = 0;
    	for (E[] array : arrays)
    		totalLength += array.length;
	    return totalLength;
    }

}
