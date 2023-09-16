package excel.exporter.model;

import java.util.Date;

import excel.exporter.annotation.SheetSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SheetSetting(isDisplayGrid = true, name = "example1")
public class Employee1 {

	private Long id;

	private String name;

	private short age;

	private boolean istall;

	private Date currentTime;

	private String date;
}
