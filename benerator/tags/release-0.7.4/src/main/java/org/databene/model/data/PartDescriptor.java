/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.script.Expression;

/**
 * Descriptor for attributes<br/><br/>
 * Created: 30.06.2007 07:29:43
 * @since 0.2
 * @author Volker Bergmann
 */
public class PartDescriptor extends ComponentDescriptor {

    public PartDescriptor(String name, DescriptorProvider owner) {
        this(name, owner, (TypeDescriptor) null);
    }
    
    public PartDescriptor(String name, DescriptorProvider owner, String type) {
        this(name, owner, type, null, null, null);
    }
    
    public PartDescriptor(String name, DescriptorProvider owner, TypeDescriptor localType) {
        this(name, owner, localType, null, null);
    }
    
    public PartDescriptor(String name, DescriptorProvider owner, TypeDescriptor localType, Expression<Long> minCount, Expression<Long> maxCount) {
    	this(name, owner, null, localType, minCount, maxCount);
    }
    	
    public PartDescriptor(String name, DescriptorProvider owner, String type, TypeDescriptor localType, Expression<Long> minCount, Expression<Long> maxCount) {
        super(name, owner, type, localType);
        setMinCount(minCount);
        setMaxCount(maxCount);
    }
    
}
