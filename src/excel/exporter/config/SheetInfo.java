package excel.exporter.config;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;

@Data
public class SheetInfo {

	private boolean isFitToPage;

	private String name;

	private boolean isDisplayGrid;

	private FontInfo font;

	// TODO
	private String moreConfig;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
