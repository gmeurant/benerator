/*
 * (c) Copyright 2009-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.xls;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.databene.commons.Converter;
import org.databene.commons.IOUtil;
import org.databene.commons.converter.ArrayTypeConverter;
import org.databene.commons.converter.NoOpConverter;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Iterates the lines of a sheet in an Excel document.<br/>
 * <br/>
 * Created at 27.01.2009 22:04:47
 * @since 0.4.8
 * @author Volker Bergmann
 */

public class XLSLineIterator implements DataIterator<Object[]> {
	
	private String emptyMarker;
	private String nullMarker;
	private boolean formatted;
	private Converter<String, ?> stringPreprocessor;
	
	private String[] headers;
	private Iterator<Row> rowIterator;
	
	// constructors ----------------------------------------------------------------------------------------------------
	
	public XLSLineIterator(String uri) throws IOException, InvalidFormatException {
		this(uri, 0);
	}
	
	public XLSLineIterator(String uri, int sheetIndex) throws IOException, InvalidFormatException {
		this(uri, sheetIndex, false, false, null);
	}
	
    public XLSLineIterator(String uri, int sheetIndex, boolean headersIncluded, boolean formatted, Converter<String, ?> stringPreprocessor) 
    		throws IOException, InvalidFormatException {
		this(sheet(uri, sheetIndex), headersIncluded, formatted, stringPreprocessor);
	}
	
	public XLSLineIterator(String uri, String sheetName, boolean headersIncluded, boolean formatted) 
			throws IOException, InvalidFormatException {
		this(uri, sheetName, headersIncluded, formatted, null);
	}
	
	public XLSLineIterator(String uri, String sheetName, boolean headersIncluded, boolean formatted, Converter<String, ?> stringPreprocessor) 
			throws IOException, InvalidFormatException {
		this(sheet(uri, sheetName), headersIncluded, formatted, stringPreprocessor);
	}
	
    public XLSLineIterator(Sheet sheet, boolean headersIncluded, boolean formatted, Converter<String, ?> stringPreprocessor) {
    	this.emptyMarker = "'";
		this.formatted = formatted;
		if (stringPreprocessor == null)
			stringPreprocessor = new NoOpConverter<String>();
		this.stringPreprocessor = stringPreprocessor;
		
		rowIterator = sheet.rowIterator();
		if (!rowIterator.hasNext()) {
			close();
			return;
		} else if (headersIncluded) {
			parseHeaders();
		}
    }


	// properties ------------------------------------------------------------------------------------------------------

	public String getEmptyMarker() {
		return emptyMarker;
	}
	
	public void setEmptyMarker(String emptyMarker) {
		this.emptyMarker = emptyMarker;
	}
	
	public String getNullMarker() {
		return nullMarker;
	}
	
	public void setNullMarker(String nullMarker) {
		this.nullMarker = nullMarker;
	}
	
	public boolean isFormatted() {
		return formatted;
	}
	
	public void setFormatted(boolean formatted) {
		this.formatted = formatted;
	}
	
	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	
	// interface -------------------------------------------------------------------------------------------------------
	
	@Override
	public Class<Object[]> getType() {
		return Object[].class;
	}
	
	@Override
	public synchronized DataContainer<Object[]> next(DataContainer<Object[]> wrapper) {
		if (rowIterator == null || !rowIterator.hasNext())
			return null;
		Row row = rowIterator.next();
		int cellCount = row.getLastCellNum();
		Object[] result = new Object[cellCount];
		for (int cellnum = 0; cellnum < cellCount; cellnum++) {
			if (formatted)
				result[cellnum] = XLSUtil.resolveCellValueAsString(row.getCell(cellnum), emptyMarker, nullMarker, stringPreprocessor);
			else
				result[cellnum] = XLSUtil.resolveCellValue(row.getCell(cellnum), emptyMarker, nullMarker, stringPreprocessor);
		}
		return wrapper.setData(result);
	}

	@Override
	public synchronized void close() {
		rowIterator = null;
	}

	// helper methods --------------------------------------------------------------------------------------------------
	
    private static Sheet sheet(String uri, String sheetName) throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(IOUtil.getInputStreamForURI(uri));
		Sheet sheet = sheetName != null ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
		if (sheet == null)
			throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in file " + uri);
		return sheet;
    }

    private static Sheet sheet(String uri, int sheetIndex) throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(IOUtil.getInputStreamForURI(uri));
		return workbook.getSheetAt(sheetIndex);
    }

    private void parseHeaders() {
    	DataContainer<Object[]> wrapper = new DataContainer<Object[]>();
		if (next(wrapper) != null) {
			this.headers = ArrayTypeConverter.convert(wrapper.getData(), String.class);
		} else {
			this.headers = null;
			close();
		}
	}

    @Override
    public String toString() {
    	return getClass().getSimpleName() + "[" + rowIterator + "]";
    }
    
}
