package excel.importer.handle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author PhamLinh refer:
 *         https://howtodoinjava.com/java/library/poi-read-excel-with-sax-parser/
 */
public class RawTableExcelSAXParserReader extends DefaultHandler {

	private static final Logger logger = LoggerFactory.getLogger(RawTableExcelSAXParserReader.class);

	private Map<String, String> rowValues = new LinkedHashMap<>();

	private Map<String, String> header = new LinkedHashMap<>();

	private List<Map<String, String>> reponseAtSheet = new LinkedList<>();

	private SharedStringsTable sharedStringsTable;

	private List<SAXTableData> saxTableDatas = new LinkedList<>();

	protected long rowNumber = 0;
	protected String cellId;
	private String contents;
	private boolean isCellValue;
	private boolean fromSST;

	/**
	 * 
	 * @param attribute
	 * @return
	 * @throws SAXException
	 */
	protected static String getColumnId(String attribute) throws SAXException {
		for (int i = 0; i < attribute.length(); i++) {
			if (!Character.isAlphabetic(attribute.charAt(i))) {
				return attribute.substring(0, i);
			}
		}
		logger.error("Error at RawTableExcelSAXParserReader.getColumnId(): {}", attribute);
		throw new SAXException("Invalid format " + attribute);
	}

	/**
	 * 
	 */
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		// Clear contents cache
		contents = "";
		// element row represents Row
		switch (name) {
		case "row":
			String rowNumStr = attributes.getValue("r");
			rowNumber = Long.parseLong(rowNumStr);
			// element c represents Cell
		case "c":
			cellId = getColumnId(attributes.getValue("r"));
			// attribute t represents the cell type
			String cellType = attributes.getValue("t");
			if (cellType != null && cellType.equals("s")) {
				// cell type s means value will be extracted from SharedStringsTable
				fromSST = true;
			}
			// element v represents value of Cell
		case "v":
			isCellValue = true;
		}
	}

	/**
	 * 
	 */
	@Override
	public void characters(char[] ch, int start, int length) {
		if (isCellValue) {
			contents += new String(ch, start, length);
		}
	}

	/**
	 * 
	 */
	@Override
	public void endElement(String uri, String localName, String name) {
		if (isCellValue && fromSST) {
			int index = Integer.parseInt(contents);
			contents = new XSSFRichTextString(sharedStringsTable.getItemAt(index).getString()).toString();
			rowValues.put(cellId, contents);
			cellId = null;
			isCellValue = false;
			fromSST = false;
		} else if (isCellValue) {
			rowValues.put(cellId, contents);
			isCellValue = false;
		} else if (name.equals("row")) {
			if (rowNumber == 1) {
				header.putAll(rowValues);
			}
			try {
				processRow();
			} catch (ExecutionException | InterruptedException e) {
				logger.error("RawTableExcelSAXParserReader.endElement() {}", e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	protected void processRow() throws ExecutionException, InterruptedException {
		if (rowNumber == 1 && !header.isEmpty()) {
			System.out.println("The header values are at line no. " + rowNumber + " " + "are :" + header);
		} else if (rowNumber > 1 && !rowValues.isEmpty()) {
			reponseAtSheet.add(cloneHeaderData(rowValues));
			// Get specific values here
			/*
			 * String a = rowValues.get("A"); String b = rowValues.get("B");
			 */
			// Print whole row
			System.out.println("The row values are at line no. " + rowNumber + " are :" + rowValues);
			rowValues.clear();
		}
	}

	/**
	 * 
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws OpenXML4JException
	 */
	public void readExcelFile(File file)
			throws ParserConfigurationException, SAXException, IOException, OpenXML4JException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		try (OPCPackage opcPackage = OPCPackage.open(file)) {
			XSSFReader xssfReader = new XSSFReader(opcPackage);
			sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();
			// System.out.println(sharedStringsTable.getUniqueCount());
			Iterator<InputStream> sheets = xssfReader.getSheetsData();

			if (sheets instanceof XSSFReader.SheetIterator) {
				SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();
				header = new LinkedHashMap<>();
				reponseAtSheet = new LinkedList<>();
				while (sheetIterator.hasNext()) {
					saxParser.parse(sheetIterator.next(), this);
					saxTableDatas.add(new SAXTableData(cloneHeaderData(header), cloneListRowData(reponseAtSheet)));
					header.clear();
					reponseAtSheet.clear();
				}
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param sheetIndex
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws OpenXML4JException
	 */
	public void readExcelFile(File file, int sheetIndex)
			throws ParserConfigurationException, SAXException, IOException, OpenXML4JException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		try (OPCPackage opcPackage = OPCPackage.open(file)) {
			XSSFReader xssfReader = new XSSFReader(opcPackage);
			sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();

			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			if (sheets instanceof XSSFReader.SheetIterator) {
				SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();
				header = new LinkedHashMap<>();
				reponseAtSheet = new LinkedList<>();

				int index = 0;
				while (sheetIterator.hasNext()) {
					if (index++ == sheetIndex) {
						saxParser.parse(sheetIterator.next(), this);
						saxTableDatas.add(new SAXTableData(header, reponseAtSheet));
						header.clear();
						reponseAtSheet.clear();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param sheetIndexes
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws OpenXML4JException
	 */
	public void readExcelFile(File file, List<Integer> sheetIndexes)
			throws ParserConfigurationException, SAXException, IOException, OpenXML4JException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		try (OPCPackage opcPackage = OPCPackage.open(file)) {
			XSSFReader xssfReader = new XSSFReader(opcPackage);
			sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();

			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			if (sheets instanceof XSSFReader.SheetIterator) {
				SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();
				header = new LinkedHashMap<>();
				reponseAtSheet = new LinkedList<>();

				int index = 0;
				while (sheetIterator.hasNext()) {
					if (sheetIndexes.contains(index++)) {
						saxParser.parse(sheetIterator.next(), this);
						saxTableDatas.add(new SAXTableData(header, reponseAtSheet));
						header.clear();
						reponseAtSheet.clear();
					}
				}
			}
		}
	}

	private Map<String, String> cloneHeaderData(Map<String, String> original) {

		if (original == null) {
			return new LinkedHashMap<>();
		}

		return original.entrySet().stream()
				// perform customization
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> {
					throw new IllegalStateException("Can not mapping cloneMap()");
				}, LinkedHashMap::new));

	}

	private List<Map<String, String>> cloneListRowData(List<Map<String, String>> original) {

		if (original == null) {
			return new LinkedList<>();
		}

		return original.stream().map(v -> v).collect(Collectors.toCollection(LinkedList::new));
	}

	/**
	 * 
	 * @return
	 */
	public List<SAXTableData> getSaxTableData() {
		return saxTableDatas;
	}

}
