package excel.exporter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import excel.exporter.enums.ExcelType;
import excel.exporter.enums.HeaderNameFormatType;
import excel.exporter.handle.SimpleTableExcelExporter;
import excel.exporter.model.Employee;

public class Main {

	public static void main(String[] args) {

		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		List<Employee> lists = new ArrayList<Employee>();

		Employee em = new Employee(1l, "Example 1", (short) 22, true, new Date());
		em.setDate(date);
		lists.add(em);

		Employee em1 = new Employee(2l, "Example 2", (short) 2, true, new Date());
		em1.setDate(date);
		lists.add(em1);

		Employee em2 = new Employee(3l, "Example 4", (short) 122, true, new Date());
		em2.setDate(date);
		lists.add(em2);

		Employee em3 = new Employee(4l, "Example 23", (short) 9, true, new Date());
		em3.setDate(date);
		lists.add(em3);

		SimpleTableExcelExporter<Employee> excelExporter = new SimpleTableExcelExporter<>("employee", "example", lists);

		try {
			excelExporter.out("E:\\");
		} catch (IOException e) {
			e.printStackTrace();
		}

		SimpleTableExcelExporter<Employee> excelExporter1 = new SimpleTableExcelExporter<>("employee1", "example1",
				lists, ExcelType.XLSX, HeaderNameFormatType.UPPER);

		try {
			excelExporter1.out("E:\\");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
