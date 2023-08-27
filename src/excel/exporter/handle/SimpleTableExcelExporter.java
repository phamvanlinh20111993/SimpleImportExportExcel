package excel.exporter.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import excel.exporter.enums.ExcelType;
import excel.exporter.enums.HeaderNameFormatType;

public class SimpleTableExcelExporter<T extends Object> extends AbstractTableExcelExporter {

	private String sheetName;

	private List<T> listData;

	public SimpleTableExcelExporter(String fileName, String sheetName, List<T> listData) {
		super(fileName);
		this.sheetName = sheetName;
		this.listData = listData;
	}

	public SimpleTableExcelExporter(String fileName, String sheetName, List<T> listData, ExcelType excelType,
			HeaderNameFormatType formatHeaderName) {
		super(fileName, excelType, formatHeaderName);
		this.sheetName = sheetName;
		this.listData = listData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<Object>> getData() {
		List<List<Object>> data = new LinkedList<List<Object>>();
		data.add((List<Object>) listData);
		return data;
	}

	@Override
	public List<CellStyle> configHeader() {
		List<CellStyle> cellStyles = new ArrayList<>();
		CellStyle cellStyle = this.getWorkbook().createCellStyle();
		Font font = this.getWorkbook().createFont();

		font.setFontName("Arial");
		font.setBold(true);
		font.setColor(HSSFColorPredefined.DARK_RED.getIndex());
		// font.setFontHeight((short) 16);
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setBorderRight(BorderStyle.MEDIUM);
		cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setBorderLeft(BorderStyle.MEDIUM);
		// cellStyle.setWrapText(true);

		cellStyles.add(cellStyle);
		return cellStyles;
	}

	@Override
	public List<CellStyle> configBody() {
		List<CellStyle> cellStyles = new ArrayList<>();
		CellStyle cellStyle = this.getWorkbook().createCellStyle();
		Font font = this.getWorkbook().createFont();

		font.setFontName("Arial");
		font.setBold(false);
		// font.setColor(HSSFColorPredefined.BLACK.getIndex());
		cellStyle.setFont(font);

		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		// cellStyle.setWrapText(true);

		cellStyles.add(cellStyle);

		return cellStyles;
	}

	@Override
	public Map<String, Sheet> configSheets() {
		Map<String, Sheet> mapSheets = new HashMap<>();
		Sheet sheet = this.createWorkBook().createSheet();
		sheet.setDisplayGridlines(false);
		mapSheets.put(sheetName, sheet);

		return mapSheets;
	}

	@Override
	public Row configHeaderRow() {
		Row row = this.createWorkBook().createSheet().createRow(0);
		row.setHeight((short) 400);
		
		return row;
	}

	@Override
	public Row configBodyRow() {
		return null;
	}

}
