/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.document.xls;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.databene.commons.ArrayBuilder;
import org.databene.commons.Converter;
import org.databene.commons.HeavyweightIterator;
import org.databene.commons.IOUtil;
import org.databene.commons.converter.NoOpConverter;

/**
 * Iterates the lines of a sheet in an Excel document.<br/>
 * <br/>
 * Created at 27.01.2009 22:04:47
 * @since 0.4.8
 * @author Volker Bergmann
 */

public class XLSLineIterator implements HeavyweightIterator<Object[]> {
	
	private Iterator<Row> rowIterator;
	private String[] headers;
	private Converter<String, ?> preprocessor;
	
	// constructors ----------------------------------------------------------------------------------------------------
	
	public XLSLineIterator(String uri) throws IOException {
		this(uri, 0);
	}
	
	public XLSLineIterator(String uri, int sheetIndex) throws IOException {
		this(uri, sheetIndex, null);
	}
	
    public XLSLineIterator(String uri, int sheetIndex, Converter<String, ?> preprocessor) throws IOException {
		this(sheet(uri, sheetIndex), preprocessor);
	}
	
	@SuppressWarnings("unchecked")
    public XLSLineIterator(HSSFSheet sheet, Converter<String, ?> preprocessor) {
		if (preprocessor == null)
			preprocessor = new NoOpConverter();
		this.preprocessor = preprocessor;
		rowIterator = sheet.rowIterator();
		
		if (!rowIterator.hasNext()) {
			close();
			return;
		}
			
		// read headers
		Row headerRow = rowIterator.next(); // TODO read headers only if indicated by a flag
		ArrayBuilder<String> builder = new ArrayBuilder<String>(String.class);
		for (int cellnum = 0; cellnum <= headerRow.getLastCellNum(); cellnum++) {
			Cell cell = headerRow.getCell(cellnum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				builder.add(cell.getRichStringCellValue().getString());
		}
		headers = builder.toArray();
    }

	// interface -------------------------------------------------------------------------------------------------------
	
	public String[] getHeaders() {
		return headers;
	}

	public void close() {
		rowIterator = null;
		headers = new String[0];
	}

	public boolean hasNext() {
		return (rowIterator != null && rowIterator.hasNext());
	}

	public Object[] next() {
		if (rowIterator == null)
			return null;
		Row row = rowIterator.next();
		Object[] result = new Object[headers.length];
		for (int cellnum = 0; cellnum < headers.length; cellnum++)
			result[cellnum] = HSSFUtil.resolveCellValue(row.getCell(cellnum), preprocessor);
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported");
	}
	
	// helper methods --------------------------------------------------------------------------------------------------
	
    private static HSSFSheet sheet(String uri, int sheetIndex) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(IOUtil.getInputStreamForURI(uri));
		return workbook.getSheetAt(sheetIndex);
    }

}
