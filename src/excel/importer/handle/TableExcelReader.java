package excel.importer.handle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.importer.annotation.MappingField;
import excel.importer.utils.CellType;
import excel.importer.utils.Utility;
import utils.Constants;

public interface TableExcelReader extends ExcelReader {

	public static final Logger logger = LoggerFactory.getLogger(TableExcelReader.class);

	public List<Map<String, CellType>> configHeaders();

	/**
	 * 
	 * @param clazz
	 * @param properties
	 * @return
	 */
	default Optional<Object> toObject(Class<?> clazz, Map<String, Object> properties) {

		Object obj;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();

			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(MappingField.class)) {
					String value = field.getAnnotation(MappingField.class).value();
					field.set(obj, properties.get(value));
				} else {
					String fieldName = field.getName();
					field.set(obj, properties.get(fieldName));
				}
			}

			return Optional.of(obj);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			logger.error("TableExcelReader.toObject() error {}", e.getMessage());
		}

		return Optional.empty();
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	default Optional<?> toDataType(Cell cell) {

		Optional<?> cellData = Optional.empty();
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
			cellData = Optional.of(Constants.EMPTY);
			break;
		}

		return cellData;
	}

	/**
	 * 
	 * @param cell
	 * @param cellType
	 * @return
	 */
	default Optional<?> toDataType(Cell cell, CellType cellType) {

		Optional<?> cellData = Optional.empty();

		Optional<?> rawData = toDataType(cell);
		switch (cellType) {
		case STRING:
			cellData = rawData.isPresent() ? Optional.of(rawData.get().toString()) : Optional.of(Constants.EMPTY);
			break;
		case BOOLEAN:
			if (rawData.isPresent() && rawData.get() instanceof Boolean) {
				cellData = Optional.of(rawData.get());
			}
			break;
		case LONG:
			if (rawData.isPresent()) {
				cellData = Optional.of(Long.valueOf(rawData.get().toString()));
			}
			break;
		case DOUBLE:
			if (rawData.isPresent()) {
				cellData = Optional.of(Double.valueOf(rawData.get().toString()));
			}
			break;
		case DATE:
			if (rawData.isPresent() && rawData.get() instanceof Date) {
				cellData = Optional.of(rawData.get());
			}
			break;
		case JSON:
			String jsonStr = rawData.isPresent() ? rawData.toString() : "{}";
			cellData = Optional.of(Utility.toMapObject(jsonStr));
			break;
		case ARRAY:
			if (rawData.isPresent()) {
				cellData = Optional.of(Utility.toArray(rawData.get().toString()));
			}
			break;
		case LOCALDATETIME:
			if (rawData.isPresent() && rawData.get() instanceof LocalDateTime) {
				cellData = Optional.of(rawData.get());
			}
			break;
		case DECIMAL:
			if (rawData.isPresent()) {
				cellData = Optional.of(new BigDecimal(rawData.get().toString()));
			}
			break;
		default:
			cellData = Optional.of(Constants.EMPTY);
			break;
		}

		return cellData;
	}
}
