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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.exporter.config.CellBorder;
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

		logger.info(AbstractTableExcelExporter.class.getName() + " createHeader() start");

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

			if (headerInfo.getIsAutoWidth()) {
				sheet.autoSizeColumn(cellIndex);
			} else {
				sheet.setColumnWidth(cellIndex, headerInfo.getWidth());
			}
			
			if (headerInfo.getRow() != null) {
				rowHead.setHeight(headerInfo.getRow().getHeight());
			}
			cellIndex++;
		}

		logger.info(AbstractTableExcelExporter.class.getName() + " createHeader() end");
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
			font.setBold(cellInfo.getFont().getIsBold());
			font.setColor(cellInfo.getFont().getColor());
			font.setFontName(cellInfo.getFont().getName());
			font.setFontHeightInPoints(cellInfo.getFont().getSize());
		} else {
			font = this.defaultFont();
		}

		cellStyle.setFont(font);
		cellStyle.setAlignment(cellInfo.getHorizontalAlignment());
		cellStyle.setVerticalAlignment(cellInfo.getVerticalAlignment());

		CellBorder[] cellBorders = cellInfo.getBorders();
		cellStyle.setBorderTop(cellBorders[0].getBorderStyle());
		cellStyle.setTopBorderColor(cellBorders[0].getBorderColor());

		cellStyle.setBorderLeft(cellBorders[1].getBorderStyle());
		cellStyle.setLeftBorderColor(cellBorders[1].getBorderColor());

		cellStyle.setBorderBottom(cellBorders[2].getBorderStyle());
		cellStyle.setBottomBorderColor(cellBorders[2].getBorderColor());

		cellStyle.setBorderRight(cellBorders[3].getBorderStyle());
		cellStyle.setRightBorderColor(cellBorders[3].getBorderColor());

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
	protected void createBody(Sheet sheet, List<ColumnInfo> columnInfos, List<?> data)
			throws IllegalArgumentException, IllegalAccessException {
		logger.info(AbstractTableExcelExporter.class.getName() + " createBody() start");
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
				if (columnInfo.getIsAutoWidth()) {
					sheet.autoSizeColumn(cellInd);
				} else {
					sheet.setColumnWidth(cellInd, columnInfo.getWidth());
				}
				cellInd++;
			}
		}

		logger.info(AbstractTableExcelExporter.class.getName() + " createBody() end");
	}

	public Workbook executeExport() {
		logger.info(AbstractTableExcelExporter.class.getName() + " executeExport() start");
		List<SheetInfoSetting> sheetInfoSettings = this.getSheetsSetting();

		List<List<?>> data = this.getData();
		int ind = 0;
		List<Integer> sheetsRemove = new ArrayList<>();

		for (SheetInfoSetting sheetInfoSetting : sheetInfoSettings) {
			this.initialRowIndex = 0;

			SheetInfo sheetInfo = sheetInfoSetting.getSheetInfo();

			Sheet sheet = this.workbook.createSheet(sheetInfo.getName());
			sheet.setDisplayGridlines(sheetInfo.getIsDisplayGrid());
			sheet.setFitToPage(sheetInfo.getIsFitToPage());

			try {
				List<?> listRows = data.get(ind);
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

		logger.info(AbstractTableExcelExporter.class.getName() + " executeExport() end");
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
		return excelType.equals(ExcelType.XLSX) ? new XSSFWorkbook()
				: excelType.equals(ExcelType.SXSSF) ? new SXSSFWorkbook() : new HSSFWorkbook();
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
