package excel.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface CellSetting {

	Font font() default @Font;

	FillPatternType fillPatternType() default FillPatternType.SOLID_FOREGROUND;

	VerticalAlignment verticalAlignment() default VerticalAlignment.CENTER;

	HorizontalAlignment horizontalAlignment() default HorizontalAlignment.LEFT;

	/**
	 * Format following the sequence top:left:bottom:right with
	 * {borderStyle-borderColor} About the border style refer {@link BorderStyle}
	 * About the border color refer {@link IndexedColors}}
	 * 
	 * @return
	 */
	String border() default "1-0:1-0:1-0:1-0";

	/**
	 * Refer @{Link {@link IndexedColors}}
	 * 
	 * @return
	 */
	short backgroundColor() default 9; // white

	/**
	 * This configuration only using for XSSF and SXSSF excel. Refer {@link XSSFWorkbook}
	 * and {@link SXSSFWorkbook}
	 * 
	 * @return
	 */
	String backgroundRGBColor() default "255;255;255";

	// TODO
	String moreConfig() default "";

}
