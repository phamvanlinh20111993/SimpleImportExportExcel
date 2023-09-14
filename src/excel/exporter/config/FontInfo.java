package excel.exporter.config;

import org.apache.poi.xssf.usermodel.XSSFColor;

import lombok.Data;

@Data
public class FontInfo {

	private String name;

	private String rgbColor;

	private short color;

	private short size;

	private boolean isUnderline;

	private boolean isBold;

	public XSSFColor getRgbColor() {
		String[] rgbStr = rgbColor.split(";");
		byte[] rgb = new byte[3];
		if (rgbStr.length == 3) {
			rgb[0] = Byte.parseByte(rgbStr[0]);
			rgb[1] = Byte.parseByte(rgbStr[1]);
			rgb[2] = Byte.parseByte(rgbStr[2]);
		}
		return new XSSFColor(rgb);
	}
}
