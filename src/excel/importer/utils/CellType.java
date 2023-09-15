package excel.importer.utils;

public enum CellType {
	STRING("String"), DOUBLE("Double"), LONG("Long"), DATE("Date"), DECIMAL("Decimal"), ARRAY("Array"), JSON(
			"Json"), LOCALDATETIME("LocalDateTime"), BOOLEAN("Bool");

	private String typeValue;

	private CellType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}
}
