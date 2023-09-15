package excel.exporter.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;

@Data
public class ColumnInfo {

	private RowInfo row;

	private short width;

	private CellInfo cellInfo;

	private boolean isAutoWidth;

	// TODO
	private String moreConfig;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
