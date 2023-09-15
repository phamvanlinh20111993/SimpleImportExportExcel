package excel.exporter.config;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;

@Data
public class SheetInfoSetting {

	private SheetInfo sheetInfo;

	private List<ColumnInfo> columnInfos;

	private List<HeaderInfo> headerInfos;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
