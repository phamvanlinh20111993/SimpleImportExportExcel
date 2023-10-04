package utils;

public enum ExcelType {
	XLSX("xlsx"), 
	XLS("xls"), 
	SXSSF("xlsx");

	private String typeValue;

	private ExcelType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}
}
