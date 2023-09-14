package excel.importer.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import excel.importer.SimpleTableExcelImporter;
import excel.importer.TableExcelImporter;

public class Main {

	public static void main(String[] args) {

		String path = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";
		FileInputStream file;
		try {
			file = new FileInputStream(new File(path));
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			SimpleTableExcelImporter excelImporter = new SimpleTableExcelImporter(sheet);

			List<Map<String, Object>> datas = excelImporter.read(true);

			for (Map<String, Object> row : datas) {
				for (Map.Entry<String, Object> cellData : row.entrySet()) {
					System.out.print(cellData + " ");
				}
				System.out.println();
			}

			workbook.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
