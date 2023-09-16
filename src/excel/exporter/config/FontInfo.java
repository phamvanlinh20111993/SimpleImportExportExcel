package excel.exporter.config;

import java.awt.Color;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

import lombok.Data;

@Data
public class FontInfo {

	private String name;

	private String rgbColor;

	private Short color;

	private Short size;

	private Boolean isUnderline;

	private Boolean isBold;

	public XSSFColor getXSSFRgbColor() {
		String[] rgbStr = rgbColor.split(";");
		short[] rgb = new short[3];
		if (rgbStr.length == 3) {
			rgb[0] = Short.parseShort(rgbStr[0]);
			rgb[1] = Short.parseShort(rgbStr[1]);
			rgb[2] = Short.parseShort(rgbStr[2]);
		}
		return new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]), new DefaultIndexedColorMap());
	}

	public HSSFColor getHSSFRgbColor(HSSFWorkbook workbook) {
		String[] rgbStr = rgbColor.split(";");
		short[] rgb = new short[3];
		if (rgbStr.length == 3) {
			rgb[0] = Short.parseShort(rgbStr[0]);
			rgb[1] = Short.parseShort(rgbStr[1]);
			rgb[2] = Short.parseShort(rgbStr[2]);
		}

		HSSFWorkbook hssfworkbook = (HSSFWorkbook) workbook;
		HSSFPalette palette = hssfworkbook.getCustomPalette();

		HSSFColor hssfColor = palette.findSimilarColor(rgb[0], rgb[1], rgb[2]);
		if (hssfColor == null) {
			hssfColor = palette.findSimilarColor(rgb[0], rgb[1], rgb[2]);
		}

		return hssfColor;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
