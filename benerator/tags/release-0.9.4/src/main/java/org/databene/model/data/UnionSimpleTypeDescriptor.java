/*
 * (c) Copyright 2008-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.model.data;

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.ConfigurationError;
import org.databene.script.PrimitiveType;

/**
 * Describes an XML schema style type. 
 * Instances of this type may have one of the supported type alternatives.<br/><br/>
 * Created: 28.02.2008 22:29:37
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class UnionSimpleTypeDescriptor extends SimpleTypeDescriptor {
    
    private List<SimpleTypeDescriptor> alternatives;

    public UnionSimpleTypeDescriptor(String name, DescriptorProvider provider) {
        super(name, provider, (String) null);
        this.alternatives = new ArrayList<SimpleTypeDescriptor>();
    }
    
    public void addAlternative(SimpleTypeDescriptor alternative) {
        this.alternatives.add(alternative);
    }
    
    public List<SimpleTypeDescriptor> getAlternatives() {
        return alternatives;
    }
    
    @Override
    public PrimitiveType getPrimitiveType() {
        TypeDescriptor firstType = alternatives.get(0);
        if (firstType == null)
            throw new ConfigurationError("Cannot determine primitive type of union: " + getName());
        return ((SimpleTypeDescriptor) firstType).getPrimitiveType();
    }

}
