package excel.exporter.config;

import java.util.List;

import lombok.Data;

@Data
public class SheetInfoSetting {
	
	private SheetInfo sheetInfo;
	
	private List<ColumnInfo> columnInfos;
	
	private List<HeaderInfo> headerInfos;

}
