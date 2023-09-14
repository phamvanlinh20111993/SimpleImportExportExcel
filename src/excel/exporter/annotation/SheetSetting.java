package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface SheetSetting {
	
	String name() default "";
	
	boolean isFitToPage() default true;
	
	boolean isDisplayGrid() default true;
	
	Font font() default @Font;
}
