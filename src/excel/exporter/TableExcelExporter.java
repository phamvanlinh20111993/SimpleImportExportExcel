package excel.exporter;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface TableExcelExporter extends ExcelExporter {

	public List<Map<String, Map<String, List<Object>>>> configHeader();

	public List<Map<String, Map<String, List<Object>>>> configBody();

	default List<String> getHeaderName(Object obj, HeaderNameFormatType formatType) {
		List<String> headers = new LinkedList<String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			if (field.isAnnotationPresent(HeaderName.class)) {
				HeaderName hearNameAtt = field.getAnnotation(HeaderName.class);
				name = hearNameAtt.value();
			}

			headers.add(formatType.changeFormatType(name));
		}

		return headers;
	}
}
