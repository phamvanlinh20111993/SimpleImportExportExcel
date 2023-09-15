package excel.exporter.config;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;

import lombok.Data;

@Data
public class CellInfo {

	private FillPatternType fillPatternType;

	private FontInfo font;

	private VerticalAlignment verticalAlignment;

	private HorizontalAlignment horizontalAlignment;

	private short backgroundColor;

	private String backgroundRGBColor;

	private String border;

	// TODO
	private String moreConfig;

	public XSSFColor getBackgroundRGBColor() {
		String[] rgbStr = backgroundRGBColor.split(";");

		short[] rgb = new short[3];
		if (rgbStr.length == 3) {
			rgb[0] = Short.parseShort(rgbStr[0]);
			rgb[1] = Short.parseShort(rgbStr[1]);
			rgb[2] = Short.parseShort(rgbStr[2]);
		}

		return new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]), null);
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

	public boolean isBackgroundRGBColorEmpty() {
		return StringUtils.isAllEmpty(this.backgroundRGBColor);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
