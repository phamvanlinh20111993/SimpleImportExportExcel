package excel.importer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import excel.importer.utils.CellType;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface MappingHeader {
	
	String value() default "";
	
	CellType type() default CellType.STRING;
}
