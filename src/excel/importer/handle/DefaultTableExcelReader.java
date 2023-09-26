package excel.importer.handle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.importer.utils.CellType;
import input.validation.handle.RowDataHandle;
import input.validation.handle.SimpleHandleRowData;

/**
 * 
 * @author PhamLinh
 *
 */
public class DefaultTableExcelReader extends AbstractTableExcelReader {

	private static final Logger logger = LoggerFactory.getLogger(DefaultTableExcelReader.class);

	protected Workbook workbook;

	protected RowDataHandle<?> rowDataHandle = new SimpleHandleRowData<>();

	public DefaultTableExcelReader(Workbook workbook, List<?> objectTemplates) {
		super(objectTemplates);
		this.workbook = workbook;
	}

	@Override
	public List<List<Object>> executeImport() {

		logger.info("DefaultTableExcelReader.executeImport() start");

		List<List<Object>> res = new LinkedList<List<Object>>();

		List<Map<String, CellType>> configHeadersMap = this.configHeaders();
		for (int ind = 0; ind < workbook.getNumberOfSheets(); ind++) {
			Sheet sheet = workbook.getSheetAt(ind);
			validateHeader(sheet, true, configHeadersMap.get(ind));

			List<Map<String, Object>> data = this.readBodyFromSheet(sheet, configHeadersMap.get(ind));

			List<Object> lists = new LinkedList<>();
			for (Map<String, Object> prop : data) {
				Optional<Object> realObj = toObject(objectTemplates.get(ind).getClass(), prop);
				if (realObj.isPresent()) {
					rowDataHandle = new SimpleHandleRowData<>(realObj.get());
					rowDataHandle.validateColumns();
					if (rowDataHandle.isRowDataError()) {
						logger.error("Error DefaultTableExcelReader.executeImport() {}",
								rowDataHandle.getErrorDetails());
						logger.error("Error DefaultTableExcelReader.executeImport() {}", realObj.get());
						continue;
					}

					lists.add(realObj.get());
				}
			}

			res.add(lists);
		}

		logger.info("DefaultTableExcelReader.executeImport() end");

		return res;
	}
}
