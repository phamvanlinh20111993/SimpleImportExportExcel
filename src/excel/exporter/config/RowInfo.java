package excel.exporter.config;

import excel.exporter.enums.RowType;
import lombok.Data;

@Data
public class RowInfo {
	private short height;

	private RowType type;
}
