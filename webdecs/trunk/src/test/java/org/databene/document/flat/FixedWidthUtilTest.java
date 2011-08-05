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

import java.util.Arrays;

import org.databene.commons.format.Alignment;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link FixedWidthUtil}.<br/><br/>
 * Created at 29.04.2008 19:06:41
 * @since 0.4.2
 * @author Volker Bergmann
 */
public class FixedWidthUtilTest {

	@Test
	public void testLeft() {
		FixedWidthColumnDescriptor d3l = new FixedWidthColumnDescriptor("name", 3, Alignment.LEFT, ' ');
		FixedWidthColumnDescriptor[] array = new FixedWidthColumnDescriptor[] { d3l };
		assertTrue(Arrays.equals(array, parse("name[3]")));
		assertTrue(Arrays.equals(array, parse("name[3l]")));
		assertTrue(Arrays.equals(array, parse("name[3l ]")));
	}

	@Test
	public void testRight() {
		FixedWidthColumnDescriptor d3r = new FixedWidthColumnDescriptor("name", 3, Alignment.RIGHT, ' ');
		FixedWidthColumnDescriptor[] array = new FixedWidthColumnDescriptor[] { d3r };
		assertTrue(Arrays.equals(array, parse("name[3r]")));
		assertTrue(Arrays.equals(array, parse("name[3r ]")));
	}

	@Test
	public void testCenter() {
		FixedWidthColumnDescriptor d3c = new FixedWidthColumnDescriptor("name", 3, Alignment.CENTER, ' ');
		FixedWidthColumnDescriptor[] array = new FixedWidthColumnDescriptor[] { d3c };
		assertTrue(Arrays.equals(array, parse("name[3c]")));
		assertTrue(Arrays.equals(array, parse("name[3c ]")));
	}

	@Test
	public void testMultiple() {
		FixedWidthColumnDescriptor n3l = new FixedWidthColumnDescriptor("name", 3, Alignment.LEFT, ' ');
		FixedWidthColumnDescriptor a3r = new FixedWidthColumnDescriptor("age",  3, Alignment.RIGHT, '0');
		FixedWidthColumnDescriptor[] array = new FixedWidthColumnDescriptor[] { n3l, a3r };
		assertTrue(Arrays.equals(array, parse("name[3l],age[3r0]")));
	}

	@Test
	public void testParsePadChar() {
		FixedWidthColumnDescriptor d3l = new FixedWidthColumnDescriptor("name", 3, Alignment.LEFT, '_');
		FixedWidthColumnDescriptor[] d3l_a = new FixedWidthColumnDescriptor[] { d3l };
		assertTrue(Arrays.equals(d3l_a, parse("name[3l_]")));
	}

	private FixedWidthColumnDescriptor[] parse(String pattern) {
		return FixedWidthUtil.parseProperties(pattern);
	}
	
}
