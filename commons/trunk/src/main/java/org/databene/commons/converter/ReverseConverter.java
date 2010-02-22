/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.commons.converter;

import org.databene.commons.ConversionException;

/**
 * Reverses the conversion directions of another TypedConverter.<br/>
 * <br/>
 * Created: 04.08.2007 19:45:18
 * @author Volker Bergmann
 */
public class ReverseConverter<S, T> extends AbstractConverter<S,T> implements BidirectionalConverter<S, T> {

    private BidirectionalConverter<T, S> realConverter;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public ReverseConverter() {
        this(null);
    }
    
    public ReverseConverter(BidirectionalConverter<T, S> realConverter) {
    	super(realConverter.getTargetType(), realConverter.getSourceType());
        this.realConverter = realConverter;
    }
    
    // BidirectionalConverter interface --------------------------------------------------------------------------------

    public T convert(S source) throws ConversionException {
        return realConverter.revert(source);
    }

    public S revert(T target) throws ConversionException {
        return realConverter.convert(target);
    }
    
    // properties ------------------------------------------------------------------------------------------------------

	public BidirectionalConverter<T, S> getRealConverter() {
		return realConverter;
	}

	public void setRealConverter(BidirectionalConverter<T, S> realConverter) {
		this.realConverter = realConverter;
	}

}
