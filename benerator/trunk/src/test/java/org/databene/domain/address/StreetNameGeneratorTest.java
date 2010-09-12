/*
 * (c) Copyright 2006-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.domain.address;

import org.databene.benerator.test.GeneratorClassTest;
import org.databene.dataset.DatasetUtil;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Tests the {@link StreetNameGenerator}.<br/><br/>
 * Created: 12.06.2007 06:46:33
 * @author Volker Bergmann
 */
public class StreetNameGeneratorTest extends GeneratorClassTest {

    public StreetNameGeneratorTest() {
        super(StreetNameGenerator.class);
    }

    @Test
    public void test_DE() {
        StreetNameGenerator generator = new StreetNameGenerator("DE");
        generator.init(context);
        for (int i = 0; i < 10; i++) {
	        String product = generator.generate();
	        assertNotNull(product);
        }
        generator.close();
    }
    
    @Test
    public void test_AU() {
    	DatasetUtil.runInRegion(Country.AUSTRALIA.getIsoCode(), new Runnable() {
			public void run() {
		        StreetNameGenerator generator = new StreetNameGenerator();
		        generator.init(context);
		        for (int i = 0; i < 10; i++) {
	                String product = generator.generate();
	                assertNotNull(product);
                }
		        generator.close();
            }
    	});
    }
    
}