package excel.importer.handle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import excel.importer.exception.HeaderColumnException;
import excel.importer.exception.NotMappingTableHeaderException;
import excel.importer.utils.CellType;
import input.validation.handle.RowDataHandle;
import input.validation.handle.SimpleHandleRowData;

public class SAXParserTableExcelReader extends AbstractTableExcelReader {

	private static final Logger logger = LoggerFactory.getLogger(SAXParserTableExcelReader.class);

	private String pathFile;

	protected RowDataHandle<?> rowDataHandle = new SimpleHandleRowData<>();
	
	public SAXParserTableExcelReader(String pathFile, List<?> objectTemplates) {
		super(objectTemplates);
		this.pathFile = pathFile;
	}
	
	/**
	 * 
	 * @param saxTableData
	 * @return
	 */
	protected Map<String, Integer> readHeaderFromSheet(SaxTableData saxTableData) {

		Map<String, Integer> headerFields = new LinkedHashMap<>();

		Map<String, String> headers = saxTableData.getHeader();

		for (Map.Entry<String, String> cell : headers.entrySet()) {
			headerFields.put(cell.getValue(), convertAttributeToIndex(cell.getKey()));
		}
		return headerFields;
	}

	protected List<Map<String, Object>> readBodyFromSheet(SaxTableData saxTableData,
			Map<String, CellType> configHeader) {

		logger.info("SAXParserTableExcelReader.readBodyFromSheet() start ");

		Map<String, Integer> headers = this.readHeaderFromSheet(saxTableData);

		Map<Integer, String> invertheader = new LinkedHashMap<>();

		for (Map.Entry<String, Integer> header : headers.entrySet()) {
			invertheader.put(header.getValue(), header.getKey());
		}

		List<Map<String, Object>> rowDatas = new ArrayList<>();

		for (Map<String, String> row : saxTableData.getRowDatas()) {
			Map<String, Object> rowData = new LinkedHashMap<>();
			for (Map.Entry<String, String> cellStr : row.entrySet()) {
				CellType cellTypeCustom = configHeader.get(invertheader.get(convertAttributeToIndex(cellStr.getKey())));
				Optional<?> data = toDataType(cellStr.getValue(), cellTypeCustom);
				if (data.isPresent()) {
					rowData.put(invertheader.get(convertAttributeToIndex(cellStr.getKey())), data.get());
				}
			}
			rowDatas.add(rowData);
		}

		logger.info("SAXParserTableExcelReader.readBodyFromSheet() end ");

		return rowDatas;
	}

	protected void validateHeader(SaxTableData saxTableData, boolean isStrictFormat,
			Map<String, CellType> configHeader) {

		Map<String, Integer> headers = this.readHeaderFromSheet(saxTableData);

		if (headers.size() != configHeader.size()) {
			throw new NotMappingTableHeaderException(" is not mapping header size");
		}

		Set<String> notStrictconfigHeader = configHeader.keySet().stream().map(v -> v.toLowerCase())
				.collect(Collectors.toSet());

		for (String headerVal : headers.keySet()) {
			if (!notStrictconfigHeader.contains(headerVal.toLowerCase())) {
				throw new NotMappingTableHeaderException(headerVal + " is not exist in your config header");
			}
		}

		if (isStrictFormat) {
			for (String headerVal : headers.keySet()) {
				if (!configHeader.keySet().contains(headerVal)) {
					throw new NotMappingTableHeaderException(headerVal + " is not valid format");
				}
			}

			List<Integer> indexs = new LinkedList<Integer>();
			for (Integer index : headers.values()) {
				indexs.add(index);
			}

			int index = indexs.get(0);
			for (int ind = 0; ind < indexs.size(); ind++) {
				if (index++ != indexs.get(ind)) {
					throw new HeaderColumnException("Not valid index at column header index " + index);
				}
			}
		}
	}

	/**
	 * convert Attribute from excel to index. For example:
	 * <p/>
	 * A -> 1
	 * <p/>
	 * B -> 2
	 * <p/>
	 * C -> 3
	 * <p/>
	 * ...
	 * <p/>
	 * AA -> 27
	 * <p/>
	 * AB -> 28
	 * <p/>
	 * AAA -> 677 ...
	 * 
	 * @param attribute
	 * @return
	 */
	private Integer convertAttributeToIndex(String attribute) {
		if (StringUtils.isAllEmpty(attribute)) {
			return 1;
		}
		if (attribute.length() > 6) {
			throw new RuntimeException("the attribute is too high");
		}

		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Integer res = 0, attL = attribute.length();
		for (int pos = 0; pos < attL; pos++) {
			Character character = attribute.charAt(pos);
			for (int ind = 0; ind < letters.length(); ind++) {
				if (character == letters.charAt(ind)) {
					res += (int) Math.pow(26, attL - 1 - pos) * (ind + 1);
					break;
				}
			}
		}

		return res;
	}

	@Override
	public List<List<Object>> executeImport() {

		logger.info("SAXParserTableExcelReader.executeImport() start");

		List<List<Object>> res = new LinkedList<List<Object>>();
		List<Map<String, CellType>> configHeadersMap = this.configHeaders();
		try {
			RawTableExcelSAXParserReader rawTableExcelSAXParserReader = new RawTableExcelSAXParserReader();
			rawTableExcelSAXParserReader.readExcelFile(new File(pathFile));

			List<SaxTableData> saxTableDatas = rawTableExcelSAXParserReader.getSaxTableData();

			for (int ind = 0; ind < saxTableDatas.size(); ind++) {
				validateHeader(saxTableDatas.get(ind), true, configHeadersMap.get(ind));
				List<Map<String, Object>> data = this.readBodyFromSheet(saxTableDatas.get(ind),
						configHeadersMap.get(ind));
				List<Object> lists = new LinkedList<>();
				for (Map<String, Object> prop : data) {
					Optional<Object> realObj = toObject(objectTemplates.get(ind).getClass(), prop);
					if (realObj.isPresent()) {
						rowDataHandle = new SimpleHandleRowData<>(realObj.get());
						rowDataHandle.validateColumns();
						if (rowDataHandle.isRowDataError()) {
							logger.error("Error DefaultTableExcelReader.executeImport() {}",
									rowDataHandle.getErrorDetails());
							logger.error("Error DefaultTableExcelReader.executeImport() {}", realObj.get());
							continue;
						}

						lists.add(realObj.get());
					}
				}

				res.add(lists);

			}

		} catch (ParserConfigurationException | SAXException | IOException | OpenXML4JException
				| NullPointerException e) {
			logger.error("Error SAXParserTableExcelReader.executeImport(): {}", e.getMessage());
		}

		logger.info("SAXParserTableExcelReader.executeImport() end");

		return res;
	}

}
