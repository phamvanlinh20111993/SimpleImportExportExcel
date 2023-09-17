package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExcelImportSimple {

	private static final Logger logger = LoggerFactory.getLogger(ExcelImportSimple.class);

	private Workbook workbook;

	private String pathFile;

	public ExcelImportSimple(String pathFile, String sheetName) {
		this.pathFile = pathFile;
	}

	protected void createWorkBook(String fileName) {
		try {
			InputStream file = new FileInputStream(new File(this.pathFile));
			this.workbook = pathFile.endsWith("xls") ? new HSSFWorkbook(file) : new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			logger.error("createWorkBook(): {}", e.getMessage());
		} catch (IOException e) {
			logger.error("createWorkBook(): {}", e.getMessage());
		}
	}
}
