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

package org.databene.commons.xml;

import java.io.Closeable;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Writes XML to a stream. The interface is similar to {@link Transformer}, 
 * but setup is easier, methods have been simplified and convenience methods 
 * have been added.<br/><br/>
 * Created: 28.11.2010 06:39:04
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class SimpleXMLWriter implements Closeable {

    OutputStream out;
    TransformerHandler handler;

	public SimpleXMLWriter(OutputStream out) {
		this(out, SystemInfo.getFileEncoding());
	}

	public SimpleXMLWriter(OutputStream out, String encoding) {
        try {
			// create file and write header
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			handler = tf.newTransformerHandler();
			Transformer transformer = handler.getTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}" + "indent-amount", "2"); 
			handler.setResult(new StreamResult(out));
			handler.startDocument();
		} catch (TransformerConfigurationException e) {
			throw new ConfigurationError(e);
		} catch (SAXException e) {
			throw new ConfigurationError("Error in initializing XML file", e);
		}
	}

	public void print(String text) throws SAXException {
		characters(text.toCharArray(), 0, text.length());
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		handler.characters(ch, start, length);
	}

	public void comment(char[] ch, int start, int length) throws SAXException {
		handler.comment(ch, start, length);
	}

	public void endCDATA() throws SAXException {
		handler.endCDATA();
	}

	public void endDTD() throws SAXException {
		handler.endDTD();
	}

	public void endDocument() throws SAXException {
		handler.endDocument();
	}

	public void endElement(String name)
			throws SAXException {
		handler.endElement("", "", name);
	}

	public void endEntity(String name) throws SAXException {
		handler.endEntity(name);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		handler.endPrefixMapping(prefix);
	}

	public String getSystemId() {
		return handler.getSystemId();
	}

	public Transformer getTransformer() {
		return handler.getTransformer();
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		handler.ignorableWhitespace(ch, start, length);
	}

	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		handler.notationDecl(name, publicId, systemId);
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		handler.processingInstruction(target, data);
	}

	public void setDocumentLocator(Locator locator) {
		handler.setDocumentLocator(locator);
	}

	public void setResult(Result result) throws IllegalArgumentException {
		handler.setResult(result);
	}

	public void setSystemId(String systemID) {
		handler.setSystemId(systemID);
	}

	public void skippedEntity(String name) throws SAXException {
		handler.skippedEntity(name);
	}

	public void startCDATA() throws SAXException {
		handler.startCDATA();
	}

	public void startDTD(String name, String publicId, String systemId)
			throws SAXException {
		handler.startDTD(name, publicId, systemId);
	}

	public void startDocument() throws SAXException {
		handler.startDocument();
	}

	public void startElement(String name, Attributes atts) throws SAXException {
		handler.startElement("", "", name, atts);
	}

	public void startEntity(String name) throws SAXException {
		handler.startEntity(name);
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		handler.startPrefixMapping(prefix, uri);
	}

	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		handler.unparsedEntityDecl(name, publicId, systemId, notationName);
	}

	public void close() {
		try {
			if (handler != null) {
				try {
					handler.endDocument();
				} catch (SAXException e) {
					throw new RuntimeException("Error in closing XML file", e);
				}
				handler = null;
			}
		} finally {
			IOUtil.close(out);
		}
	}

}