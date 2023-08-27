package excel.exporter.handle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.exporter.enums.ExcelType;
import excel.exporter.enums.HeaderNameFormatType;

public abstract class AbstractTableExcelExporter implements TableExcelExporter {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTableExcelExporter.class);

	protected ExcelType excelType = ExcelType.XLSX;

	protected HeaderNameFormatType formatHeaderName = HeaderNameFormatType.NORMAL;

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

	public AbstractTableExcelExporter(String fileName, ExcelType excelType, HeaderNameFormatType formatHeaderName) {
		this.excelType = excelType;
		this.fileName = fileName;
		this.formatHeaderName = formatHeaderName;
		this.workbook = createWorkBook();
	}

	protected void createHeader(Sheet sheet, CellStyle cellStyle, List<String> headerNames) {
		Row rowHead = sheet.createRow(initialRowIndex);
		rowHead = cloneRowSetting(rowHead, this.configHeaderRow());

		int cellIndex = 0;

		for (String headerName : headerNames) {
			Cell cell = rowHead.createCell(cellIndex++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(headerName);
		}

	}

	protected void createBody(Sheet sheet, CellStyle cellStyle, List<String> headerNames, List<Object> data)
			throws IllegalArgumentException, IllegalAccessException {

		int rowIndex = ++initialRowIndex;

		for (Object rowData : data) {
			Row row = sheet.createRow(rowIndex++);
			row = cloneRowSetting(row, this.configBodyRow());

			Map<String, Object> mapData = objectToMap(rowData);

			int cellInd = 0;
			for (Map.Entry<String, Object> rowIndexData : mapData.entrySet()) {
				Cell cell = row.createCell(cellInd++);
				cell.setCellStyle(cellStyle);
				setCellValue(cell, rowIndexData.getValue());
				sheet.autoSizeColumn(cellInd);
			}
		}
	}

	public Workbook export() {

		Map<String, Sheet> sheets = this.configSheets();

		List<CellStyle> headers = this.configHeader();

		List<CellStyle> bodies = this.configBody();

		List<List<Object>> data = this.getData();

		this.initialRowIndex = 0;

		int ind = 0;

		for (Map.Entry<String, Sheet> entry : sheets.entrySet()) {
			Sheet sheet = this.workbook.createSheet(entry.getKey());
			try {
				cloneSheetSetting(sheet, entry.getValue());
				List<Object> listRows = data.get(ind);
				if (listRows.isEmpty()) {
					continue;
				}

				List<String> headerNames = this.getHeaderName(listRows.get(ind), formatHeaderName);

				createHeader(sheet, headers.get(ind), headerNames);

				createBody(sheet, bodies.get(ind), headerNames, listRows);

			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println(AbstractTableExcelExporter.class.getName() + "  export() error: " + e.getMessage());
				logger.error(AbstractTableExcelExporter.class.getName() + "  export() error: {}", e.getMessage());
			}
			ind++;
		}

		return this.workbook;
	}

	protected Workbook createWorkBook() {
		return excelType.equals(ExcelType.XLSX) ? new XSSFWorkbook() : new HSSFWorkbook();
	}

	public void out(String path) throws IOException {
		this.export();
		String exportPath = path + "\\" + this.fileName + "." + excelType.getTypeValue();
		OutputStream fileOut = new FileOutputStream(exportPath);
		System.out.println("Excel File has been created successfully " + exportPath);
		logger.debug("Excel File has been created successfully {}", exportPath);
		workbook.write(fileOut);
	}

	protected Sheet cloneSheetSetting(Sheet currentSheet, Sheet cloneSheet) {
		
		if(cloneSheet == null) return currentSheet;
		
		currentSheet.setDefaultRowHeight(cloneSheet.getDefaultRowHeight());
		currentSheet.setDisplayGridlines(cloneSheet.isDisplayGridlines());
		currentSheet.setDefaultColumnWidth(cloneSheet.getDefaultColumnWidth());
		currentSheet.setVerticallyCenter(cloneSheet.getVerticallyCenter());
		currentSheet.setHorizontallyCenter(cloneSheet.getHorizontallyCenter());
		// TODO need time to call all method
		// ..

		return currentSheet;
	}

	protected Row cloneRowSetting(Row currentRow, Row cloneRow) {
		
		if(cloneRow == null) return currentRow;
		
		currentRow.setHeight(cloneRow.getHeight());
		currentRow.setHeightInPoints(cloneRow.getHeightInPoints());
		currentRow.setRowStyle(cloneRow.getRowStyle());
		// TODO need time to call all method
		// ..

		return currentRow;
	}

	public Workbook getWorkbook() {
		return this.workbook;
	}
}
