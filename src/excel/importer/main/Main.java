package excel.importer.main;

import excel.importer.ExcelImportSimple;

public class Main {

	public static void main(String[] args) {

		String path = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";
		
		ExcelImportSimple excelImportSimple = new ExcelImportSimple(path);
		
		excelImportSimple.importToDatabase();

	}

}
