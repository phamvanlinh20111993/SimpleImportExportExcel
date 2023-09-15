package excel.exporter.handle;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelExporter {

	/**
	 * 
	 * @return
	 */
	public List<List<Object>> getData();

	/**
	 * 
	 * @return
	 */
	public Workbook executeExport();

	/**
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	default Map<String, Object> objectToMap(Object data) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> objectData = new LinkedHashMap<>();

		Field[] fields = data.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object value = field.get(data);
			objectData.put(field.getName(), value);
		}

		return objectData;
	}

	/**
	 * 
	 * @param cell
	 * @param value
	 */
	default void setCellValue(Cell cell, Object value) {

		if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else

		if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else

		if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else

		if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else

		if (value instanceof LocalDate) {
			cell.setCellValue((LocalDate) value);
		} else

		if (value instanceof LocalDateTime) {
			cell.setCellValue((LocalDateTime) value);
		} else {
			cell.setCellValue(String.valueOf(value));
		}

	}
}
