package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import excel.exporter.enums.RowType;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.TYPE })
public @interface ColumnSetting {
	
	boolean isAutoWidth() default false;
	
	RowSetting row() default @RowSetting(type=RowType.BODY);
	
	short width() default (short)250;
	
	CellSetting cellInfo() default @CellSetting;
}
