/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link BooleanConverter}.<br/><br/>
 * Created: 17.12.2009 15:45:11
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class BooleanConverterTest {
	
	@Test
	public void testToBoolean() {
		BooleanConverter<Boolean> converter = new BooleanConverter<Boolean>(Boolean.class);
		assertEquals(true, converter.convert(true).booleanValue());
		assertEquals(false, converter.convert(false).booleanValue());
	}

	@Test
	public void testToInt() {
		BooleanConverter<Integer> converter = new BooleanConverter<Integer>(Integer.class);
		assertEquals(1, converter.convert(true).intValue());
		assertEquals(0, converter.convert(false).intValue());
	}

	@Test
	public void testToString() {
		BooleanConverter<String> converter = new BooleanConverter<String>(String.class);
		assertEquals("true",  converter.convert(true));
		assertEquals("false", converter.convert(false));
	}

}
