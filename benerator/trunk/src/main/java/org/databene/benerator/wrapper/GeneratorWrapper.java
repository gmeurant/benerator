/*
 * (c) Copyright 2006-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.wrapper;

import org.databene.benerator.Generator;
import org.databene.benerator.InvalidGeneratorSetupException;

/**
 * Abstract generator class that wraps another generator object (in a <i>source</i> property)
 * and delegates life cycle control to it.<br/>
 * <br/>
 * Created: 12.12.2006 19:13:55
 * @author Volker Bergmann
 */
public abstract class GeneratorWrapper<S, P> implements Generator<P> {

    protected Generator<S> source;
    protected boolean dirty;

    public GeneratorWrapper(Generator<S> source) {
        this.source = source;
        this.dirty = true;
    }

    // config properties -----------------------------------------------------------------------------------------------

    /** Returns the source generator */
    public Generator<S> getSource() {
        return source;
    }

    /** Sets the source generator */
    public void setSource(Generator<S> source) {
        this.source = source;
        dirty = true;
    }

    // Generator interface implementation ------------------------------------------------------------------------------

    public void validate() {
        if (dirty) {
            if (source == null)
                throw new InvalidGeneratorSetupException("source", "is null");
            source.validate();
            dirty = false;
        }
    }

    public void reset() {
        if (dirty)
            validate();
        source.reset();
    }

    public void close() {
        if (dirty)
            validate();
        source.close();
    }

    public boolean available() {
        if (dirty)
            validate();
        return source.available();
    }
    
    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + source + ']';
    }
}
