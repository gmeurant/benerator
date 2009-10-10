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

package org.databene.commons.format;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link NullSafeFormat}.<br/><br/>
 * Created at 02.05.2008 16:31:28
 * @since 0.4.3
 * @author Volker Bergmann
 */
public class NullSafeFormatTest {

	private static final String NULL = "[null]";

	@Test
	public void test() throws ParseException {
		NullSafeFormat<?> format = createFormat();
		assertNull(format.parseObject(null));
		assertNull(format.parseObject(NULL));
		assertEquals(1, ((Integer) format.parseObject("1")).intValue());
	}

	private NullSafeFormat<?> createFormat() {
		DecimalFormat nf = new DecimalFormat();
		nf.setGroupingUsed(false);
		NullSafeFormat<?> format = new NullSafeFormat<Integer>(nf, Integer.class, NULL);
		return format;
	}
	
}
