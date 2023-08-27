package excel.exporter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelExporter {

	public Map<String, Map<String, List<Object>>> configSheets();

	public List<List<Object>> getData();

	public Workbook export();

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

	default CellStyle settingCellStyle(CellStyle cellStyle, Map<String, List<Object>> settings)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		for (Map.Entry<String, List<Object>> setting : settings.entrySet()) {
			Method method = cellStyle.getClass().getMethod("set" + setting.getKey());
			List<Object> parameterValues = setting.getValue();

			if (parameterValues.size() == 1) {
				method.invoke(cellStyle, parameterValues.get(0));
			} else

			if (parameterValues.size() == 2) {
				method.invoke(cellStyle, parameterValues.get(0), parameterValues.get(1));
			} else

			if (parameterValues.size() == 3) {
				method.invoke(cellStyle, parameterValues.get(0), parameterValues.get(1), parameterValues.get(2));
			} else

			if (parameterValues.size() == 4) {
				method.invoke(cellStyle, parameterValues.get(0), parameterValues.get(1), parameterValues.get(2),
						parameterValues.get(3));
			}
		}

		return cellStyle;

	}

	default Sheet settingSheet(Sheet sheet, Map<String, List<Object>> settings) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		for (Map.Entry<String, List<Object>> setting : settings.entrySet()) {

			Method method = sheet.getClass().getMethod("set" + setting.getKey());

			List<Object> parameterValues = setting.getValue();

			if (parameterValues.size() == 1) {
				method.invoke(sheet, parameterValues.get(0));
			} else

			if (parameterValues.size() == 2) {
				method.invoke(sheet, parameterValues.get(0), parameterValues.get(1));
			} else

			if (parameterValues.size() == 3) {
				method.invoke(sheet, parameterValues.get(0), parameterValues.get(1), parameterValues.get(2));
			} else

			if (parameterValues.size() == 4) {
				method.invoke(sheet, parameterValues.get(0), parameterValues.get(1), parameterValues.get(2),
						parameterValues.get(3));
			}
		}

		return sheet;
	}
}
