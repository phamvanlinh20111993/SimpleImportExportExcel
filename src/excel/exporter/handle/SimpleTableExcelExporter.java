package excel.exporter.handle;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import excel.exporter.config.SheetInfoSetting;
import excel.exporter.enums.ExcelType;

public class SimpleTableExcelExporter<T extends Object> extends AbstractTableExcelExporter {

	private List<T> listData;

	public SimpleTableExcelExporter(String fileName, List<T> listData) {
		super(fileName);
		this.listData = listData;
	}

	public SimpleTableExcelExporter(String fileName, List<T> listData, ExcelType excelType) {
		super(fileName, excelType);
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
	public CellStyle defaultHeaderSetting() {
		CellStyle cellStyle = this.getWorkbook().createCellStyle();
		Font font = this.defaultFont();
		font.setFontHeightInPoints((short) 11);
		font.setBold(true);

		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	@Override
	public CellStyle defaultColumnSetting() {
		CellStyle cellStyle = this.getWorkbook().createCellStyle();
		Font font = this.defaultFont();
		font.setBold(false);

		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}

	public Font defaultFont() {
		Font font = this.getWorkbook().createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setItalic(false);
		font.setColor(IndexedColors.BLACK.getIndex());

		return font;
	}

	@Override
	List<SheetInfoSetting> getSheetsSetting() {
		List<SheetInfoSetting> infoSettings = new LinkedList<>();
		if (listData.size() > 0) {
			infoSettings.add(toSheetInfo(listData.get(0)));
		}
		return infoSettings;
	}
}
