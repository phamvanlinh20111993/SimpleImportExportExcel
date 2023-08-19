package excel.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import excel.importer.exception.HeaderColumnException;
import excel.importer.exception.NotMappingTableHeaderException;
import excel.importer.utils.CellType;

public abstract class AbstractTableExcelImporter implements TableExcelImporter {

	private Sheet sheet;

	public AbstractTableExcelImporter(Sheet sheet) {
		super();
		this.sheet = sheet;
	}

	public void validationFormat(boolean isStrictFormat) {

		Map<String, Integer> headers = this.readHeader();
		Map<String, CellType> configHeader = this.configHeader();

		if (headers.size() != configHeader.size()) {
			throw new NotMappingTableHeaderException("is not mapping header size");
		}

		Set<String> notStrictconfigHeader = configHeader.keySet().stream().map(v -> v.toLowerCase())
				.collect(Collectors.toSet());

		for (String headerVal : headers.keySet()) {
			if (!notStrictconfigHeader.contains(headerVal.toLowerCase())) {
				throw new NotMappingTableHeaderException(headerVal + "is not exist in your config header");
			}
		}

		if (isStrictFormat) {
			for (String headerVal : headers.keySet()) {
				if (!configHeader.keySet().contains(headerVal)) {
					throw new NotMappingTableHeaderException(headerVal + "is not valid format");
				}
			}

			List<Integer> indexs = (List<Integer>) headers.values();
			int index = indexs.get(0);
			for (int ind = 1; ind < indexs.size(); ind++) {
				if (index + 1 != indexs.get(ind)) {
					throw new HeaderColumnException("not valid index at column header index");
				}
			}

		}

	}

	public Map<String, Integer> readHeader() {
		Row firstRow = sheet.getRow(0);
		Map<String, Integer> headerFields = new HashMap<>();

		for (Cell cell : firstRow) {
			headerFields.put(toDataType(cell).get().toString(), cell.getColumnIndex());
		}
		return headerFields;
	}

	public List<Map<String, Object>> readBody() {
		
		Map<String, Integer> headers = this.readHeader();

		Map<String, CellType> configHeaders = this.configHeader();

		Map<Integer, String> invertheader = new HashMap<>();

		for (Map.Entry<String, Integer> header : headers.entrySet()) {
			invertheader.put(header.getValue(), header.getKey());
		}

		List<Map<String, Object>> rowDatas = new ArrayList<>();
		for (int index = 1; index < sheet.getLastRowNum(); index++) {
			Map<String, Object> rowData = new HashMap<>();
			for (Cell cell : sheet.getRow(index)) {

				CellType cellTypeCustom = configHeaders.get(invertheader.get(cell.getColumnIndex()));
				Optional<Object> data = toDataType(cell);
				if (cellTypeCustom != null) {
					data = toDataType(cell, cellTypeCustom);
				}

				if (data.isPresent()) {
					rowData.put(invertheader.get(cell.getColumnIndex()), data.get());
				}
			}
			rowDatas.add(rowData);
		}

		return rowDatas;
	}
}
