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

import org.databene.commons.CollectionUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;

import java.util.ArrayList;

/**
 * Aggregates other (sub) convertes and implements conversion by subsequent invocation of the sub generators,
 * each converting the result of the preceeding converter.<br/>
 * <br/>
 * Created: 13.05.2005 17:43:04
 */
@SuppressWarnings("unchecked")
public class ConverterChain<S, T> implements Converter<S, T> {

	private ArrayList<Converter> converters;

	public ConverterChain(Converter ... converters) {
        this.converters = new ArrayList<Converter>(converters.length);
        add(converters);
    }

	public void add(Converter... converters) {
        CollectionUtil.add(this.converters, converters);
    }

    public T convert(S source) throws ConversionException {
        Object result = source;
        for (Converter converter : converters) {
            result = converter.convert(result);
        }
        return (T)result;
    }

    public Class<T> getTargetType() {
        return (Class<T>) converters.get(converters.size() - 1).getClass();
    }
}
