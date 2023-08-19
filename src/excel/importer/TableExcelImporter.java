package excel.importer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;

import excel.importer.exception.NotMappingTableHeaderException;
import excel.importer.utils.CellType;
import excel.importer.utils.Utility;

public interface TableExcelImporter {

	String EMPTY = "";

	Map<String, CellType> configHeader();

	void validationFormat(boolean isStrictFormat) throws NotMappingTableHeaderException;

	Map<String, Integer> readHeader();

	List<Map<String, Object>> readBody();

	default Optional<Object> toDataType(Cell cell) {

		Optional<Object> cellData = Optional.empty();
		switch (cell.getCellType()) {
		case STRING:
			cellData = Optional.of(cell.getStringCellValue());
			break;
		case BOOLEAN:
			cellData = Optional.of(cell.getBooleanCellValue());
			break;
		case NUMERIC:
			cellData = Optional.of(cell.getNumericCellValue());
			break;
		case FORMULA:
			cellData = Optional.of(cell.getCellFormula());
			break;
		case BLANK:
		case _NONE:
		case ERROR:
		default:
			cellData = Optional.of(EMPTY);
			break;
		}

		return cellData;
	}

	default Optional<Object> toDataType(Cell cell, CellType cellType) {

		Optional<Object> cellData = Optional.empty();
		switch (cellType) {
		case STRING:
			cellData = Optional.of(cell.getStringCellValue());
			break;

		case BOOLEAN:
			cellData = Optional.of(cell.getBooleanCellValue());
			break;

		case LONG:
			String longStr = cell.getStringCellValue();
			cellData = Optional.of(Long.valueOf(longStr));
			break;

		case DOUBLE:
			cellData = Optional.of(cell.getNumericCellValue());
			break;

		case DATE:
			cellData = Optional.of(cell.getDateCellValue());
			break;

		case JSON:
			String jsonStr = cell.getStringCellValue();
			cellData = Optional.of(Utility.toMapObject(jsonStr));
			break;

		case ARRAY:
			String arrStr = cell.getStringCellValue();
			cellData = Optional.of(Utility.toArray(arrStr));
			break;

		case LOCALDATETIME:
			cellData = Optional.of(cell.getLocalDateTimeCellValue());
			break;

		case DECIMAL:
			String decimalStr = cell.getStringCellValue();
			cellData = Optional.of(new BigDecimal(decimalStr));
			break;

		default:
			cellData = Optional.of(EMPTY);
			break;
		}

		return cellData;
	}
}
