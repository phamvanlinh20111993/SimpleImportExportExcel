package excel.exporter.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import excel.exporter.enums.RowType;
import lombok.Data;

@Data
public class RowInfo {
	private short height = (short) 400;

	private RowType type;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
