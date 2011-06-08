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

import static org.junit.Assert.*;

import org.databene.commons.context.DefaultContext;
import org.junit.Test;

/**
 * Tests the ExclusiveOrExpression.<br/><br/>
 * Created: 08.06.2011 09:07:34
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class ExclusiveOrExpressionTest {

	@Test
	public void testTrueTrue() {
		assertFalse(new ExclusiveOrExpression(ExpressionUtil.constant(true), ExpressionUtil.constant(true)).evaluate(new DefaultContext()));
	}

	@Test
	public void testFalseFalse() {
		assertFalse(new ExclusiveOrExpression(ExpressionUtil.constant(false), ExpressionUtil.constant(false)).evaluate(new DefaultContext()));
	}

	@Test
	public void testTrueFalse() {
		assertTrue(new ExclusiveOrExpression(ExpressionUtil.constant(true), ExpressionUtil.constant(false)).evaluate(new DefaultContext()));
	}

	@Test
	public void testFalseTrue() {
		assertTrue(new ExclusiveOrExpression(ExpressionUtil.constant(false), ExpressionUtil.constant(true)).evaluate(new DefaultContext()));
	}

}
