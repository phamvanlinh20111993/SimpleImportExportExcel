package excel.exporter;

import java.util.List;
import java.util.Map;

public interface ExcelExporter {
	
	public void configHeader();

	public List<Map<String, Object>> configCells();
	
	public List<Map<String, Object>> configFooter();

	public void export();
	
	
}
