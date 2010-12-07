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

package org.databene.webdecs.xml;

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.CollectionUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 * TODO Document class.<br/><br/>
 * Created: 05.12.2010 12:15:57
 * @since TODO version
 * @author Volker Bergmann
 */
public class XMLElementParserFactory {
	
	protected List<XMLElementParser> parsers;

	public XMLElementParserFactory() {
		this.parsers = new ArrayList<XMLElementParser>();
	}
	
	public void addParser(XMLElementParser parser) {
		this.parsers.add(parser);
	}

	public XMLElementParser getParser(Element element, List<Object> parentPath) {
		for (XMLElementParser parser : parsers)
			if (parser.supports(element, parentPath))
				return parser;
		throw new ConfigurationError("Element not supported in the context of a " + 
			CollectionUtil.lastElement(parentPath).getClass().getSimpleName() + ": " + 
			XMLUtil.format(element));
	}
	
}
