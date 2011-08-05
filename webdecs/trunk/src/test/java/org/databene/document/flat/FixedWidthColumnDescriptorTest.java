/*
 * (c) Copyright 2008-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.document.flat;

import org.databene.commons.format.Alignment;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link FixedWidthColumnDescriptor}.<br/><br/>
 * Created at 05.05.2008 07:18:54
 * @since 0.5.3
 * @author Volker Bergmann
 */
public class FixedWidthColumnDescriptorTest {

	@Test
	public void testEquals() {
		FixedWidthColumnDescriptor d1 = new FixedWidthColumnDescriptor("name", 8, Alignment.LEFT, ' ');
		// simple tests
		assertFalse(d1.equals(null));
		assertFalse(d1.equals(""));
		assertTrue(d1.equals(d1));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name2", 8, Alignment.LEFT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name3", 9, Alignment.LEFT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name4", 8, Alignment.RIGHT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name5", 8, Alignment.LEFT, '_')));
	}
	
}
