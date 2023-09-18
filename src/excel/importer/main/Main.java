package excel.importer.main;

import org.apache.commons.dbcp2.BasicDataSource;

import excel.importer.ExcelImportSimple;

public class Main {
	
	/**
	 * 
	 * @return
	 */
	private static BasicDataSource defaultDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/fake?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		dataSource.setMinIdle(5);
		dataSource.setMaxIdle(10);
		dataSource.setMaxTotal(25);
		dataSource.setMaxWaitMillis(30000);

		return dataSource;
	}

	public static void main(String[] args) {

		String path = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";
		
		ExcelImportSimple excelImportSimple = new ExcelImportSimple(path, defaultDataSource());
		
		// excelImportSimple.importToDatabase();
		
		excelImportSimple.importToDatabase(10);

	}

}
