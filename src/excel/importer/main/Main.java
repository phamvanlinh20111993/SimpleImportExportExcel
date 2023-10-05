package excel.importer.main;

import java.io.File;

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

		String smallPath = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263-0.xlsx";
		String bigPath = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";

		ExcelImportSimple excelImportSimple = new ExcelImportSimple(smallPath, defaultDataSource());

		// excelImportSimple.importToDatabase();

		// excelImportSimple.importToDatabase(100);
		long startTime = System.nanoTime();
		ExcelImportSimple excelImportSimple1 = new ExcelImportSimple(bigPath, defaultDataSource());
		excelImportSimple1.importBigDataToDatabase(5000);
		
		/**
		 * Exception in thread "main" Exception in thread "mysql-cj-abandoned-connection-cleanup" Cleaning up unclosed ZipFile for archive C:\Users\DELL\eclipse-workspace\SimpleImportExportExcel\BangGia_KV14082023-213536-263.xlsx
			java.lang.OutOfMemoryError: Java heap space
			java.lang.OutOfMemoryError: Java heap space
		 */
		// => importToDatabase() function can not use for imort excel big data file
		//excelImportSimple1.importToDatabase();
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Total time " + totalTime / 1000000000 + "s");

		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "log4j.xml";
		System.out.println(log4jConfigFile);

	}

}
