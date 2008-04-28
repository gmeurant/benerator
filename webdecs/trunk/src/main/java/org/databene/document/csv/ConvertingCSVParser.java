/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

package org.databene.document.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.databene.commons.Converter;
import org.databene.commons.HeavyweightIterator;

/**
 * Parses CSV files and converts the row to the desired target type.<br/><br/>
 * Created at 25.04.2008 18:49:50
 * @since 0.4.2
 * @author Volker Bergmann
 */
public class ConvertingCSVParser<E> implements HeavyweightIterator<E>{
	
	private Converter<String[], E> rowConverter;
	private CSVLineIterator source;
	
	public ConvertingCSVParser(String uri, Converter<String[], E> rowConverter) throws IOException {
		this.source = new CSVLineIterator(uri);
		this.rowConverter = rowConverter;
	}

	public boolean hasNext() {
		return source.hasNext();
	}

	public E next() {
		return rowConverter.convert(source.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported");
	}

	public void close() {
		source.close();
	}
	
	public static <T> List<T> parse(String uri, Converter<String[], T> rowConverter) throws IOException {
		return parse(uri, rowConverter, new ArrayList<T>());
	}
	
	public static <T> List<T> parse(String uri, Converter<String[], T> rowConverter, List<T> list) throws IOException {
		ConvertingCSVParser<T> parser = new ConvertingCSVParser<T>(uri, rowConverter);
		while (parser.hasNext())
			list.add(parser.next());
		return list;
	}
}
