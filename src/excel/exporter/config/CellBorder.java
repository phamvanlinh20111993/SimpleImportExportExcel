package excel.exporter.config;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Data;

@Data
public class CellBorder {

	private short boderColor;

	private BorderStyle borderStyle;
}
