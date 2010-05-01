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

import java.util.Map;
import java.util.Set;

import org.databene.benerator.engine.DescriptorParser;
import org.databene.benerator.engine.Statement;
import org.databene.benerator.engine.statement.GeneratorStatement;
import org.databene.benerator.engine.statement.RunTaskStatement;
import org.databene.benerator.engine.statement.WhileStatement;
import org.databene.commons.Assert;
import org.databene.commons.ConfigurationError;
import org.databene.commons.xml.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Abstract parent class for Descriptor parsers.<br/><br/>
 * Created: 25.10.2009 00:43:18
 * @since 0.6.0
 * @author Volker Bergmann
 */
public abstract class AbstractDescriptorParser implements DescriptorParser {
	
	protected Logger logger = LoggerFactory.getLogger(AbstractDescriptorParser.class);

	private String elementName;
	private String parentName;
	
	public AbstractDescriptorParser(String elementName) {
	    this(elementName, null);
    }

	public AbstractDescriptorParser(String elementName, String parentName) {
		Assert.notNull(elementName, "elementName");
	    this.elementName = elementName;
	    this.parentName = parentName;
    }

	public boolean supports(String elementName, String parentName) {
		return this.elementName.equals(elementName) 
			&& (this.parentName == null || this.parentName.equals(parentName));
	}
	
	public static boolean containsLoop(Statement[] parentPath) {
		for (Statement statement : parentPath)
			if (isLoop(statement))
				return true;
		return false;
	}

	public static boolean isLoop(Statement statement) {
	    return (statement instanceof RunTaskStatement) 
	    	|| (statement instanceof GeneratorStatement)
	    	|| (statement instanceof WhileStatement);
    }
	
	public static boolean containsGeneratorStatement(Statement[] parentPath) {
		for (Statement statement : parentPath)
			if (statement instanceof GeneratorStatement)
				return true;
		return false;
    }

	protected static void checkAttributes(Element element, Set<String> supportedAttributes) {
	    for (Map.Entry<String, String> attribute : XMLUtil.getAttributes(element).entrySet()) {
	        if (!supportedAttributes.contains(attribute.getKey()))
				throw new ConfigurationError("Not a supported import attribute: " + attribute.getKey());
        }
    }

}
