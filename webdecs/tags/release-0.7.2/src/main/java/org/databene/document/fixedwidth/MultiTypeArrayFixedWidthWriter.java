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

/**
 * Writes data arrays to fixed-width files supporting different row types and formats.<br/><br/>
 * Created: 13.03.2014 12:37:54
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class MultiTypeArrayFixedWidthWriter implements Closeable {
	
	private final Writer out;
	private Map<String, FixedWidthColumnDescriptor[]> rowFormats;
	
	public MultiTypeArrayFixedWidthWriter(Writer out) {
		this(out, null);
	}
	
	public MultiTypeArrayFixedWidthWriter(Writer out, Map<String, FixedWidthColumnDescriptor[]> rowFormats) {
		Assert.notNull(out, "Writer");
		this.out = out;
		this.rowFormats = new HashMap<String, FixedWidthColumnDescriptor[]>();
		if (rowFormats != null)
			for (Map.Entry<String, FixedWidthColumnDescriptor[]> entry : rowFormats.entrySet())
				addRowFormat(entry.getKey(), entry.getValue());
	}
	
	public void addRowFormat(String rowType, FixedWidthColumnDescriptor[] cellFormats) {
		this.rowFormats.put(rowType, cellFormats);
	}
	
	public void write(String rowType, Object... values) throws IOException {
		// Check preconditions
		Assert.notNull(values, "array");
		FixedWidthColumnDescriptor[] cellFormats = rowFormats.get(rowType);
		if (cellFormats == null)
			throw new IllegalArgumentException("Illegal row type: " + rowType);
		if (values.length != cellFormats.length)
			throw new IllegalArgumentException("Expected " + cellFormats + " array elements " +
					"for row type '" + rowType + "' but found: " + values.length);
		// format array
		for (int i = 0; i < cellFormats.length; i++)
			out.write(cellFormats[i].format(values[i]));
		out.write(SystemInfo.getLineSeparator());
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}
	
}
