/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.converter;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link ToStringConverter}.<br/>
 * <br/>
 * Created at 15.03.2009 20:32:19
 * @since 0.5.8
 * @author Volker Bergmann
 */

public class ToStringConverterTest {

	@Test
	public void testDecimal() {
		ToStringConverter converter = new ToStringConverter();
		assertEquals("", converter.convert(null));
		assertEquals("0.0", converter.convert(0.));
		assertEquals("0", converter.convert(0));
		converter.setDecimalPattern("0.00");
		assertEquals("0.00", converter.convert(0.));
		converter.setDecimalSeparator(',');
		assertEquals("0,00", converter.convert(0.));
		converter.setDecimalPattern("#,##0");
		assertEquals("1.000", converter.convert(1000.));
		converter.setDecimalPattern("#,##0.00");
		assertEquals("1.000,00", converter.convert(1000.));
	}
	
}
