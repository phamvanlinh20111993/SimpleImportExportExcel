package excel.exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
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

public abstract class AbstractTableExcelExporter implements TableExcelExporter {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTableExcelExporter.class);

	private Workbook workbook;

	protected ExcelType excelType = ExcelType.XLSX;

	protected String fileName;

	private Integer initialRowIndex = 0;

	protected HeaderNameFormatType formatHeaderName = HeaderNameFormatType.NORMAL;

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

	protected void createHeader(Sheet sheet, List<String> headerNames,
			Map<String, Map<String, List<Object>>> headerSetting) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Row rowhead = sheet.createRow(initialRowIndex);
		int cellIndex = 0;

		for (String headerName : headerNames) {
			Cell cell = rowhead.createCell(cellIndex++);
			// rowhead.setRowStyle(style);
			cell.setCellValue(headerName);

			CellStyle cellStyle = cell.getCellStyle();
			cellStyle = cloneCellStyleSetting(cellStyle, defaultHeaderSetting());
			if (headerSetting.containsKey(headerName)) {
				cellStyle = cloneCellStyleSetting(cell.getCellStyle(), headerSetting.get(headerName));
			}
			cell.setCellStyle(cellStyle);
		}

	}

	protected void createBody(Sheet sheet, List<String> headerNames, Map<String, Map<String, List<Object>>> bodySetting,
			List<Object> data) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException,
			SecurityException, InvocationTargetException {

		int rowIndex = ++initialRowIndex;

		for (Object rowData : data) {
			Row row = sheet.createRow(rowIndex++);
			Map<String, Object> mapData = objectToMap(rowData);

			int cellInd = 0;
			for (Map.Entry<String, Object> rowIndexData : mapData.entrySet()) {
				Cell cell = row.createCell(cellInd++);
				setCellValue(cell, rowIndexData.getValue());

				CellStyle cellStyle = cell.getCellStyle();
				cellStyle = cloneCellStyleSetting(cellStyle, defaultHeaderSetting());
				if (bodySetting.containsKey(rowIndexData.getKey())) {
					cellStyle = cloneCellStyleSetting(cellStyle, bodySetting.get(rowIndexData.getKey()));
				}
				cell.setCellStyle(cellStyle);
			}
		}
	}

	public Workbook export() {
		Map<String, Map<String, List<Object>>> sheets = this.configSheets();
		List<Map<String, Map<String, List<Object>>>> headers = this.configHeader();
		List<Map<String, Map<String, List<Object>>>> bodies = this.configHeader();
		List<List<Object>> data = this.getData();

		this.initialRowIndex = 0;

		int ind = 0;
		for (Map.Entry<String, Map<String, List<Object>>> entry : sheets.entrySet()) {
			Sheet sheet = this.workbook.createSheet(entry.getKey());

			try {
				cloneSheetSetting(sheet, entry.getValue());

				List<Object> listRows = data.get(ind);

				if (listRows.isEmpty())
					continue;

				List<String> headerNames = this.getHeaderName(listRows.get(ind), formatHeaderName);

				createHeader(sheet, headerNames, headers.get(ind));

				createBody(sheet, headerNames, bodies.get(ind), listRows);

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException
					| InvocationTargetException e) {
				System.out.println(AbstractTableExcelExporter.class.toString() + " export() error: " + e.getMessage());
				logger.error(AbstractTableExcelExporter.class.toString() + " export() error: {}", e.getMessage());
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

	protected Sheet cloneSheetSetting(Sheet currentSheet, Map<String, List<Object>> settings)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// settingSheet(currentSheet, settings);
		// TODO need time to call all method
		// ..

		return currentSheet;
	}

	protected CellStyle cloneCellStyleSetting(CellStyle cellStyle, Map<String, List<Object>> settings)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// settingCellStyle(cellStyle, settings);
		// TODO need time to call all method
		// ..

		return cellStyle;
	}

	abstract Map<String, List<Object>> defaultHeaderSetting();

	abstract Map<String, List<Object>> defaultBodySetting();
}
