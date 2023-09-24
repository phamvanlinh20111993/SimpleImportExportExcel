package excel.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author PhamLinh refer:
 *         https://howtodoinjava.com/java/library/poi-read-excel-with-sax-parser/
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExcelSAXParser extends DefaultHandler {

	private static final Logger logger = LoggerFactory.getLogger(ExcelSAXParser.class);

	protected Map<String, String> header = new HashMap<>();
	protected Map<String, String> rowValues = new HashMap<>();
	private SharedStringsTable sharedStringsTable;

	protected List<Map<String, String>> data = new LinkedList<>();

	protected long rowNumber = 0;
	protected String cellId;
	private String contents;
	private boolean isCellValue;
	private boolean fromSST;

	protected static String getColumnId(String attribute) throws SAXException {
		for (int i = 0; i < attribute.length(); i++) {
			if (!Character.isAlphabetic(attribute.charAt(i))) {
				return attribute.substring(0, i);
			}
		}
		throw new SAXException("Invalid format " + attribute);
	}

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

	@Override
	public void characters(char[] ch, int start, int length) {
		if (isCellValue) {
			contents += new String(ch, start, length);
		}
	}

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
			header.clear();
			if (rowNumber == 1) {
				header.putAll(rowValues);
			}
			try {
				processRow();
			} catch (ExecutionException | InterruptedException e) {
				logger.error("ExcelSAXParser.endElement() {}", e.getMessage());
			}
			rowValues.clear();
		}
	}

	protected void processRow() throws ExecutionException, InterruptedException {
		if (rowNumber == 1 && !header.isEmpty()) {
			System.out.println("The header values are at line no. " + rowNumber + " " + "are :" + header);
		} else if (rowNumber > 1 && !rowValues.isEmpty()) {
			data.add(rowValues);
			// Get specific values here
			/*
			 * String a = rowValues.get("A"); String b = rowValues.get("B");
			 */
			// Print whole row
			System.out.println("The row values are at line no. " + rowNumber + " are :" + rowValues);
		}
	}

	public void readExcelFile(File file)
			throws ParserConfigurationException, SAXException, IOException, OpenXML4JException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		try (OPCPackage opcPackage = OPCPackage.open(file)) {
			XSSFReader xssfReader = new XSSFReader(opcPackage);
			sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();

			System.out.println(sharedStringsTable.getUniqueCount());

			Iterator<InputStream> sheets = xssfReader.getSheetsData();

			if (sheets instanceof XSSFReader.SheetIterator) {
				SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();
				while (sheetIterator.hasNext()) {
					saxParser.parse(sheetIterator.next(), this);
				}
			}
		}
	}

	public static void main(String[] args) {
		String filePath = "C:\\Users\\DELL\\eclipse-workspace\\SimpleImportExportExcel\\BangGia_KV14082023-213536-263.xlsx";

		try {
			ExcelSAXParser parser = new ExcelSAXParser();
			parser.readExcelFile(new File(filePath));
			System.out.println(parser.getData().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
