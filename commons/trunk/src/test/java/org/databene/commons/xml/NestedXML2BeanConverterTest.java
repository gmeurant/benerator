/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

import junit.framework.TestCase;

import org.databene.commons.ConversionException;
import org.databene.commons.IOUtil;
import org.databene.commons.converter.NoOpConverter;
import org.databene.commons.xml.XMLElement2BeanConverter;
import org.databene.SomeBean;
//import org.databene.SomeBean;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Tests the XMLElement2BeanConverter.<br/>
 * <br/>
 * Created: 19.08.2007 15:19:25
 */
public class NestedXML2BeanConverterTest extends TestCase {

    public void test() throws IOException, ConversionException {
        String xml = "<?xml version=\"1.0\"?><bean class=\"org.databene.SomeBean\">\r\n" +
                "<property name=\"num\" value=\"10\"/>" +
                "<property name=\"text\" value=\"blabla\"/>" +
            "</bean>";
        InputStream stream = new ByteArrayInputStream(xml.getBytes());
        try {
            Element element = XMLUtil.parse(stream).getDocumentElement();
            SomeBean bean = (SomeBean) XMLElement2BeanConverter.convert(element, null, new NoOpConverter<String>());
            assertEquals(10, bean.getNum());
            assertEquals("blabla", bean.getText());
            stream.close();
        } finally {
            IOUtil.close(stream);
        }
    }

}
