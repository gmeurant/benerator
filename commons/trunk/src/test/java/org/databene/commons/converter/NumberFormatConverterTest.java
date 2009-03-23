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

import junit.framework.TestCase;

/**
 * Tests the {@link NumberFormatConverter}.<br/>
 * <br/>
 * Created at 22.03.2009 08:02:42
 * @since 0.4.9
 * @author Volker Bergmann
 */

public class NumberFormatConverterTest extends TestCase {

	public void testConvert() {
		NumberFormatConverter converter = new NumberFormatConverter();
		assertEquals("", converter.convert(null));
		assertEquals("0", converter.convert(0.));
		assertEquals("0", converter.convert(0));
		converter.setPattern("0.00");
		assertEquals("0.00", converter.convert(0.));
		converter.setDecimalSeparator(',');
		assertEquals("0,00", converter.convert(0.));
		converter.setPattern("#,##0");
		assertEquals("1.000", converter.convert(1000.));
		converter.setPattern("#,##0.00");
		assertEquals("1.000,00", converter.convert(1000.));
	}

	public void testRevert() {
		NumberFormatConverter converter = new NumberFormatConverter();
		assertEquals(null, converter.revert(""));
		assertEquals(0., converter.revert("0.0").doubleValue());
		assertEquals(0, converter.revert("0").intValue());
		converter.setPattern("0.00");
		assertEquals(0., converter.revert("0.00").doubleValue());
		converter.setDecimalSeparator(',');
		assertEquals(0., converter.revert("0,00").doubleValue());
		converter.setPattern("#,##0");
		assertEquals(1000, converter.revert("1.000").intValue());
		converter.setPattern("#,##0.00");
		assertEquals(1000., converter.revert("1.000,00").doubleValue());
	}
}
