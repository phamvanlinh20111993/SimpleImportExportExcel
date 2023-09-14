package excel.exporter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderInfo {
	
	private RowInfo row;

	private short width;
	
	private CellInfo cellInfo;
	
	private HeaderName name;
	
	private boolean isAutoWidth;
	
}
