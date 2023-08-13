package database.in.main;

import database.in.annotation.Column;
import database.in.annotation.Table;

@Table(name = "employee")
public class Employee {

	private String name;

	@Column(name = "userId")
	private String id;

	private String department;

	@Column(name = "ageName")
	private Integer age;

	public Employee(String name, String id, String department, Integer age) {
		super();
		this.name = name;
		this.id = id;
		this.department = department;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
