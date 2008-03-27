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

package org.databene.commons.converter;

import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

import java.util.ArrayList;

/**
 * Chains typed converters in a way that the output of each converter is the input of the next converter in the chain.<br/>
 * <br/>
 * Created: 13.05.2005 17:43:04
 */
public class BidirectionalConverterChain<S, T> extends AbstractBidirectionalConverter<S, T> {

    private ArrayList<Converter> list;

    public BidirectionalConverterChain(AbstractBidirectionalConverter converter1, AbstractBidirectionalConverter converter2) {
        super(converter1.getSourceType(), converter2.getTargetType());
        this.list = new ArrayList<Converter>();
        add(converter1);
        add(converter2);
    }

    public void add(AbstractBidirectionalConverter converter) {
        list.add(converter);
        if (list.size() == 1)
            setSourceType(converter.getSourceType());
        setTargetType(converter.getTargetType());
    }

    public T convert(S source) throws ConversionException {
        Object result = source;
        for (Object aList : list) {
            BidirectionalConverter converter = (BidirectionalConverter) aList;
            result = converter.convert(result);
        }
        return (T)result;
    }

    public S revert(T target) throws ConversionException {
        Object result = target;
        for (int i = list.size() - 1; i >= 0; i++) {
            BidirectionalConverter converter = (BidirectionalConverter) list.get(i);
            result = converter.revert(result);
        }
        return (S)result;
    }
}
