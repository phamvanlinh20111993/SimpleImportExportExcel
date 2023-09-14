package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD})
public @interface CellSetting {
	
	Font font() default @Font;
	
	FillPatternType fillPatternType() default FillPatternType.SOLID_FOREGROUND;
	
	VerticalAlignment verticalAlignment() default VerticalAlignment.CENTER;
	
	HorizontalAlignment horizontalAlignment() default HorizontalAlignment.LEFT;
	
	String border() default "1-r:1-r:1-r:1-r";
	
	short backgroundColor() default 8; // black
	
	String backgroundRGBColor() default "200;200;200";
}
