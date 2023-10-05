package excel.exporter.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import excel.exporter.ExcelExportSimple;
import excel.exporter.model.Employee;
import excel.exporter.model.Employee1;
import utils.ExcelType;

public class Main {

	public static void main(String[] args) {

		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		List<Employee> lists = new ArrayList<Employee>();

		Employee em = new Employee(1l, "Example 1", (short) 22, true, new Date(), "hm");
		em.setDate(date);
		lists.add(em);

		Employee em1 = new Employee(2l, "Example 2", (short) 2, true, new Date(), "hm");
		em1.setDate(date);
		lists.add(em1);

		Employee em2 = new Employee(3l, "Example 4", (short) 122, true, new Date(), "hm");
		em2.setDate(date);
		lists.add(em2);

		Employee em3 = new Employee(4l, "Example 23", (short) 9, true, new Date(), "hm");
		em3.setDate(date);
		lists.add(em3);

		List<Employee1> lists1 = new ArrayList<Employee1>();

		Employee1 em111 = new Employee1(1l, "Example 1", (short) 22, true, new Date(), "1");
		em.setDate(date);
		lists1.add(em111);

		Employee1 em11 = new Employee1(2l, "Example 2", (short) 2, true, new Date(), "1");
		em11.setDate(date);
		lists1.add(em11);

		Employee1 em21 = new Employee1(3l, "Example 4", (short) 122, true, new Date(), "1");
		em21.setDate(date);
		lists1.add(em21);

		Employee1 em31 = new Employee1(4l, "Example 23", (short) 9, true, new Date(), "1");
		em31.setDate(date);
		lists1.add(em31);

		String outputPath = "E:\\";

		ExcelExportSimple excelExportSimple = new ExcelExportSimple();

		excelExportSimple.setDataSheets(List.of(lists));
		excelExportSimple.setFileName("employee");
		excelExportSimple.setOutputPath(outputPath);
		excelExportSimple.export(ExcelType.SXSSF);
		// excelExportSimple.export(ExcelType.XLS);
	//	excelExportSimple.export(ExcelType.XLSX);

		excelExportSimple.setDataSheets(List.of(lists1));
		excelExportSimple.setFileName("employee1");
		// excelExportSimple.setOutputPath(outputPath);
		// excelExportSimple.export(ExcelType.XLS);
		excelExportSimple.export(ExcelType.XLSX);

		List<List<?>> multiListData = new LinkedList<List<?>>();
		multiListData.add(lists);
		multiListData.add(lists1);

		excelExportSimple.setDataSheets(multiListData);
		excelExportSimple.setFileName("employee-responseData");
		excelExportSimple.setOutputPath(outputPath);
		excelExportSimple.export(ExcelType.XLSX);

	}

}
