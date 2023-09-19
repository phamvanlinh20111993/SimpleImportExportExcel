package excel.exporter.handle;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.exporter.annotation.ColumnSetting;
import excel.exporter.annotation.Font;
import excel.exporter.annotation.HeaderName;
import excel.exporter.annotation.HeaderSetting;
import excel.exporter.annotation.RowSetting;
import excel.exporter.annotation.SheetSetting;
import excel.exporter.datainfo.CellInfo;
import excel.exporter.datainfo.ColumnInfo;
import excel.exporter.datainfo.FontInfo;
import excel.exporter.datainfo.HeaderInfo;
import excel.exporter.datainfo.RowInfo;
import excel.exporter.datainfo.SheetInfo;
import excel.exporter.datainfo.SheetInfoSetting;
import utils.Constants;
import utils.ObjectUtils;

public interface TableExcelExporter extends ExcelExporter {

	Logger logger = LoggerFactory.getLogger(TableExcelExporter.class);

	/**
	 * 
	 * @param path
	 * @throws IOException
	 */
	void out(String path) throws IOException;

	/**
	 * 
	 * @return
	 */
	CellStyle defaultHeaderSetting();

	/**
	 * 
	 * @return
	 */
	CellStyle defaultColumnSetting();

	/**
	 * Convert annotation to object. the annotation field must be mapping with
	 * object field
	 * 
	 * @param obj
	 * @param annotation
	 * @return
	 */
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

					Optional<Object> instance = ObjectUtils
							.createInstanceFromPackage(Constants.EXCEL_EXPORT_POJO_PACKAGE_NAME, nameField.getType());

					if (instance.isPresent()) {
						nameField.set(obj, convertAnnotationToConcreteObject(instance.get(), nestedAnnotation));
					}
					continue;
				}
				nameField.set(obj, value);

			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException
					| InvocationTargetException e) {
				logger.error("Got error: TableExcelExporter.convertAnnotationToConcreteObject() {}", e.getMessage());
			}
		}

		return obj;
	}

	/**
	 * convert annotation setting to POJO object setting
	 * 
	 * @param obj
	 * @return
	 */
	default SheetInfoSetting toSheetInfo(Object obj) {

		SheetInfoSetting sheetInfoSetting = new SheetInfoSetting();

		List<ColumnInfo> columnInfos = new LinkedList<>();

		List<HeaderInfo> headerInfos = new LinkedList<>();

		SheetInfo sheetInfo = new SheetInfo();

		Field[] fields = obj.getClass().getDeclaredFields();
		// init data
		for (int index = 0; index < fields.length; index++) {
			columnInfos.add(new ColumnInfo());
			headerInfos.add(new HeaderInfo());
		}

		if (obj.getClass().isAnnotationPresent(SheetSetting.class)) {
			sheetInfo = toSheetInfo(obj.getClass().getAnnotation(SheetSetting.class));
		}

		HeaderInfo headerInfoDefault = new HeaderInfo();

		ColumnInfo columnInfoDefault = new ColumnInfo();

		FontInfo fontInfoDefault = new FontInfo();

		// defined annotation at class level
		if (obj.getClass().isAnnotationPresent(HeaderSetting.class)) {
			headerInfoDefault = toHeaderInfo(obj.getClass().getAnnotation(HeaderSetting.class));
			for (int index = 0; index < fields.length; index++) {
				headerInfos.set(index, headerInfoDefault);
			}
		}
		// defined annotation at class level
		if (obj.getClass().isAnnotationPresent(ColumnSetting.class)) {
			columnInfoDefault = toColumnInfo(obj.getClass().getAnnotation(ColumnSetting.class));
			for (int index = 0; index < fields.length; index++) {
				columnInfos.set(index, columnInfoDefault);
			}
		}
		// defined annotation at class level
		if (obj.getClass().isAnnotationPresent(Font.class)) {
			fontInfoDefault = toFontInfo(obj.getClass().getAnnotation(Font.class));
			for (int index = 0; index < fields.length; index++) {
				CellInfo cellInfoColumn = new CellInfo();
				cellInfoColumn.setFont(fontInfoDefault);
				columnInfos.get(index).setCellInfo(cellInfoColumn);

				CellInfo cellInfoHeader = new CellInfo();
				cellInfoHeader.setFont(fontInfoDefault);
				headerInfos.get(index).setCellInfo(cellInfoHeader);
			}
		}

		// defined annotation at field level
		int index = 0;
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			if (field.isAnnotationPresent(ColumnSetting.class)) {
				columnInfos.set(index, toColumnInfo(field.getAnnotation(ColumnSetting.class)));
			}

			if (field.isAnnotationPresent(HeaderSetting.class)) {
				headerInfos.set(index, toHeaderInfo(field.getAnnotation(HeaderSetting.class)));
			}

			/**
			 * detect if header name value is empty
			 */
			if (headerInfos.get(index).getName() == null
					|| StringUtils.isAllEmpty(headerInfos.get(index).getName().getValue())) {
				HeaderInfo headerInfo = headerInfos.get(index);
				excel.exporter.datainfo.HeaderName headerName = new excel.exporter.datainfo.HeaderName();
				if (field.isAnnotationPresent(HeaderName.class)) {
					headerName = toHeaderName(field.getAnnotation(HeaderName.class));
					// we don't have any setting, auto get header name is the class property
				} else {
					headerName.setValue(name);
				}
				headerInfo.setName(headerName);
				headerInfos.set(index, headerInfo);
			}

			index++;
		}

		sheetInfoSetting.setSheetInfo(sheetInfo);
		sheetInfoSetting.setColumnInfos(columnInfos);
		sheetInfoSetting.setHeaderInfos(headerInfos);

		return sheetInfoSetting;
	}

	/**
	 * 
	 * @param columnInfoAno
	 * @return
	 */
	default ColumnInfo toColumnInfo(ColumnSetting columnInfoAno) {
		ColumnInfo columnInfo = (ColumnInfo) convertAnnotationToConcreteObject(new ColumnInfo(), columnInfoAno);
		return columnInfo;
	}

	/**
	 * 
	 * @param headerSetting
	 * @return
	 */
	default HeaderInfo toHeaderInfo(HeaderSetting headerSetting) {
		HeaderInfo headerInfo = (HeaderInfo) convertAnnotationToConcreteObject(new HeaderInfo(), headerSetting);
		return headerInfo;
	}

	/**
	 * 
	 * @param headerName
	 * @return
	 */
	default excel.exporter.datainfo.HeaderName toHeaderName(HeaderName headerName) {
		excel.exporter.datainfo.HeaderName headerNameSetting = (excel.exporter.datainfo.HeaderName) convertAnnotationToConcreteObject(
				new excel.exporter.datainfo.HeaderName(), headerName);
		return headerNameSetting;
	}

	/**
	 * 
	 * @param fontAno
	 * @return
	 */
	default FontInfo toFontInfo(Font fontAno) {
		FontInfo fontInfo = (FontInfo) convertAnnotationToConcreteObject(new FontInfo(), fontAno);
		return fontInfo;
	}

	/**
	 * 
	 * @param rowSetting
	 * @return
	 */
	default RowInfo toRowSetting(RowSetting rowSetting) {
		RowInfo rowInfo = (RowInfo) convertAnnotationToConcreteObject(new RowInfo(), rowSetting);
		return rowInfo;
	}

	/**
	 * 
	 * @param sheetSetting
	 * @return
	 */
	default SheetInfo toSheetInfo(SheetSetting sheetSetting) {
		SheetInfo sheetInfo = (SheetInfo) convertAnnotationToConcreteObject(new SheetInfo(), sheetSetting);
		return sheetInfo;
	}

}
