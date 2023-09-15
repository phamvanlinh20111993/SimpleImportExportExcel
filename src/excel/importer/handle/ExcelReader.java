package excel.importer.handle;

import java.util.Map;

public interface ExcelReader {

	public void config();

	public Map<String, Object> execute();

	public void finish();

}
