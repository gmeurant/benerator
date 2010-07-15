/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.engine;

import static org.junit.Assert.*;

import java.util.Locale;

import org.databene.commons.SystemInfo;
import org.databene.domain.address.Country;
import org.databene.script.ScriptUtil;
import org.junit.Test;

/**
 * Tests the {@link BeneratorContext}.<br/><br/>
 * Created: 31.03.2010 15:15:07
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class BeneratorContextTest {

	@Test
	public void testDefaults() {
		BeneratorContext context = new BeneratorContext();
		assertEquals(".", context.getContextUri());
		assertEquals(Country.getDefault().getIsoCode(), context.getDefaultDataset());
		assertEquals("fatal", context.getDefaultErrorHandler());
		assertEquals(SystemInfo.getLineSeparator(), context.getDefaultLineSeparator());
		assertEquals(Locale.getDefault(), context.getDefaultLocale());
		assertEquals(1, context.getDefaultPageSize());
		assertEquals("ben", context.getDefaultScript());
		assertEquals("ben", ScriptUtil.getDefaultScriptEngine());
		assertEquals(',', context.getDefaultSeparator());
		assertEquals(null, context.getMaxCount());
	}
	
	@Test
	public void testSysPropAccess() {
		BeneratorContext context = new BeneratorContext();
		assertEquals(System.getProperty("user.name"), context.get("user.name"));
	}
	
	@Test
	public void testGenerationCount() {
		BeneratorContext context = new BeneratorContext();
		assertEquals(0L, context.getTotalGenerationCount());
		context.countGenerations(100);
		context.countGenerations(89);
		assertEquals(189, context.getTotalGenerationCount());
	}
	
}
