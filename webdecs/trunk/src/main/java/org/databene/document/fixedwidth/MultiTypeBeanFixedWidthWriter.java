/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.document.fixedwidth;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.databene.commons.Assert;
import org.databene.commons.SystemInfo;
import org.databene.commons.accessor.GraphAccessor;

/**
 * Writes JavaBean properties (graphs) to a file with fixed-width columns.<br/><br/>
 * Created: 14.03.2014 16:09:37
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class MultiTypeBeanFixedWidthWriter implements Closeable {
	
	private final Writer out;
	private Map<String, FixedWidthColumnDescriptor[]> rowFormats;
	
	public MultiTypeBeanFixedWidthWriter(Writer out) {
		this(out, null);
	}
	
	public MultiTypeBeanFixedWidthWriter(Writer out, Map<String, FixedWidthColumnDescriptor[]> rowFormats) {
		Assert.notNull(out, "Writer");
		this.out = out;
		this.rowFormats = new HashMap<String, FixedWidthColumnDescriptor[]>();
		if (rowFormats != null)
			for (Map.Entry<String, FixedWidthColumnDescriptor[]> entry : rowFormats.entrySet())
				addRowFormat(entry.getKey(), entry.getValue());
	}
	
	public void addRowFormat(String simpleClassName, FixedWidthColumnDescriptor[] cellFormats) {
		this.rowFormats.put(simpleClassName, cellFormats);
	}
	
	public FixedWidthColumnDescriptor[] getRowFormat(String simpleClassName) {
		return this.rowFormats.get(simpleClassName);
	}
	
	public void write(Object bean) throws IOException {
		// Check preconditions
		Assert.notNull(bean, "bean");
		FixedWidthColumnDescriptor[] cellFormats = rowFormats.get(bean.getClass().getSimpleName());
		if (cellFormats == null)
			throw new IllegalArgumentException("Bean class not configured: " + bean.getClass().getSimpleName());
		// format array
		for (int i = 0; i < cellFormats.length; i++) {
			String path = cellFormats[i].getName();
			Object value = GraphAccessor.getValue(path, bean);
			out.write(cellFormats[i].format(value));
		}
		out.write(SystemInfo.getLineSeparator());
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}
	
}
