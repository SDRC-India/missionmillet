package org.sdrc.missionmillet.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

/**
 * 
 * @author subrata
 * 
 * @Description This class will lock the excel sheet.
 *
 */
@Component
public class LockWorkbook {
	
	/**
	 * @author subrata
	 * 
	 * @param workbook
	 * 
	 * @Descripotion Locking all the excel sheet available in a workbook
	 */

	public static HSSFWorkbook workbookLock(HSSFWorkbook workbookObj) {
		HSSFWorkbook workbook = workbookObj;
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			lockExcelSheet(workbook.getSheetAt(i), workbook);
		}
		return workbook;
	}
	
	/**@author Subrata
	 * @param sheetAt
	 * @param workbook
	 * @Descripotion Lock excel sheet iterating all the row and cell
	 */

	private static void lockExcelSheet(HSSFSheet sheet, HSSFWorkbook workbook) {
		
		HSSFCellStyle styleForLocking = getStyleForLocking(workbook);

		HSSFRow row = null;
		HSSFCell cell = null;
		
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if(row !=null){
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell.getCellStyle().getLocked() == false)
						cell.setCellStyle(styleForLocking);
				}
			}
		}
	}
	
	/**
	 * @author Subrata
	 * @param sheetAt
	 * 
	 * @Descripotion For excel lock(Only .xlsx format)
	 */
	public static XSSFSheet sheetLock(XSSFSheet sheet) {
		
		// Restrict deleting columns
		sheet.lockDeleteColumns(true);
		// Restrict deleting rows
		sheet.lockDeleteRows(true);
		// Restrict formatting cells
		sheet.lockFormatCells(true);
		// Restrict formatting columns
		sheet.lockFormatColumns(true);
		// Restrict formatting rows
		sheet.lockFormatRows(true);
		// Restrict inserting columns
		sheet.lockInsertColumns(true);
		// Restrict inserting rows
		sheet.lockInsertRows(true);
		// Lock the sheet
		sheet.enableLocking();
		
		return sheet;
	}
	
	/**
	 * @author Subrata
	 * @param workbook
	 * @Descripotion style for unlocking the excel sheet
	 */

	public static HSSFCellStyle getStyleForUnLocking(HSSFWorkbook workbook) {
		
		// Define unlock style for the cells to be unlocked
		HSSFCellStyle styleForUnLocking = workbook.createCellStyle();
		styleForUnLocking.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForUnLocking.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForUnLocking.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForUnLocking.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleForUnLocking.setLocked(false);
		styleForUnLocking.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleForUnLocking.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleForUnLocking.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleForUnLocking.setVerticalAlignment(HSSFCellStyle.ALIGN_FILL);
		
		return styleForUnLocking;
	}

	/**
	 * @author Subrata
	 * @param workbook
	 * @Descripotion style for Locking the excel sheet
	 */
	public static HSSFCellStyle getStyleForLocking(HSSFWorkbook workbook) {
		
		HSSFCellStyle styleForLocking = workbook.createCellStyle();
		styleForLocking.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForLocking.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForLocking.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForLocking.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleForLocking.setLocked(true);
		styleForLocking.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleForLocking.setVerticalAlignment(HSSFCellStyle.ALIGN_FILL);
		
		return styleForLocking;
	}
	
	/**
	 * @author Subrata
	 * @param workbook
	 * @Descripotion style for text left alignment
	 */
	public static HSSFCellStyle getStyleForLeftAlign(HSSFWorkbook workbook) {
		
		HSSFCellStyle styleForLeftAlign = workbook.createCellStyle();
		styleForLeftAlign.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleForLeftAlign.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForLeftAlign.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForLeftAlign.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForLeftAlign.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		return styleForLeftAlign;
	}
	
	/**
	 * Creating a align center style and returning the style.
	 * 
	 * @param workbook
	 * @return styleForCenter
	 * 
	 * @author Subrata
	 */
	public static CellStyle getStyleForCenter(Workbook workbook) {
		
		CellStyle styleForCenter = workbook.createCellStyle();
		styleForCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleForCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleForCenter.setWrapText(true);
		
		return styleForCenter;
	}
	
	/**
	 * Creating a align center style with color and returning the style.
	 * 
	 * @param workbook
	 * @return StyleForColorHeader
	 * 
	 * @author Subrata
	 */
	
	public static CellStyle getStyleForColorHeader(Workbook workbook) {
		
		CellStyle styleForColorHeader = workbook.createCellStyle();
		styleForColorHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForColorHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForColorHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForColorHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleForColorHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleForColorHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleForColorHeader.setVerticalAlignment(HSSFCellStyle.ALIGN_FILL);
		styleForColorHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleForColorHeader.setFont(getFontForHeader(workbook));
		
		return styleForColorHeader;
	}
	
	/**
	 * Creating a align center style with color and returning the style for title.
	 * 
	 * @param workbook
	 * @return StyleForColorHeader
	 * 
	 * @author Subrata
	 */
	public static CellStyle getStyleForTitle(Workbook workbook) {
		
		CellStyle styleForTitle = workbook.createCellStyle();
		styleForTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleForTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleForTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleForTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleForTitle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleForTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleForTitle.setVerticalAlignment(HSSFCellStyle.ALIGN_FILL);
		styleForTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleForTitle.setFont(getFontForTitle(workbook));
		
		return styleForTitle;
	}
	
	/**
	 * Create a new font for title.
	 * 
	 * @param workbook
	 * @return font
	 * 
	 * @author Subrata
	 */
	public static Font getFontForTitle(Workbook workbook) {
		
	    Font font = workbook.createFont();
	    font.setFontHeightInPoints((short) 14);
	    font.setFontName("IMPACT");
	    font.setColor(HSSFColor.BLACK.index);
	    
	    return font;
	}
	
	/**
	 * Create a new font for header.
	 * 
	 * @param workbook
	 * @return font
	 * 
	 * @author Subrata
	 */
	public static Font getFontForHeader(Workbook workbook) {
		
	    Font font = workbook.createFont();
	    font.setFontHeightInPoints((short) 10);
	    font.setBold(true);
	    
	    return font;
	}
}
