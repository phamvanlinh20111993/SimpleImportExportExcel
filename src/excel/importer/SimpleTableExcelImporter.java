package excel.importer;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import excel.importer.annotation.MappingHeader;
import excel.importer.utils.CellType;

public class SimpleTableExcelImporter<T> extends AbstractTableExcelImporter {

	protected Workbook workbook;

	T object;

	public SimpleTableExcelImporter(Sheet sheet) {
		super(sheet);
	}

	@Override
	public Map<String, CellType> configHeader() {

		Map<String, CellType> configHeaders = new LinkedHashMap<String, CellType>();
		Field[] fields = object.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(MappingHeader.class)) {
				MappingHeader mappingHeader = field.getAnnotation(MappingHeader.class);
				configHeaders.put(mappingHeader.value(), mappingHeader.type());
			}
		}

		return configHeaders;
	}

}
