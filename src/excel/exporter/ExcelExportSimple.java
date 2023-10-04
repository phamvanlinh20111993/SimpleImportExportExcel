package excel.exporter;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import excel.exporter.handle.SimpleMultiSheetTableExcelExporter;
import excel.exporter.handle.SimpleSingleSheetTableExcelExporter;
import excel.exporter.handle.TableExcelExporter;
import lombok.Data;
import utils.ExcelType;

@Data
public class ExcelExportSimple {

	private List<List<?>> dataSheets;

	private String outputPath;

	private String fileName;

	public ExcelExportSimple() {
	}

	public ExcelExportSimple(List<List<?>> dataSheets, String outputPath, String fileName) {
		this.dataSheets = dataSheets;
		this.outputPath = outputPath;
		this.fileName = fileName;

	}

	/**
	 * 
	 * @param excelType
	 */
	public void export() {
		Logger logger = LoggerFactory.getLogger(ExcelExportSimple.class);
		validateInput();
		TableExcelExporter excelExporter = dataSheets.size() == 1
				? new SimpleSingleSheetTableExcelExporter<>(fileName, dataSheets.get(0))
				: new SimpleMultiSheetTableExcelExporter(fileName, dataSheets);

		try {
			excelExporter.out(outputPath);
		} catch (IOException e) {
			logger.error("ExcelExportSimple.export() {}", e.getMessage());
		}
	}

	/**
	 * 
	 * @param excelType
	 */
	public void export(ExcelType excelType) {
		Logger logger = LoggerFactory.getLogger(ExcelExportSimple.class);
		validateInput();
		TableExcelExporter excelExporter = dataSheets.size() == 1
				? new SimpleSingleSheetTableExcelExporter<>(fileName, dataSheets.get(0), excelType)
				: new SimpleMultiSheetTableExcelExporter(fileName, dataSheets, excelType);

		try {
			excelExporter.out(outputPath);
		} catch (IOException e) {
			logger.error("ExcelExportSimple.export() {}", e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	protected void validateInput() {

		if (StringUtils.isAnyBlank(outputPath)) {
			throw new NullPointerException("outputPath can not be null or blank");
		}

		if (StringUtils.isAnyBlank(fileName)) {
			throw new NullPointerException("fileName can not be null or blank");
		}

		if (ObjectUtils.allNull(dataSheets)) {
			throw new NullPointerException("dataSheets can not be null");
		}
	}

}
