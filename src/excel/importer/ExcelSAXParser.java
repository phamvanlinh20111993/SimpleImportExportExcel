package excel.importer;

import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExcelSAXParser extends DefaultHandler {

	private SharedStringsTable sharedStringsTable;
	private StylesTable stylesTable;
	private String currentCellValue;
	private boolean isCellOpen;

	public void parseExcelFile(String filePath) throws Exception {
		OPCPackage opcPackage = OPCPackage.open(filePath);
		XSSFReader xssfReader = new XSSFReader(opcPackage);
		sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();
		stylesTable = xssfReader.getStylesTable();

		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setContentHandler(this);

		InputStream sheetInputStream = xssfReader.getSheet("rId1"); // Assuming the first sheet
		InputSource inputSource = new InputSource(sheetInputStream);

		xmlReader.parse(inputSource);

		sheetInputStream.close();
		opcPackage.close();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (name.equals("c")) {
			String cellType = attributes.getValue("t");
			isCellOpen = true;
			currentCellValue = "";

			// Handle cell types as needed
			if (cellType != null && cellType.equals("s")) {
				// Cell contains a shared string, resolve it
				int sharedStringIndex = Integer.parseInt(attributes.getValue("v"));
				currentCellValue = new XSSFRichTextString(sharedStringsTable.getItemAt(sharedStringIndex).getString())
						.toString();
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (isCellOpen) {
			currentCellValue += new String(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (isCellOpen && name.equals("c")) {
			// Process the cell value
			System.out.println(currentCellValue);

			isCellOpen = false;
			currentCellValue = "";
		}
	}

	public static void main(String[] args) {
		String filePath = "path/to/your/excel/file.xlsx";

		try {
			ExcelSAXParser parser = new ExcelSAXParser();
			parser.parseExcelFile(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
