package excel.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.in.handle.SimplePrepareStatementSqlInsert;
import database.in.handle.SqlInsert;
import database.in.utils.TransactionIsolationLevel;
import excel.exporter.exception.NotMappingTypeException;
import excel.importer.handle.DefaultTableExcelReader;
import excel.importer.handle.SAXParserTableExcelReader;
import excel.importer.handle.TableExcelReader;
import excel.importer.model.PriceTableKiotVietImportDataModel;
import excel.importer.model.PriceTableKiotVietTableEntity;
import utils.ExcelType;
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
	 * @throws InvalidFormatException
	 */
	protected Workbook createWorkBook(String path, boolean isStreamingMode) {

		if (path == null) {
			throw new NullPointerException("path can not be null");
		}

		String[] filePath = getFileName().split("\\.");
		String extention = filePath[filePath.length - 1];

		if (!extention.equals(ExcelType.XLS.getTypeValue()) && !extention.equals(ExcelType.XLSX.getTypeValue())) {
			throw new NotMappingTypeException("Not support type " + extention);
		}

		Workbook workbook = null;
		// refer:
		// https://poi.apache.org/components/spreadsheet/quick-guide.html#FileInputStream
		try {
			if (extention.equals(ExcelType.XLS.getTypeValue())) {
				POIFSFileSystem fs = new POIFSFileSystem(new File(this.pathFile), true);
				workbook = new HSSFWorkbook(fs.getRoot(), true);
				fs.close();
			} else if (extention.equals(ExcelType.XLSX.getTypeValue())) {
				OPCPackage pkg = OPCPackage.open(new File(this.pathFile), PackageAccess.READ);
				if (isStreamingMode) {
					// https://stackoverflow.com/questions/11891851/how-to-load-a-large-xlsx-file-with-apache-poi
					// https://poi.apache.org/components/spreadsheet/index.html
					workbook = new SXSSFWorkbook(new XSSFWorkbook(pkg));
				} else {
					workbook = new XSSFWorkbook(pkg);
				}
				pkg.close();
			}
			workbook.close();
			return workbook;
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException ExcelImportSimple.createWorkBook(): {}", e.getMessage());
		} catch (IOException e) {
			logger.error("IOException ExcelImportSimple.createWorkBook(): {}", e.getMessage());
		} catch (InvalidFormatException e) {
			logger.error("InvalidFormatException ExcelImportSimple.createWorkBook(): {}", e.getMessage());
		}

		return workbook;
	}

	private String getFileName() {
		String[] paths = this.pathFile.split("/");
		String fileName = paths[paths.length - 1];
		return fileName;
	}

	/**
	 * 
	 * @param orginalList
	 * @param size
	 * @return
	 */
	protected <T> List<List<T>> paging(List<T> orginalList, int size) {

		if (orginalList == null || size > orginalList.size()) {
			return List.of(orginalList);
		}

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

		logger.info("ExcelImportSimple.importToDatabase() starting get responseData from excel");
		Workbook workbook = createWorkBook(pathFile, false);
		TableExcelReader excelReader = new DefaultTableExcelReader(workbook, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info("ExcelImportSimple.importToDatabase() starting import responseData from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableEntity> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableEntity>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get responseData from excel file
		for (List<Object> tableRow : datas) {

			// work, good point, insert single responseData
			// for (Object row : tableRow) {
			// System.out.println("Data " + row.toString());
			// PriceTableKiotVietTableEntity priceTableKiotVietTableModel = new
			// PriceTableKiotVietTableEntity();
			// priceTableKiotVietTableModel =
			// ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
			// // import to database
			// String result =
			// sqlInsertCommand.singleInsertValue(priceTableKiotVietTableModel);
			// System.out.println("Result " + result);
			// }

			// insert multi responseData with batch insert
			List<PriceTableKiotVietTableEntity> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableEntity>();
			for (Object row : tableRow) {
				PriceTableKiotVietTableEntity priceTableKiotVietTableModel = new PriceTableKiotVietTableEntity();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}
			String result = sqlInsertCommand.batchInsertValues(kiotVietTableModels, true);
			logger.info("ExcelImportSimple.importToDatabase() Result {}", result);
		}

		logger.info("ExcelImportSimple.importToDatabase() end");
	}

	/**
	 * 
	 * @param batchSize
	 */
	public void importToDatabase(int batchSize) {

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) start");

		List<PriceTableKiotVietImportDataModel> models = List.of(new PriceTableKiotVietImportDataModel());

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) starting get responseData from excel");
		Workbook workbook = createWorkBook(pathFile, false);
		TableExcelReader excelReader = new DefaultTableExcelReader(workbook, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info("ExcelImportSimple.importToDatabase() starting import responseData from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableEntity> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableEntity>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get responseData from excel file
		for (List<Object> tableRow : datas) {
			// insert multi responseData with batch insert
			List<PriceTableKiotVietTableEntity> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableEntity>();

			for (Object row : tableRow) {
				PriceTableKiotVietTableEntity priceTableKiotVietTableModel = new PriceTableKiotVietTableEntity();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}

			// Paging
			List<List<PriceTableKiotVietTableEntity>> pagingList = paging(kiotVietTableModels, batchSize);

			for (List<PriceTableKiotVietTableEntity> list : pagingList) {
				String result = sqlInsertCommand.batchInsertValues(list, true);
				logger.info("ExcelImportSimple.importToDatabase(int batchSize) Result {}", result);
			}

		}

		logger.info("ExcelImportSimple.importToDatabase(int batchSize) end");
	}

	/**
	 * Using SAXParserTableExcelReader.class
	 * 
	 * @param batchSize
	 */
	public void importBigDataToDatabase(int batchSize) {

		logger.info("ExcelImportSimple.importBigDataToDatabase(int batchSize) start");

		List<PriceTableKiotVietImportDataModel> models = List.of(new PriceTableKiotVietImportDataModel());

		logger.info("ExcelImportSimple.importBigDataToDatabase(int batchSize) starting get responseData from excel");
		TableExcelReader excelReader = new SAXParserTableExcelReader(pathFile, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info("ExcelImportSimple.importBigDataToDatabase() starting import responseData from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableEntity> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableEntity>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get responseData from excel file
		for (List<Object> tableRow : datas) {
			// insert multi responseData with batch insert
			List<PriceTableKiotVietTableEntity> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableEntity>();

			for (Object row : tableRow) {
				PriceTableKiotVietTableEntity priceTableKiotVietTableModel = new PriceTableKiotVietTableEntity();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}

			// Paging
			List<List<PriceTableKiotVietTableEntity>> pagingList = paging(kiotVietTableModels, batchSize);

			for (List<PriceTableKiotVietTableEntity> list : pagingList) {
				String result = sqlInsertCommand.batchInsertValues(list, true);
				logger.info("ExcelImportSimple.importBigDataToDatabase(int batchSize) Result {}", result);
			}

		}

		logger.info("ExcelImportSimple.importBigDataToDatabase(int batchSize) end");
	}

	/**
	 * {@link SXSSFWorkbook} does not support reading.
	 * </p>
	 * refer:
	 * https://stackoverflow.com/questions/12513981/reading-data-from-xlsx-with-apache-pois-sxssfsheet
	 */
	@Deprecated
	public void streamingImportToDatabase(int batchSize) {
		logger.info("ExcelImportSimple.streamingImportToDatabase(int batchSize) start");

		List<PriceTableKiotVietImportDataModel> models = List.of(new PriceTableKiotVietImportDataModel());

		logger.info("ExcelImportSimple.streamingImportToDatabase(int batchSize) starting get responseData from excel");
		Workbook workbook = createWorkBook(pathFile, true);
		TableExcelReader excelReader = new DefaultTableExcelReader(workbook, models);
		List<List<Object>> datas = excelReader.executeImport();

		logger.info(
				"ExcelImportSimple.streamingImportToDatabase() starting import responseData from excel to database");
		// import to database
		SqlInsert<PriceTableKiotVietTableEntity> sqlInsertCommand = new SimplePrepareStatementSqlInsert<PriceTableKiotVietTableEntity>(
				datasource, TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
		// Get responseData from excel file
		for (List<Object> tableRow : datas) {
			// insert multi responseData with batch insert
			List<PriceTableKiotVietTableEntity> kiotVietTableModels = new LinkedList<PriceTableKiotVietTableEntity>();

			for (Object row : tableRow) {
				PriceTableKiotVietTableEntity priceTableKiotVietTableModel = new PriceTableKiotVietTableEntity();
				priceTableKiotVietTableModel = ObjectUtils.updateProperties(priceTableKiotVietTableModel, row);
				kiotVietTableModels.add(priceTableKiotVietTableModel);
			}

			// Paging
			List<List<PriceTableKiotVietTableEntity>> pagingList = paging(kiotVietTableModels, batchSize);

			for (List<PriceTableKiotVietTableEntity> list : pagingList) {
				String result = sqlInsertCommand.batchInsertValues(list, true);
				logger.info("ExcelImportSimple.streamingImportToDatabase(int batchSize) Result {}", result);
			}

		}

		logger.info("ExcelImportSimple.streamingImportToDatabase(int batchSize) end");
	}
}
