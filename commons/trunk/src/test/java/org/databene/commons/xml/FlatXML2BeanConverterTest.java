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

package org.databene.commons.xml;

import java.io.IOException;

import org.junit.Test;
import static junit.framework.Assert.*;

import org.databene.commons.ConfigurationError;
import org.databene.commons.Person;
import org.w3c.dom.Element;

/**
 * Tests the FlatXML2BeanConverter.<br/><br/>
 * Created: 16.03.2008 13:02:55
 * @author Volker Bergmann
 */
public class FlatXML2BeanConverterTest {
    
	@Test
    public void testNormal() throws IOException {
        // create XML element
        String text = "<person name='Alice' age=\"23\" />";
        Element element = XMLUtil.parseString(text).getDocumentElement();
        // create converter
        FlatXML2BeanConverter converter = new FlatXML2BeanConverter();
        converter.addMapping("person", Person.class);
        // use converter
        Object bean = converter.convert(element);
        // check result
        assertEquals(new Person("Alice", 23), bean);
    }
    
	@Test
    public void testUndefinedType() throws IOException {
        try {
            // create XML element
            String text = "<person name='Alice' age=\"23\" />";
            Element element = XMLUtil.parseString(text).getDocumentElement();
            // create converter
            FlatXML2BeanConverter converter = new FlatXML2BeanConverter();
            // use converter
            Object bean = converter.convert(element);
            // check result
            assertEquals(new Person("Alice", 23), bean);
            fail(ConfigurationError.class.getSimpleName() + " expected");
        } catch (ConfigurationError e) {
            // that's required
        }
    }
    
}
