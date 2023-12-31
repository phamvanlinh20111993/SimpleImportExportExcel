package excel.exporter.main;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import excel.exporter.annotation.Font;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface Example {
	int val() default -1;

	String value() default "ello";

	Font font() default @Font;
}
