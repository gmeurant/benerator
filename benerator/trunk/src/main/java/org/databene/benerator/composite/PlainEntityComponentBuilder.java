/*
 * (c) Copyright 2008-2013 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.composite;

import org.databene.benerator.Generator;
import org.databene.commons.Mutator;
import org.databene.commons.UpdateFailedException;
import org.databene.model.data.Entity;

/**
 * Builds a plain (atomic) component that is supposed to have a name.<br/><br/>
 * Created at 09.05.2008 07:20:43
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class PlainEntityComponentBuilder extends AbstractComponentBuilder<Entity> {
	
    public PlainEntityComponentBuilder(String name, Generator<?> source, String scope) {
    	super(source, new Mutator_(name), scope);
	}
    
    public String getName() {
    	return ((Mutator_) mutator).name;
    }

	@Override
	public String toString() {
		return getClass().getSimpleName() + '{' + getName() + ", " + source + '}';
	}
	
	private static class Mutator_ implements Mutator {
		
		String name;
		
		public Mutator_(String name) {
	        this.name = name;
        }

		@Override
		public void setValue(Object target, Object value) throws UpdateFailedException {
			((Entity) target).setComponent(name, value);
        }
	}

}
