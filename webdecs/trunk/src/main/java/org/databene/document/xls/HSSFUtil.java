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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;

/**
 * Provides utility methods for HSSF (POI).<br/>
 * <br/>
 * Created at 09.08.2009 07:47:52
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class HSSFUtil {

	private HSSFUtil() { }
	
	public static Object resolveCellValue(Cell cell) {
		return resolveCellValue(cell, null);
	}
	
	public static Object resolveCellValue(Cell cell, Converter<String, ?> stringResolver) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING: return convertString(cell, stringResolver);
			case Cell.CELL_TYPE_NUMERIC: if (HSSFDateUtil.isCellDateFormatted(cell))
				return cell.getDateCellValue();
			else
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_BOOLEAN: return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_BLANK: 
			case Cell.CELL_TYPE_ERROR: return cell.getRichStringCellValue().getString();
			case Cell.CELL_TYPE_FORMULA: 
				FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(cell);
				switch (cellValue.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING: return convertString(cellValue, stringResolver);
				    case HSSFCell.CELL_TYPE_NUMERIC: return cellValue.getNumberValue();
				    case Cell.CELL_TYPE_BOOLEAN: return cellValue.getBooleanValue();
				    case HSSFCell.CELL_TYPE_BLANK:
				    case HSSFCell.CELL_TYPE_ERROR: return null;
				    default: throw new IllegalStateException("Unexpected cell type: " + cellValue.getCellType());
				    	// CELL_TYPE_FORMULA is not supposed to be encountered here
				}	
			default: throw new ConfigurationError("Not a supported cell type: " + cell.getCellType());
		}
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object convertString(CellValue cellValue, Converter<?, ?> stringResolver) {
    	String content = cellValue.getStringValue();
    	return (stringResolver != null ? ((Converter) stringResolver).convert(content) : content);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object convertString(Cell cell, Converter<?, ?> stringResolver) {
    	String content = cell.getRichStringCellValue().getString();
    	return (stringResolver != null ? ((Converter) stringResolver).convert(content) : content);
    }

}
