package excel.exporter;

import java.util.List;

import lombok.Data;

@Data
public class ExcelExportAdvance {
	
	private List<List<?>> dataSheets;

	private String outputPath;

	private String fileName;
}
