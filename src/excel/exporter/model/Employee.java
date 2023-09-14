package excel.exporter.model;

import java.util.Date;

import excel.exporter.annotation.CellSetting;
import excel.exporter.annotation.ColumnSetting;
import excel.exporter.annotation.Font;
import excel.exporter.annotation.HeaderName;
import excel.exporter.annotation.HeaderSetting;
import excel.exporter.annotation.SheetSetting;

@SheetSetting(isDisplayGrid=true, name="example")
public class Employee {
	
	private Long id;

	@HeaderName("employee name")
	@HeaderSetting(cellInfo = @CellSetting(font = @Font(rgbColor = "200;200;200", isBold = true, size = 11)), isAutoWidth = true)
	@ColumnSetting(isAutoWidth = false)
	private String name;

	@HeaderName("employee age")
	private short age;

	@HeaderName("employee ok")
	private boolean istall;

	private Date currentTime;

	private String date;

	public Employee() {

	}

	public Employee(Long id, String name, short age, boolean istall, Date currentTime) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.istall = istall;
		this.currentTime = currentTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
	}

	public boolean isIstall() {
		return istall;
	}

	public void setIstall(boolean istall) {
		this.istall = istall;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
