package excel.exporter.config;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;

import lombok.Data;

@Data
public class CellInfo {
	
	private FillPatternType fillPatternType;
	
	private FontInfo fontSetting;

	private VerticalAlignment verticalAlignment;

	private HorizontalAlignment horizontalAlignment;

	private short backgroundColor;

	private String backgroundRGBColor;

	private String border;
	
	public XSSFColor getBackgroundRGBColor() {
		String[] rgbStr = backgroundRGBColor.split(";");

		byte[] rgb = new byte[3];
		if (rgbStr.length == 3) {
			rgb[0] = Byte.parseByte(rgbStr[0]);
			rgb[1] = Byte.parseByte(rgbStr[1]);
			rgb[2] = Byte.parseByte(rgbStr[2]);
		}

		return new XSSFColor(rgb);
	}

	public short[] getBorder() {
		short[] borders = { BorderStyle.THIN.getCode(), BorderStyle.THIN.getCode(), BorderStyle.THIN.getCode(),
				BorderStyle.THIN.getCode(), BorderStyle.THIN.getCode() };

		String[] borderStr = border.split(":");
		int length = borderStr.length > 4 ? 4 : borderStr.length;
		for (int ind = 0; ind < length; ind++) {
			borders[ind] = Short.parseShort(borderStr[ind]);
		}

		return borders;
	}
}
