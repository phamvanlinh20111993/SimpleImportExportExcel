package excel.exporter.config;

import lombok.Data;

@Data
public class ColumnInfo {
	
	private RowInfo row;

	private short width;
	
	private CellInfo cellInfo;
	
	private boolean isAutoWidth;
}
