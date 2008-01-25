/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

package org.databene.model.mutator;

import org.databene.model.converter.TypedConverter;
import org.databene.model.Mutator;
import org.databene.model.ConversionException;
import org.databene.model.UpdateFailedException;

/**
 * Converts its input by a Converter object and forwards the result to another Mutator.<br/>
 * <br/>
 * Created: 12.05.2005 19:08:30
 */
public class ConvertingMutator<C, VI, VO> extends MutatorWrapper<C, VO> implements Mutator<C, VI> {

    private TypedConverter<VI, VO> converter;

    public ConvertingMutator(Mutator<C, VO> realMutator, TypedConverter<VI, VO> converter) {
        super(realMutator);
        this.converter = converter;
    }

    public void setValue(C target, VI value) throws UpdateFailedException {
        try {
            VO vo = converter.convert(value);
            realMutator.setValue(target, vo);
        } catch (ConversionException e) {
            throw new UpdateFailedException(e);
        }
    }

}
