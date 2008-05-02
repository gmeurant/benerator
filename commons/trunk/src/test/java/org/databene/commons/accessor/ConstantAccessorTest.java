/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.accessor;

import junit.framework.TestCase;

/**
 * Tests the ConstantAccessor.<br/><br/>
 * Created at 02.05.2008 14:13:01
 * @since 0.4.3
 * @author Volker Bergmann
 */
public class ConstantAccessorTest extends TestCase {

	public void testGet() {
		ConstantAccessor<Integer> accessor = new ConstantAccessor<Integer>(1);
		assertEquals(1, (int) accessor.getValue(null));
	}

	public void testEquals() {
		ConstantAccessor<Integer> a1 = new ConstantAccessor<Integer>(1);
		// simple test
		assertFalse(a1.equals(null));
		assertFalse(a1.equals(""));
		assertTrue(a1.equals(a1));
		// real comparisons
		assertTrue(a1.equals(new ConstantAccessor<Integer>(1)));
		ConstantAccessor<Integer> a0 = new ConstantAccessor<Integer>(null);
		ConstantAccessor<Integer> a2 = new ConstantAccessor<Integer>(2);
		assertFalse(a0.equals(a1));
		assertFalse(a1.equals(a0));
		assertFalse(a1.equals(a2));
		assertFalse(a2.equals(a1));
	}
}
