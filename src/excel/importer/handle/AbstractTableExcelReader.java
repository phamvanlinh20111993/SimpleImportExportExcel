package excel.importer.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.importer.exception.HeaderColumnException;
import excel.importer.exception.NotMappingTableHeaderException;
import excel.importer.utils.CellType;

public abstract class AbstractTableExcelReader implements TableExcelReader {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTableExcelReader.class);

	private void validateSheet(Sheet sheet) throws NullPointerException {
		if (sheet == null)
			throw new NullPointerException("Sheet can not be null");
	}

	protected void validateHeader(Sheet sheet, boolean isStrictFormat, Map<String, CellType> configHeader) {

		Map<String, Integer> headers = this.readHeaderFromSheet(sheet);

		if (headers.size() != configHeader.size()) {
			throw new NotMappingTableHeaderException(" is not mapping header size");
		}

		Set<String> notStrictconfigHeader = configHeader.keySet().stream().map(v -> v.toLowerCase())
				.collect(Collectors.toSet());

		for (String headerVal : headers.keySet()) {
			if (!notStrictconfigHeader.contains(headerVal.toLowerCase())) {
				throw new NotMappingTableHeaderException(headerVal + " is not exist in your config header");
			}
		}

		if (isStrictFormat) {
			for (String headerVal : headers.keySet()) {
				if (!configHeader.keySet().contains(headerVal)) {
					throw new NotMappingTableHeaderException(headerVal + " is not valid format");
				}
			}

			List<Integer> indexs = new LinkedList<Integer>();
			for (Integer index : headers.values()) {
				indexs.add(index);
			}

			int index = indexs.get(0);
			for (int ind = 0; ind < indexs.size(); ind++) {
				if (index++ != indexs.get(ind)) {
					throw new HeaderColumnException("Not valid index at column header index " + index);
				}
			}
		}
	}

	protected Map<String, Integer> readHeaderFromSheet(Sheet sheet) {

		validateSheet(sheet);

		Row firstRow = sheet.getRow(0);
		Map<String, Integer> headerFields = new LinkedHashMap<>();
		for (Cell cell : firstRow) {
			headerFields.put(toDataType(cell).get().toString(), cell.getColumnIndex());
		}
		return headerFields;
	}

	protected List<Map<String, Object>> readBodyFromSheet(Sheet sheet, Map<String, CellType> configHeader) {

		logger.info("AbstractTableExcelReader.readBodyFromSheet() start ");

		validateSheet(sheet);

		Map<String, Integer> headers = this.readHeaderFromSheet(sheet);

		Map<Integer, String> invertheader = new HashMap<>();

		for (Map.Entry<String, Integer> header : headers.entrySet()) {
			invertheader.put(header.getValue(), header.getKey());
		}

		List<Map<String, Object>> rowDatas = new ArrayList<>();
		for (int index = 1; index < sheet.getLastRowNum(); index++) {
			Map<String, Object> rowData = new HashMap<>();
			for (Cell cell : sheet.getRow(index)) {
				CellType cellTypeCustom = configHeader.get(invertheader.get(cell.getColumnIndex()));
				Optional<?> data = toDataType(cell);
				if (cellTypeCustom != null) {
					data = toDataType(cell, cellTypeCustom);
				}
				if (data.isPresent()) {
					rowData.put(invertheader.get(cell.getColumnIndex()), data.get());
				}
			}
			rowDatas.add(rowData);
		}

		logger.info("AbstractTableExcelReader.readBodyFromSheet() end ");

		return rowDatas;
	}
}
