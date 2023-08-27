package excel.exporter;

public enum ExcelType {
	XLSX("xlsx"),
	XLS("xls");
	
	private String typeValue;

	private ExcelType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}
}
