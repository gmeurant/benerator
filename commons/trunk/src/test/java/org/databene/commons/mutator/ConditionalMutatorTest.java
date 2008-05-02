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

package org.databene.commons.mutator;

import org.databene.commons.Accessor;
import org.databene.commons.Mutator;
import org.databene.commons.UpdateFailedException;

import junit.framework.TestCase;

/**
 * Tests the {@link ConditionalMutator}.<br/><br/>
 * Created at 02.05.2008 11:45:19
 * @since 0.4.3
 * @author Volker Bergmann
 */
public class ConditionalMutatorTest extends TestCase {

    public static final int OVERWRITE        = 1;
    public static final int SET_IF_UNDEFINED = 2;
    public static final int SET_IF_GREATER   = 3;

	public void testAssertEqualsSuccess() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(1);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.ASSERT_EQUALS);
		mutator.setValue(null, 1);
	}

	public void testAssertEqualsFailure() throws UpdateFailedException {
		try {
			ConnectorMock connector = new ConnectorMock(1);
			ConditionalMutator mutator = createMutator(connector,
					ConditionalMutator.ASSERT_EQUALS);
			mutator.setValue(null, 2);
			fail("Expected " + UpdateFailedException.class.getSimpleName());
		} catch (UpdateFailedException e) {
			// this is expected
		}
	}

	private ConditionalMutator createMutator(ConnectorMock connector, int mode) {
		return new ConditionalMutator(connector, connector, mode);
	}

	public void testOverwrite() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(1);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.OVERWRITE);
		mutator.setValue(null, 2);
		assertEquals(2, (int) connector.value);
	}

	public void testSetIfUndefinedTrue() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(null);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.SET_IF_UNDEFINED);
		mutator.setValue(null, 2);
		assertEquals(2, (int) connector.value);
	}

	public void testSetIfUndefinedFalse() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(1);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.SET_IF_UNDEFINED);
		mutator.setValue(null, 2);
		assertEquals(1, (int) connector.value);
	}

	public void testSetValueIfGreaterTrue() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(1);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.SET_IF_GREATER);
		mutator.setValue(null, 2);
		assertEquals(2, (int) connector.value);
	}
	
	public void testSetValueIfGreaterFalse() throws UpdateFailedException {
		ConnectorMock connector = new ConnectorMock(1);
		ConditionalMutator mutator = createMutator(connector, ConditionalMutator.SET_IF_GREATER);
		mutator.setValue(null, 0);
		assertEquals(1, (int) connector.value);
	}
	
	public static class ConnectorMock implements Mutator<Object, Integer>, Accessor<Object, Integer> {
		
		public Integer value;

		public ConnectorMock(Integer value) {
			this.value = value;
		}
		
		public void setValue(Object target, Integer value) {
			this.value = value;
		}

		public Integer getValue(Object target) {
			return value;
		}
		
	}
}
