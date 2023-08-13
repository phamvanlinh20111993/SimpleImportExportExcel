package excel.reader.utils;

public enum ExcelType {
	XSSF("Horrible Spreadsheet Format"), 
	HSSF("XML Spreadsheet Format");

	private String typeValue;

	private ExcelType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}
}
