/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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
import org.apache.poi.ss.usermodel.Row;
import org.databene.commons.Converter;
import org.databene.commons.IOUtil;
import org.databene.commons.converter.NoOpConverter;
import org.databene.webdecs.DataContainer;
import org.databene.webdecs.DataIterator;

/**
 * Iterates the lines of a sheet in an Excel document.<br/>
 * <br/>
 * Created at 27.01.2009 22:04:47
 * @since 0.4.8
 * @author Volker Bergmann
 */

public class XLSLineIterator implements DataIterator<Object[]> {
	
	private String emptyMarker;
	private Iterator<Row> rowIterator;
	private Converter<String, ?> stringPreprocessor;
	
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
	
    public XLSLineIterator(HSSFSheet sheet, Converter<String, ?> stringPreprocessor) {
    	this.emptyMarker = "'";
		if (stringPreprocessor == null)
			stringPreprocessor = new NoOpConverter<String>();
		this.stringPreprocessor = stringPreprocessor;
		rowIterator = sheet.rowIterator();
		
		if (!rowIterator.hasNext()) {
			close();
			return;
		}
    }

	public String getEmptyMarker() {
		return emptyMarker;
	}
	
	public void setEmptyMarker(String emptyMarker) {
		this.emptyMarker = emptyMarker;
	}
	
	// interface -------------------------------------------------------------------------------------------------------
	
	public Class<Object[]> getType() {
		return Object[].class;
	}
	
	public void close() {
		rowIterator = null;
	}

	public DataContainer<Object[]> next(DataContainer<Object[]> wrapper) {
		if (rowIterator == null || !rowIterator.hasNext())
			return null;
		Row row = rowIterator.next();
		int cellCount = row.getLastCellNum();
		Object[] result = new Object[cellCount];
		for (int cellnum = 0; cellnum < cellCount; cellnum++)
			result[cellnum] = HSSFUtil.resolveCellValue(row.getCell(cellnum), emptyMarker, stringPreprocessor);
		return wrapper.setData(result);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove() is not supported");
	}
	
	// helper methods --------------------------------------------------------------------------------------------------
	
    private static HSSFSheet sheet(String uri, int sheetIndex) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(IOUtil.getInputStreamForURI(uri));
		return workbook.getSheetAt(sheetIndex);
    }

    @Override
    public String toString() {
    	return getClass().getSimpleName() + "[" + rowIterator + "]";
    }
    
}
