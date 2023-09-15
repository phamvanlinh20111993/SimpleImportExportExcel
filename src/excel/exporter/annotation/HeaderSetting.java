package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import excel.exporter.enums.RowType;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.TYPE })
public @interface HeaderSetting {

	boolean isAutoWidth() default true;

	RowSetting row() default @RowSetting(type = RowType.HEADER);

	short width() default (short) 250;

	CellSetting cellInfo() default @CellSetting(horizontalAlignment = HorizontalAlignment.CENTER);

	HeaderName name() default @HeaderName;

	String moreConfig() default "";
}
