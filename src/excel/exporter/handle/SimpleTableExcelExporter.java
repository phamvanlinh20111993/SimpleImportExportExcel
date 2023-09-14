package excel.exporter.handle;

import java.util.LinkedList;
import java.util.List;

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
}
