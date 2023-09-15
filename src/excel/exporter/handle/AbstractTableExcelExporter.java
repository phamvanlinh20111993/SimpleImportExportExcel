package excel.exporter.handle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.exporter.config.CellInfo;
import excel.exporter.config.ColumnInfo;
import excel.exporter.config.HeaderInfo;
import excel.exporter.config.SheetInfo;
import excel.exporter.config.SheetInfoSetting;
import excel.exporter.enums.ExcelType;
import excel.exporter.enums.HeaderNameFormatType;
import utils.ObjectUtils;

public abstract class AbstractTableExcelExporter implements TableExcelExporter {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTableExcelExporter.class);

	protected ExcelType excelType = ExcelType.XLSX;

	private Workbook workbook;

	protected String fileName;

	private Integer initialRowIndex = 0;

	public AbstractTableExcelExporter(String fileName) {
		this.fileName = fileName;
		this.workbook = createWorkBook();
	}

	public AbstractTableExcelExporter(String fileName, ExcelType excelType) {
		this.excelType = excelType;
		this.fileName = fileName;
		this.workbook = createWorkBook();
	}

	/**
	 * 
	 * @param sheet
	 * @param headerInfos
	 */
	protected void createHeader(Sheet sheet, List<HeaderInfo> headerInfos) {
		Row rowHead = sheet.createRow(initialRowIndex);
		int cellIndex = 0;

		for (HeaderInfo headerInfo : headerInfos) {
			Cell cell = rowHead.createCell(cellIndex);
			HeaderNameFormatType headerNameFormatType = headerInfo.getName().getType();
			String headerName = headerNameFormatType.changeFormatType(headerInfo.getName().getValue());
			cell.setCellValue(headerName);

			if (ObjectUtils.isEmpty(headerInfo.getCellInfo())) {
				cell.setCellStyle(this.defaultHeaderSetting());
			} else {
				cell.setCellStyle(setCellStyle(headerInfo.getCellInfo()));
			}

			if (headerInfo.isAutoWidth()) {
				sheet.autoSizeColumn(cellIndex);
			} else {
				sheet.setColumnWidth(cellIndex, headerInfo.getWidth());
			}
			if (headerInfo.getRow() != null) {
				rowHead.setHeight(headerInfo.getRow().getHeight());
			}
			cellIndex++;
		}
	}

	/**
	 * 
	 * @param cellInfo
	 * @return
	 */
	protected CellStyle setCellStyle(CellInfo cellInfo) {
		CellStyle cellStyle = this.workbook.createCellStyle();

		Font font = this.getWorkbook().createFont();

		if (!ObjectUtils.isEmpty(cellInfo.getFont())) {
			font.setBold(cellInfo.getFont().isBold());
			font.setColor(cellInfo.getFont().getColor());
			font.setFontName(cellInfo.getFont().getName());
			font.setFontHeightInPoints(cellInfo.getFont().getSize());
		} else {
			font = this.defaultFont();
		}

		cellStyle.setFont(font);
		cellStyle.setAlignment(cellInfo.getHorizontalAlignment());
		cellStyle.setVerticalAlignment(cellInfo.getVerticalAlignment());

		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (!cellInfo.isBackgroundRGBColorEmpty()) {
			XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
			xssfCellStyle.setFillForegroundColor(cellInfo.getBackgroundRGBColor());
			xssfCellStyle.setFillBackgroundColor(cellInfo.getBackgroundRGBColor());
			return xssfCellStyle;
		} else {
			cellStyle.setFillForegroundColor(cellInfo.getBackgroundColor());
			cellStyle.setFillBackgroundColor(cellInfo.getBackgroundColor());
		}

		return cellStyle;
	}

	/**
	 * 
	 * @param sheet
	 * @param columnInfos
	 * @param data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void createBody(Sheet sheet, List<ColumnInfo> columnInfos, List<Object> data)
			throws IllegalArgumentException, IllegalAccessException {

		int rowIndex = ++initialRowIndex;

		for (Object rowData : data) {
			Row row = sheet.createRow(rowIndex++);
			Map<String, Object> mapData = objectToMap(rowData);
			List<Object> listValues = new LinkedList<Object>(mapData.values());

			int cellInd = 0;

			for (ColumnInfo columnInfo : columnInfos) {
				if (columnInfo.getRow() != null) {
					row.setHeight(columnInfo.getRow().getHeight());
				}
				Cell cell = row.createCell(cellInd);
				if (ObjectUtils.isEmpty(columnInfo.getCellInfo())) {
					cell.setCellStyle(this.defaultColumnSetting());
				} else {
					cell.setCellStyle(setCellStyle(columnInfo.getCellInfo()));
				}
				setCellValue(cell, listValues.get(cellInd));
				if (columnInfo.isAutoWidth()) {
					sheet.autoSizeColumn(cellInd);
				} else {
					sheet.setColumnWidth(cellInd, columnInfo.getWidth());
				}
				cellInd++;
			}
		}
	}

	public Workbook executeExport() {

		List<SheetInfoSetting> sheetInfoSettings = this.getSheetsSetting();

		List<List<Object>> data = this.getData();

		this.initialRowIndex = 0;

		int ind = 0;

		List<Integer> sheetsRemove = new ArrayList<>();

		for (SheetInfoSetting sheetInfoSetting : sheetInfoSettings) {

			SheetInfo sheetInfo = sheetInfoSetting.getSheetInfo();

			Sheet sheet = this.workbook.createSheet(sheetInfo.getName());
			sheet.setDisplayGridlines(sheetInfo.isDisplayGrid());
			sheet.setFitToPage(sheetInfo.isFitToPage());

			try {
				List<Object> listRows = data.get(ind);
				if (listRows.isEmpty()) {
					sheetsRemove.add(ind);
					continue;
				}
				createHeader(sheet, sheetInfoSetting.getHeaderInfos());
				createBody(sheet, sheetInfoSetting.getColumnInfos(), listRows);

			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println(
						AbstractTableExcelExporter.class.getName() + " executeExport() error: " + e.getMessage());
				logger.error(AbstractTableExcelExporter.class.getName() + " executeExport() error: {}", e.getMessage());
			}
			ind++;
		}

		removeSheets(sheetsRemove);

		return this.workbook;
	}

	/**
	 * 
	 * @param sheetsRemove
	 */
	protected void removeSheets(List<Integer> sheetsRemove) {
		if (CollectionUtils.isEmpty(sheetsRemove))
			return;
		for (int ind = sheetsRemove.size(); ind >= 0; ind++) {
			this.workbook.removeSheetAt(ind);
		}
	}

	/**
	 * 
	 * @return
	 */
	protected Workbook createWorkBook() {
		return excelType.equals(ExcelType.XLSX) ? new XSSFWorkbook() : new HSSFWorkbook();
	}

	@Override
	public void out(String path) throws IOException {
		this.executeExport();
		String exportPath = path + "\\" + this.fileName + "." + excelType.getTypeValue();
		OutputStream fileOut = new FileOutputStream(exportPath);
		System.out.println("Excel File has been created successfully " + exportPath);
		logger.debug("Excel File has been created successfully {}", exportPath);
		workbook.write(fileOut);
	}

	/**
	 * 
	 * @return
	 */
	public Workbook getWorkbook() {
		return this.workbook;
	}

	/**
	 * 
	 * @param commentStr
	 * @param author
	 * @return
	 */
	public Comment addCellComment(String commentStr, String author) {

		Sheet sheet = this.workbook.createSheet();
		Drawing<?> drawing = sheet.createDrawingPatriarch();
		CreationHelper factory = this.workbook.getCreationHelper();
		ClientAnchor anchor = factory.createClientAnchor();

		Comment comment = drawing.createCellComment(anchor);
		RichTextString richTextStr = factory.createRichTextString(commentStr);

		Font font = this.workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 14);
		font.setBold(true);
		font.setColor(IndexedColors.RED.getIndex());
		richTextStr.applyFont(font);

		comment.setString(richTextStr);
		comment.setAuthor(author);

		return comment;
	}

	protected Font defaultFont() {
		Font font = this.getWorkbook().createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setItalic(false);
		font.setColor(IndexedColors.BLACK.getIndex());

		return font;
	}

	abstract List<SheetInfoSetting> getSheetsSetting();
}
