package excel.exporter.model;

import java.util.Date;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import excel.exporter.annotation.CellSetting;
import excel.exporter.annotation.ColumnSetting;
import excel.exporter.annotation.Font;
import excel.exporter.annotation.HeaderName;
import excel.exporter.annotation.HeaderSetting;
import excel.exporter.annotation.SheetSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SheetSetting(isDisplayGrid = false, name = "example")
public class Employee {

	@HeaderSetting(isAutoWidth = true, name = @HeaderName("HEhe"))
	@ColumnSetting(isAutoWidth = true)
	private Long id;

	@HeaderName("employee name")
	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "255;0;0", isBold = true, size = 11), backgroundRGBColor = "128;128;128", border = "6-2:6-2:6-2:6-2"), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = true)
	private String name;

	@HeaderName("employee age")
	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "255;0;0", isBold = true, size = 11), backgroundRGBColor = "128;128;128", horizontalAlignment = HorizontalAlignment.CENTER), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = true)
	private short age;

	@HeaderName("employee ok")
	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "255;0;0", isBold = true, size = 11), backgroundRGBColor = "128;128;128"), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = true)
	private boolean istall;

	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "255;0;0", isBold = true, size = 11), backgroundRGBColor = "128;128;128"), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = true)
	private Date currentTime;

	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "255;0;0", isBold = true, size = 11), backgroundRGBColor = "128;128;128"), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = true)
	private String date;
}
