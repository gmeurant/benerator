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
 * Tests the {@link FlatFileUtil}.<br/><br/>
 * Created at 29.04.2008 19:06:41
 * @since 0.4.2
 * @author Volker Bergmann
 */
public class FlatFileUtilTest {

	@Test
	public void testLeft() {
		FlatFileColumnDescriptor d3l = new FlatFileColumnDescriptor("name", 3, Alignment.LEFT, ' ');
		FlatFileColumnDescriptor[] array = new FlatFileColumnDescriptor[] { d3l };
		assertTrue(Arrays.equals(array, parse("name[3]")));
		assertTrue(Arrays.equals(array, parse("name[3l]")));
		assertTrue(Arrays.equals(array, parse("name[3l ]")));
	}

	@Test
	public void testRight() {
		FlatFileColumnDescriptor d3r = new FlatFileColumnDescriptor("name", 3, Alignment.RIGHT, ' ');
		FlatFileColumnDescriptor[] array = new FlatFileColumnDescriptor[] { d3r };
		assertTrue(Arrays.equals(array, parse("name[3r]")));
		assertTrue(Arrays.equals(array, parse("name[3r ]")));
	}

	@Test
	public void testCenter() {
		FlatFileColumnDescriptor d3c = new FlatFileColumnDescriptor("name", 3, Alignment.CENTER, ' ');
		FlatFileColumnDescriptor[] array = new FlatFileColumnDescriptor[] { d3c };
		assertTrue(Arrays.equals(array, parse("name[3c]")));
		assertTrue(Arrays.equals(array, parse("name[3c ]")));
	}

	@Test
	public void testMultiple() {
		FlatFileColumnDescriptor n3l = new FlatFileColumnDescriptor("name", 3, Alignment.LEFT, ' ');
		FlatFileColumnDescriptor a3r = new FlatFileColumnDescriptor("age",  3, Alignment.RIGHT, '0');
		FlatFileColumnDescriptor[] array = new FlatFileColumnDescriptor[] { n3l, a3r };
		assertTrue(Arrays.equals(array, parse("name[3l],age[3r0]")));
	}

	@Test
	public void testParsePadChar() {
		FlatFileColumnDescriptor d3l = new FlatFileColumnDescriptor("name", 3, Alignment.LEFT, '_');
		FlatFileColumnDescriptor[] d3l_a = new FlatFileColumnDescriptor[] { d3l };
		assertTrue(Arrays.equals(d3l_a, parse("name[3l_]")));
	}

	private FlatFileColumnDescriptor[] parse(String pattern) {
		return FlatFileUtil.parseProperties(pattern);
	}
	
}
