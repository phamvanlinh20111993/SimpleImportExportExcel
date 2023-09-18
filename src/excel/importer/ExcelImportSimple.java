package excel.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.in.handle.SimplePrepareStatementSqlInsert;
import database.in.handle.SqlInsert;
import database.in.utils.TransactionIsolationLevel;
import excel.importer.handle.DefaultTableExcelReader;
import excel.importer.handle.TableExcelReader;
import excel.importer.model.PriceTableKiotVietImportDataModel;
import excel.importer.model.PriceTableKiotVietTableModel;
import utils.ObjectUtils;

public class ExcelImportSimple {

	private static final Logger logger = LoggerFactory.getLogger(ExcelImportSimple.class);

	private String pathFile;

	private DataSource datasource;

	public ExcelImportSimple(String pathFile, DataSource datasource) {
		this.pathFile = pathFile;
		this.datasource = datasource;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	protected Workbook createWorkBook(String fileName) {
		try {
			InputStream file = new FileInputStream(new File(this.pathFile));
			Workbook workbook = pathFile.endsWith("xls") ? new HSSFWorkbook(file) : new XSSFWorkbook(file);
			workbook.close();
			file.close();
			return workbook;
		} catch (FileNotFoundException e) {
			logger.error("ExcelImportSimple.createWorkBook(): {}", e.getMessage());
		} catch (IOException e) {
			logger.error("ExcelImportSimple.createWorkBook(): {}", e.getMessage());
		}

		return null;
	}

	/**
	 * 
	 * @param orginalList
	 * @param size
	 * @return
	 */
	protected <T> List<List<T>> paging(List<T> orginalList, int size) {

		if (orginalList == null || size > orginalList.size())
			return List.of(orginalList);

		List<List<T>> cloneList = new LinkedList<>();
		int originListSize = orginalList.size();
		int tmp = 0;
		while (tmp + size < originListSize) {
			cloneList.add(orginalList.subList(tmp, tmp + size));
			tmp += size;
		}
		
		if (tmp < originListSize) {
			cloneList.add(orginalList.subList(tmp, originListSize));
		}

		return cloneList;
	}

	/**
	 * 
	 */
	public void importToDatabase() {

		logger.info("ExcelImportSimple.importToDatabase() start");

		List<PriceTableKiotVietImportDataModel> models = List.of(new PriceTableKiotVietImportDataModel());

		logger.info("ExcelImportSimple.importToDatabase() starting get data from excel");
		Workbook workbook = createWorkBook(pathFile);
		if (workbook == null)
			throw new NullPointerException("Workbook can not be null");

		TableExcelReader excelReader = new DefaultTableExcelReader(workbook, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info("ExcelImportSimple.importToDatabase() starting import data from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableModel> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableModel>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get data from excel file
		for (List<Object> tableRow : datas) {

			// work, good point, insert single data
			// for (Object row : tableRow) {
			// System.out.println("Data " + row.toString());
			// PriceTableKiotVietTableModel priceTableKiotVietTableModel = new
			// PriceTableKiotVietTableModel();
			// priceTableKiotVietTableModel =
			// ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
			// // import to database
			// String result =
			// sqlInsertCommand.singleInsertValue(priceTableKiotVietTableModel);
			// System.out.println("Result " + result);
			// }

			// insert multi data with batch insert
			List<PriceTableKiotVietTableModel> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableModel>();
			for (Object row : tableRow) {
				PriceTableKiotVietTableModel priceTableKiotVietTableModel = new PriceTableKiotVietTableModel();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}
			String result = sqlInsertCommand.batchInsertValues(kiotVietTableModels, true);
			System.out.println("Result " + result);
		}

		logger.info("ExcelImportSimple.importToDatabase() end");
	}

	/**
	 * 
	 */
	public void importToDatabase(int batchSize) {

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) start");

		List<PriceTableKiotVietImportDataModel> models = List.of(new PriceTableKiotVietImportDataModel());

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) starting get data from excel");
		Workbook workbook = createWorkBook(pathFile);
		if (workbook == null)
			throw new NullPointerException("Workbook can not be null");

		TableExcelReader excelReader = new DefaultTableExcelReader(workbook, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info("ExcelImportSimple.importToDatabase() starting import data from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableModel> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableModel>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get data from excel file
		for (List<Object> tableRow : datas) {
			// insert multi data with batch insert
			List<PriceTableKiotVietTableModel> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableModel>();

			for (Object row : tableRow) {
				PriceTableKiotVietTableModel priceTableKiotVietTableModel = new PriceTableKiotVietTableModel();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}

			// Paging
			List<List<PriceTableKiotVietTableModel>> pagingList = paging(kiotVietTableModels, batchSize);

			for (List<PriceTableKiotVietTableModel> list : pagingList) {
				String result = sqlInsertCommand.batchInsertValues(list, true);
				System.out.println("Result " + result);
			}

		}

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) end");
	}
}
