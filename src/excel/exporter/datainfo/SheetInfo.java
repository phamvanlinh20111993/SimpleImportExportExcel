package excel.exporter.datainfo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;

@Data
public class SheetInfo {

	private Boolean isFitToPage;

	private String name;

	private Boolean isDisplayGrid;

	private FontInfo font;

	// TODO
	private String moreConfig;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
