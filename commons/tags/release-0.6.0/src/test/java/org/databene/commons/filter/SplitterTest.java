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

package org.databene.commons.filter;

import java.util.List;

import org.databene.commons.CollectionUtil;
import org.databene.commons.Filter;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link Splitter} class.<br/><br/>
 * Created at 04.05.2008 10:08:18
 * @since 0.5.3
 * @author Volker Bergmann
 *
 */
public class SplitterTest {

    @Test
    @SuppressWarnings("unchecked")
	public void test() {
		EvenFilter evenFilter = new EvenFilter();
		List<List<Integer>> groups = Splitter.filter(
				new Integer[] { 1, 2, 3}, 
				new InverseFilter(evenFilter), evenFilter);
		assertEquals(2, groups.size());
		assertEquals(CollectionUtil.toList(1, 3), groups.get(0));
		assertEquals(CollectionUtil.toList(2), groups.get(1));
	}
	
	public class EvenFilter implements Filter<Integer> {
		public boolean accept(Integer i) {
			return ((i % 2) == 0);
		}
	}
	
}