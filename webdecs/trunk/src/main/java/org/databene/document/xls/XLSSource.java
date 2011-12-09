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

package org.databene.document.xls;

import java.io.IOException;

import org.databene.webdecs.DataIterator;
import org.databene.webdecs.DataSource;
import org.databene.webdecs.OrthogonalArrayIterator;

/**
 * Defined XLSSource as abstraction for XLS row or column data sources.<br/><br/>
 * Created: 08.12.2011 16:51:08
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class XLSSource implements DataSource<Object[]> {

	private String uri;
	private boolean rowBased;
	
	public XLSSource(String uri, boolean rowBased) {
		this.uri = uri;
		this.rowBased = rowBased;
	}

	public Class<Object[]> getType() {
		return Object[].class;
	}

	public DataIterator<Object[]> iterator() {
		try {
			DataIterator<Object[]> result = new XLSLineIterator(uri);
			if (!rowBased)
				result = new OrthogonalArrayIterator<Object>(result);
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Error creating iterator for " + uri, e);
		}
	}

	public void close() {
		// nothing to do here
	}

}
