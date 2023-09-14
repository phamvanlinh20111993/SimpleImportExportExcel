package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.TYPE })
public @interface Font {

	String name() default "Arial";

	String rgbColor() default "100:100:100";

	short color() default -1;

	short size() default (short) 9;

	boolean isUnderline() default false;

	boolean isBold() default false;
}
