package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.TYPE })
public @interface HeaderSetting {

	public int columnSize() default 30;

	public String name() default "";

	public boolean isBold() default false;

	// HeaderComment comment();
}
