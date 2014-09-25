/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.util;

import java.io.Closeable;

import org.databene.commons.IOUtil;
import org.databene.formats.DataIterator;
import org.databene.formats.DataSource;

/**
 * Provides {@link DataSource}-style access to a Java-SDK-{@link Iterable}.<br/><br/>
 * Created: 24.07.2011 11:07:04
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class DataSourceFromIterable<E> extends AbstractDataSource<E> {
	
	protected Iterable<E> source;

	public DataSourceFromIterable(Iterable<E> source, Class<E> type) {
		super(type);
		this.source = source;
	}

	@Override
	public DataIterator<E> iterator() {
		return new DataIteratorFromJavaIterator<E>(source.iterator(), type);
	}

	@Override
	public void close() {
		if (source instanceof Closeable)
			IOUtil.close((Closeable) source);
		super.close();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + source + "]";
	}
	
}
