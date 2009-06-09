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

package org.databene.benclipse.internal.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;

/**
 * XML content describer.<br/>
 * <br/>
 * Created at 10.02.2009 20:11:25
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class XMLContentDescriber extends TextContentDescriber {
	
	private static final QualifiedName[] SUPPORTED_OPTIONS = new QualifiedName[] { IContentDescription.CHARSET,
	        IContentDescription.BYTE_ORDER_MARK };
	private static final String ENCODING = "encoding=";
	private static final String XML_PREFIX = "<?xml ";

	@Override
    public int describe(InputStream input, IContentDescription description) throws IOException {
		byte[] bom = getByteOrderMark(input);
		String xmlDeclEncoding = "UTF-8"; //$NON-NLS-1$
		input.reset();
		if (bom != null) {
			if (bom == IContentDescription.BOM_UTF_16BE)
				xmlDeclEncoding = "UTF-16BE"; //$NON-NLS-1$
			else if (bom == IContentDescription.BOM_UTF_16LE)
				xmlDeclEncoding = "UTF-16LE"; //$NON-NLS-1$
			// skip BOM to make comparison simpler
			input.skip(bom.length);
			// set the BOM in the description if requested
			if (description != null && description.isRequested(IContentDescription.BYTE_ORDER_MARK))
				description.setProperty(IContentDescription.BYTE_ORDER_MARK, bom);
		}
		byte[] xmlPrefixBytes = XML_PREFIX.getBytes(xmlDeclEncoding);
		byte[] prefix = new byte[xmlPrefixBytes.length];
		if (input.read(prefix) < prefix.length)
			// there is not enough info to say anything
			return INDETERMINATE;
		for (int i = 0; i < prefix.length; i++)
			if (prefix[i] != xmlPrefixBytes[i])
				// we don't have a XMLDecl... there is not enough info to say
				// anything
				return INDETERMINATE;
		if (description == null)
			return VALID;
		// describe charset if requested
		if (description.isRequested(IContentDescription.CHARSET)) {
			String fullXMLDecl = readFullXMLDecl(input, xmlDeclEncoding);
			if (fullXMLDecl != null) {
				String charset = getCharset(fullXMLDecl);
				if (charset != null && !"UTF-8".equalsIgnoreCase(charset)) //$NON-NLS-1$
					// only set property if value is not default (avoid using a
					// non-default content description)
					description.setProperty(IContentDescription.CHARSET, getCharset(fullXMLDecl));
			}
		}
		return VALID;
	}

	@Override
    public int describe(Reader input, IContentDescription description) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		String line = reader.readLine();
		// end of stream
		if (line == null)
			return INDETERMINATE;
		// XMLDecl should be the first string (no blanks allowed)
		if (!line.startsWith(XML_PREFIX))
			return INDETERMINATE;
		if (description == null)
			return VALID;
		// describe charset if requested
		if ((description.isRequested(IContentDescription.CHARSET)))
			description.setProperty(IContentDescription.CHARSET, getCharset(line));
		return VALID;
	}
	
	// private helper methods ------------------------------------------------------------------------------------------
	
	private String readFullXMLDecl(InputStream input, String unicodeEncoding) throws IOException {
		byte[] xmlDecl = new byte[100];
		int c = 0;
		// looks for XMLDecl ending char (?)
		int read = 0;
		while (read < xmlDecl.length && (c = input.read()) != -1 && c != '?')
			xmlDecl[read++] = (byte) c;
		return c == '?' ? new String(xmlDecl, 0, read, unicodeEncoding) : null;
	}

	private String getCharset(String firstLine) {
		int encodingPos = firstLine.indexOf(ENCODING);
		if (encodingPos == -1)
			return null;
		char quoteChar = '"';
		int firstQuote = firstLine.indexOf(quoteChar, encodingPos);
		if (firstQuote == -1) {
			quoteChar = '\'';
			firstQuote = firstLine.indexOf(quoteChar, encodingPos);
		}
		if (firstQuote == -1 || firstLine.length() == firstQuote - 1)
			return null;
		int secondQuote = firstLine.indexOf(quoteChar, firstQuote + 1);
		if (secondQuote == -1)
			return null;
		return firstLine.substring(firstQuote + 1, secondQuote);
	}

	@Override
    public QualifiedName[] getSupportedOptions() {
		return SUPPORTED_OPTIONS;
	}
}
