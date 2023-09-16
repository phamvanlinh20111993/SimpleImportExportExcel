package excel.exporter.enums;

import org.apache.commons.lang3.StringUtils;

public enum HeaderNameFormatType {
	DEFAULT("default"), 
	NORMAL("normal"), 
	UPPER("upper"), 
	LOWER("lower");

	private String typeValue;

	private HeaderNameFormatType(String typeValue) {
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return this.typeValue;
	}

	public String changeFormatType(String value) {

		if (StringUtils.isAllEmpty(value)) {
			return value;
		}

		if (this.equals(HeaderNameFormatType.NORMAL)) {
			value = value.trim().toLowerCase();
			value = value.substring(0, 1).toUpperCase() + value.substring(1);
		} else

		if (this.equals(HeaderNameFormatType.UPPER)) {
			value = value.trim().toUpperCase();
		} else

		if (this.equals(HeaderNameFormatType.LOWER)) {
			value = value.trim().toLowerCase();
		}

		return value;
	}
}
