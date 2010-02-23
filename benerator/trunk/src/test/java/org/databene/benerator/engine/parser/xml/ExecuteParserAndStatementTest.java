/*
 * (c) Copyright 2009-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.engine.parser.xml;

import static org.junit.Assert.assertEquals;

import org.databene.benerator.engine.BeneratorContext;
import org.databene.benerator.engine.ResourceManagerSupport;
import org.databene.benerator.engine.statement.EvaluateStatement;
import org.databene.commons.xml.XMLUtil;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * Tests the {@link EvaluateParser} with respect to the features used 
 * in the &lt;execute&gt; element.<br/><br/>
 * Created: 30.10.2009 08:11:56
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class ExecuteParserAndStatementTest {

	@Test
	public void testBeanInvocation() throws Exception {
        Element element = XMLUtil.parseStringAsElement("<execute>bean.invoke(2)</execute>");
		EvaluateStatement statement = new EvaluateParser().parse(element, new ResourceManagerSupport());
		BeneratorContext context = new BeneratorContext();
		BeanMock bean = new BeanMock();
		context.set("bean", bean);
		statement.execute(context);
		assertEquals(1, bean.invocationCount);
		assertEquals(2, bean.lastValue);
	}

	@Test
	public void testSimpleTypeVariableDefinition() throws Exception {
		BeneratorContext context = new BeneratorContext();
        Element element = XMLUtil.parseStringAsElement("<execute>x = 3</execute>");
		EvaluateStatement statement = new EvaluateParser().parse(element, new ResourceManagerSupport());
		statement.execute(context);
		assertEquals(3, context.get("x"));
	}
	
	@Test
	public void testSimpleTypeVariableAccess() throws Exception {
		BeneratorContext context = new BeneratorContext();
		context.set("x", 3);
        Element element = XMLUtil.parseStringAsElement("<execute>x = x + 2</execute>");
		EvaluateStatement statement = new EvaluateParser().parse(element, new ResourceManagerSupport());
		statement.execute(context);
		assertEquals(5, context.get("x"));
	}
	
}
