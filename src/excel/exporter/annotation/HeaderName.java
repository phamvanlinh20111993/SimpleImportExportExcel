package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import excel.exporter.enums.HeaderNameFormatType;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface HeaderName {

	public String value() default "";

	public HeaderNameFormatType type() default HeaderNameFormatType.NORMAL;
}
