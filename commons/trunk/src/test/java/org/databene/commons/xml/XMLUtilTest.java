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

package org.databene.commons.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

/**
 * Tests the XMLUtil class.<br/><br/>
 * Created: 19.03.2008 09:11:08
 * @author Volker Bergmann
 */
public class XMLUtilTest extends TestCase {
    
    private static final String XML_TEXT = "<?xml version=\"1.0\"?><root att=\"1\"/>";

    public void testFormat() {
        String output = XMLUtil.format(createElementWithChildren("ns:test"));
        assertTrue(output.startsWith("<ns:test"));
    }
 
    public void testLocalNameString() {
        assertEquals("test", XMLUtil.localName("ns:test"));
    }

    public void testLocalNameElement() {
        Element element = createElementWithChildren("ns:test");
        assertEquals("test", XMLUtil.localName(element));
    }

    public void testGetChildElements() {
        Element child1 = createElementWithChildren("c1");
        Element child2 = createElementWithChildren("c2");
        Element parent = createElementWithChildren("p", child1, child2);
        Element[] expectedChildren = ArrayUtil.toArray(child1, child2);
        Element[] actualChildren = XMLUtil.getChildElements(parent);
        assertTrue(ArrayUtil.equals(expectedChildren, actualChildren));
    }
    
    public void testGetChildElementsByName() {
        Element child1 = createElementWithChildren("c1");
        Element child2 = createElementWithChildren("c2");
        Element parent = createElementWithChildren("p", child1, child2);
        Element[] expectedChildren = ArrayUtil.toArray(child2);
        Element[] actualChildren = XMLUtil.getChildElements(parent, true, "c2");
        assertTrue(ArrayUtil.equals(expectedChildren, actualChildren));
    }

    public void testGetIntegerAttribute() {
        Element element = createElement("test", CollectionUtil.buildMap("value", "1"));
        assertEquals(1, (int) XMLUtil.getIntegerAttribute(element, "value", 2));
    }

    public void testGetLongAttribute() {
        Element element = createElement("test", CollectionUtil.buildMap("value", "1"));
        assertEquals(1, (long) XMLUtil.getIntegerAttribute(element, "value", 2));
    }

    public void testGetAttributes() {
        Element element = createElementWithAttributes("test", "name1", "value1", "name2", "");
        Map<String, String> actualAttributes = XMLUtil.getAttributes(element);
        Map<String, String> expectedAttributes = CollectionUtil.buildMap("name1", "value1", "name2", "");
        assertEquals(expectedAttributes, actualAttributes);
    }

    public void testNormalizedAttributeValue() {
        Element element = createElementWithAttributes("test", "name1", "value1", "name2", "");
        assertEquals("value1", XMLUtil.normalizedAttributeValue(element, "name1"));
        assertEquals(null, XMLUtil.normalizedAttributeValue(element, "name2"));
    }

    public void testParseUri() throws IOException {
        File file = File.createTempFile("XMLUtilTest", ".xml");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(XML_TEXT);
            writer.close();
            Document document = XMLUtil.parse(file.getAbsolutePath());
            checkXML(document);
        } finally {
            file.delete();
        }
    }

    public void testParseStream() throws IOException {
        StringBufferInputStream stream = new StringBufferInputStream(XML_TEXT); 
        checkXML(XMLUtil.parse(stream));
    }

    public void testParseString() throws IOException {
        Document document = XMLUtil.parseString(XML_TEXT);
        checkXML(document);
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private void checkXML(Document document) {
        Element root = document.getDocumentElement();
        assertEquals("root", root.getNodeName());
        assertEquals("1", root.getAttribute("att"));
        assertEquals(1, root.getAttributes().getLength());
    }
    
    private Element createElementWithAttributes(String name, String... attKeysAndValues) {
        Map attMap = CollectionUtil.buildMap(attKeysAndValues);
        return createElement(name, attMap);
    }
    
    private Element createElementWithChildren(String name, Element... children) {
        return createElement(name, new HashMap<String, String>(), children);
    }
    
    private Element createElement(String name, Map<String, String> attributes, Element... children) {
        Element element = createMock(Element.class);
        expect(element.getNodeName()).andReturn(name);

        // set up attributes
        NamedNodeMap attributeNodeMap = createMock(NamedNodeMap.class);
        expect(attributeNodeMap.getLength()).andReturn(attributes.size());
        int i = 0; 
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            Attr attr = createMock(Attr.class);
            expect(attr.getName()).andReturn(entry.getKey());
            expect(attr.getValue()).andReturn(entry.getValue());
            expect(attributeNodeMap.item(i)).andReturn(attr);
            expect(element.getAttribute(entry.getKey())).andReturn(entry.getValue());
            replay(attr);
            i++;
        }
        replay(attributeNodeMap);
        expect(element.getAttributes()).andReturn(attributeNodeMap);
        
        // set up child nodes
        NodeList childNodeList = createMock(NodeList.class);
        expect(childNodeList.getLength()).andReturn(children.length);
        for (i = 0; i < children.length; i++)
            expect(childNodeList.item(i)).andReturn(children[i]);
        replay(childNodeList);
        expect(element.getChildNodes()).andReturn(childNodeList);
        replay(element);
        return element;
    }

    private Element createTextElement(String text) {
        Element element = createMock(Element.class);
        expect(element.getNodeValue()).andReturn(text);
        replay(element);
        return element;
    }

}
