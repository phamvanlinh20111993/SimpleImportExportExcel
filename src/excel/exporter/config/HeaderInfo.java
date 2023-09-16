package excel.exporter.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderInfo {
	// TODO will using later
	private Integer rowSpan = 1;
	// TODO will using later
	private Integer colSpan = 1;

	private RowInfo row;

	private Short width = (short) 25 * 256;

	private CellInfo cellInfo;

	private HeaderName name;

	private Boolean isAutoWidth = false;

	// TODO
	private String moreConfig;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
