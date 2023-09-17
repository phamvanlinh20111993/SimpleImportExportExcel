package excel.importer.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import excel.importer.handle.DefaultTableExcelReader;
import excel.importer.handle.TableExcelReader;
import main.PriceTableKiotVietDataModel;

public class Main {

	public static void main(String[] args) {

		String path = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";
		FileInputStream file;
		try {
			file = new FileInputStream(new File(path));
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			List<PriceTableKiotVietDataModel> models = List.of(new PriceTableKiotVietDataModel());
			TableExcelReader excelImporter = new DefaultTableExcelReader(workbook, models);

			List<List<Object>> datas = excelImporter.executeImport();

			for (List<Object> tableRow : datas) {
				for (Object row : tableRow) {
					System.out.println("Data " + row.toString());
				}
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
