package excel.exporter.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import excel.exporter.enums.HeaderNameFormatType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeaderName {

	public HeaderName(String name) {
		this.value = name;
	}

	private String value;

	private HeaderNameFormatType type = HeaderNameFormatType.UPPER;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
