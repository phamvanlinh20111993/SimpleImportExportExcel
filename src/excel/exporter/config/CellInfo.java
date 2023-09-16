package excel.exporter.config;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;

import lombok.Data;

@Data
public class CellInfo {

	private FillPatternType fillPatternType;

	private FontInfo font;

	private VerticalAlignment verticalAlignment;

	private HorizontalAlignment horizontalAlignment;

	private Short backgroundColor;

	private String backgroundRGBColor;

	private String border;

	// TODO will using later
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

	/**
	 * Format following the sequence top:left:bottom:right with
	 * {borderStyle-borderColor}
	 * 
	 * @return
	 */
	public CellBorder[] getBorders() {
		CellBorder[] borders = { new CellBorder(BorderStyle.THIN, IndexedColors.BLACK.getIndex()),
				new CellBorder(BorderStyle.THIN, IndexedColors.BLACK.getIndex()),
				new CellBorder(BorderStyle.THIN, IndexedColors.BLACK.getIndex()),
				new CellBorder(BorderStyle.THIN, IndexedColors.BLACK.getIndex()) };

		String[] bordersStr = this.border.split(":");

		for (int ind = 0; ind < bordersStr.length; ind++) {
			String[] cellBorder = bordersStr[ind].split("-");
			if (cellBorder.length == 2) {
				borders[ind] = new CellBorder(getBorderStyle(Short.parseShort(cellBorder[0])),
						Short.parseShort(cellBorder[1]));
			}
		}

		return borders;
	}

	public boolean isBackgroundRGBColorEmpty() {
		return StringUtils.isAllEmpty(this.backgroundRGBColor);
	}
	
	/**
	 * 
	 * @return
	 */
	public IndexedColors backGroundColorToIndexedColor() {
		IndexedColors defaultIndexedColor = IndexedColors.BLACK;
		for (IndexedColors val : IndexedColors.values()) {
			if (val.getIndex() == this.backgroundColor) {
				defaultIndexedColor = val;
				break;
			}
		}
		return defaultIndexedColor;
	}

	private BorderStyle getBorderStyle(short value) {
		BorderStyle enumValue = BorderStyle.THIN;
		for (BorderStyle val : BorderStyle.values()) {
			if (val.getCode() == value) {
				enumValue = val;
				break;
			}
		}

		return enumValue;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
