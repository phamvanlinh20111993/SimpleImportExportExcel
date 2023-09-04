package excel.exporter.handle;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import excel.exporter.annotation.HeaderComment;
import excel.exporter.annotation.HeaderName;
import excel.exporter.enums.HeaderNameFormatType;

public interface TableExcelExporter extends ExcelExporter {
	
	public List<CellStyle> configHeader();

	public Row configHeaderRow();

	public List<CellStyle> configBody();

	public Row configBodyRow();

	default List<String> getHeaderName(Object obj) {
		List<String> headers = new LinkedList<String>();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			
			HeaderNameFormatType headerNameFormatType = HeaderNameFormatType.NORMAL;
			
			if (field.isAnnotationPresent(HeaderName.class)) {
				HeaderName hearNameAtt = field.getAnnotation(HeaderName.class);
				name = hearNameAtt.value();
				headerNameFormatType = hearNameAtt.type();
			}
			
			String comment;
			if (field.isAnnotationPresent(HeaderComment.class)) {
				HeaderComment hearComment = field.getAnnotation(HeaderComment.class);
				comment = hearComment.value();
			}

			headers.add(headerNameFormatType.changeFormatType(name));
		}

		return headers;
	}
}
