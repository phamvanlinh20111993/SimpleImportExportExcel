package excel.importer.handle;

import java.util.List;

/**
 * 
 * @author PhamLinh
 *
 */
public interface ExcelReader {
	/**
	 * 
	 * @return
	 */
	List<List<Object>> executeImport();
}
