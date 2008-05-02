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

package org.databene.commons;

import java.util.Date;

import org.databene.commons.collection.MapEntry;

import junit.framework.TestCase;

/**
 * TODO documentation.<br/><br/>
 * Created at 02.05.2008 12:08:43
 * @since 0.4.3
 * @author Volker Bergmann
 */
public class CompositeFormatterTest extends TestCase {
	
	private CompositeFormatter formatter = new CompositeFormatter();
	
	public void testRenderNullComponent() {
		checkRendering("name=[null]", "name", null);
	}

	public void testRenderDateComponent() {
		checkRendering("date=1970-01-01", "date", TimeUtil.date(1970, 0, 1));
	}

	public void testRenderTimeComponent() {
		checkRendering("time=1970-01-01T01:02:03", "time", TimeUtil.date(1970, 0, 1, 1, 2, 3, 0));
	}

	public void testRenderArray() {
		checkRendering("array=[1, 2, 3]", "array", ArrayUtil.toArray(1, 2, 3));
	}

	private void checkRendering(String expected, String name, Object value) {
		StringBuilder builder = new StringBuilder();
		formatter.renderComponent(builder, "", new MapEntry(name, value));
		assertEquals(expected, builder.toString());
	}
}
