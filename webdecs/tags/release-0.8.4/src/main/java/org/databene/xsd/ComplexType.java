/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.xsd;

import java.util.Collection;
import java.util.Map;

import org.databene.commons.collection.OrderedNameMap;

/**
 * Parent class for schema elements that represent complex types.<br/><br/>
 * Created: 16.05.2014 19:21:44
 * @since 0.8.2
 * @author Volker Bergmann
 */

public abstract class ComplexType extends NamedSchemaElement {
	
	protected Map<String, Attribute> attributes;
	
	public ComplexType(String name) {
		super(name);
		this.attributes = new OrderedNameMap<Attribute>();
	}
	
	public void addAttribute(Attribute attribute) {
		this.attributes.put(attribute.getName(), attribute);
	}
	
	public Collection<Attribute> getAttributes() {
		return attributes.values();
	}
	
	public abstract void printContent(String string);
	
}
