package excel.exporter.datainfo;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellBorder {
	
	private BorderStyle borderStyle;
	
	private Short borderColor;

	
}
