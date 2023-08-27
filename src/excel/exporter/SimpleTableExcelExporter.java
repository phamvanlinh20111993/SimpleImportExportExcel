package excel.exporter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class SimpleTableExcelExporter<T extends Object> extends AbstractTableExcelExporter {

	private String sheetName;

	private List<T> listData;

	public SimpleTableExcelExporter(String fileName, String sheetName, List<T> listData) {
		super(fileName);
		this.sheetName = sheetName;
		this.listData = listData;
	}
	
	public SimpleTableExcelExporter(String fileName, String sheetName, List<T> listData,  ExcelType excelType, HeaderNameFormatType formatHeaderName) {
		super(fileName, excelType, formatHeaderName);
		this.sheetName = sheetName;
		this.listData = listData;
	}

	@Override
	public Map<String, Map<String, List<Object>>> configSheets() {
		Map<String, Map<String, List<Object>>> sheetConfig = new HashMap<String, Map<String, List<Object>>>();
		Map<String, List<Object>> setting = new HashMap<>();

		setting.put("Autobreaks", List.of(true));
		setting.put("DefaultColumnWidth", List.of(30));
		setting.put("DisplayGridlines", List.of(false));

		sheetConfig.put(sheetName, setting);
		return sheetConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<Object>> getData() {
		List<List<Object>> data = new LinkedList<List<Object>>();
		data.add((List<Object>) listData);

		return data;
	}

	@Override
	public List<Map<String, Map<String, List<Object>>>> configHeader() {
		List<Map<String, Map<String, List<Object>>>> configs = new LinkedList<>();
		Map<String, Map<String, List<Object>>> config = new HashMap<String, Map<String, List<Object>>>();
		configs.add(config);
		return configs;
	}

	@Override
	public List<Map<String, Map<String, List<Object>>>> configBody() {
		List<Map<String, Map<String, List<Object>>>> configs = new LinkedList<>();

		Map<String, Map<String, List<Object>>> config = new HashMap<String, Map<String, List<Object>>>();
		configs.add(config);

		return configs;
	}

	public Map<String, List<Object>> defaultHeaderSetting() {
		Map<String, List<Object>> setting = new HashMap<>();
		
		setting.put("ShrinkToFit", List.of(true));
		setting.put("Alignment", List.of(HorizontalAlignment.CENTER));
		
		Font font = new  XSSFFont();
		font.setBold(true);
		font.setFontName("Arial");
		
		setting.put("Font", List.of(font));
		
		return setting;
	}

	@Override
	Map<String, List<Object>> defaultBodySetting() {
		Map<String, List<Object>> setting = new HashMap<>();
		
		setting.put("ShrinkToFit", List.of(true));
		setting.put("Alignment", List.of(HorizontalAlignment.CENTER));
		
		Font font = new XSSFFont();
		font.setBold(false);
		font.setFontName("Arial");
		
		setting.put("Font", List.of(font));

		return setting;
	}
}
