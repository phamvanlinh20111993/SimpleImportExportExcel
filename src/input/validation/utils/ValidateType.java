package input.validation.utils;

public enum ValidateType {
	DATE("Date"), STRING("String"), NUMBER("Number");

	private String typeValue;

	private ValidateType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}
}
