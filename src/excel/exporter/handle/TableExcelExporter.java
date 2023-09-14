package excel.exporter.handle;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import excel.exporter.annotation.ColumnSetting;
import excel.exporter.annotation.Font;
import excel.exporter.annotation.HeaderName;
import excel.exporter.annotation.HeaderSetting;
import excel.exporter.annotation.RowSetting;
import excel.exporter.annotation.SheetSetting;
import excel.exporter.config.ColumnInfo;
import excel.exporter.config.FontInfo;
import excel.exporter.config.HeaderInfo;
import excel.exporter.config.RowInfo;
import excel.exporter.config.SheetInfo;
import excel.exporter.config.SheetInfoSetting;

public interface TableExcelExporter extends ExcelExporter {

	void out(String path) throws IOException;

	default Object convertAnnotationToConcreteObject(Object obj, Annotation annotation) {

		Method[] methods = annotation.annotationType().getDeclaredMethods();

		for (Method method : methods) {
			try {
				Object value = method.invoke(annotation);
				Field nameField;
				nameField = obj.getClass().getDeclaredField(method.getName());
				nameField.setAccessible(true);

				if (method.getReturnType().isAnnotation()) {
					Annotation nestedAnnotation = (Annotation) value;
					nameField.set(obj, convertAnnotationToConcreteObject(obj, nestedAnnotation));
					continue;
				}
				nameField.set(obj, value);

			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException
					| InvocationTargetException e) {
				System.err.println("Error " + e.getMessage());
			}
		}

		return obj;
	}

	default SheetInfoSetting toSheetInfo(Object obj) {

		SheetInfoSetting sheetInfoSetting = new SheetInfoSetting();

		List<ColumnInfo> columnInfos = new LinkedList<>();

		List<HeaderInfo> headerInfos = new LinkedList<>();

		SheetInfo sheetInfo = new SheetInfo();

		Field[] fields = obj.getClass().getDeclaredFields();

		if (obj.getClass().isAnnotationPresent(SheetSetting.class)) {
			sheetInfo = toSheetInfo(obj.getClass().getAnnotation(SheetSetting.class));
		}

		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			if (field.isAnnotationPresent(ColumnSetting.class)) {
				columnInfos.add(toColumnInfo(field.getAnnotation(ColumnSetting.class)));
			}
			if (field.isAnnotationPresent(HeaderSetting.class)) {
				headerInfos.add(toHeaderInfo(field.getAnnotation(HeaderSetting.class)));
			} else {
				HeaderInfo headerInfo = new HeaderInfo();
				headerInfo.setName(new excel.exporter.config.HeaderName(name));
				headerInfos.add(new HeaderInfo());
			}
		}

		sheetInfoSetting.setSheetInfo(sheetInfo);
		sheetInfoSetting.setColumnInfos(columnInfos);
		sheetInfoSetting.setHeaderInfos(headerInfos);

		return sheetInfoSetting;
	}

	default ColumnInfo toColumnInfo(ColumnSetting columnInfoAno) {
		return (ColumnInfo) convertAnnotationToConcreteObject(new ColumnInfo(), columnInfoAno);
	}

	default HeaderInfo toHeaderInfo(HeaderSetting headerSetting) {
		return (HeaderInfo) convertAnnotationToConcreteObject(new HeaderInfo(), headerSetting);
	}

	default excel.exporter.config.HeaderName toHeaderName(HeaderName headerName) {
		return (excel.exporter.config.HeaderName) convertAnnotationToConcreteObject(
				new excel.exporter.config.HeaderName(), headerName);
	}

	default FontInfo toFontInfo(Font fontAno) {
		return (FontInfo) convertAnnotationToConcreteObject(new FontInfo(), fontAno);
	}

	default RowInfo toRowSetting(RowSetting rowSetting) {
		return (RowInfo) convertAnnotationToConcreteObject(new RowInfo(), rowSetting);
	}

	default SheetInfo toSheetInfo(SheetSetting sheetSetting) {
		return (SheetInfo) convertAnnotationToConcreteObject(new SheetInfo(), sheetSetting);
	}

}
