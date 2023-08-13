package excel.reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelBatchIterator implements ExcelIterator<Row> {

	private int batchSize = DEFAULT_BATCH;

	private Sheet sheet;

	private int currentRowIndex;

	private int totalRows;

	public ExcelBatchIterator(Sheet sheet, int batchSize) {
		this.batchSize = batchSize;
		this.sheet = sheet;
		this.currentRowIndex = 0;
		this.totalRows = sheet.getLastRowNum() + 1;
	}

	@Override
	public List<Row> batchNext() {
		if (!hasBatchNext()) {
			throw new NoSuchElementException("No more rows to iterate.");
		}
		List<Row> nextBatch = new ArrayList<>();
		for (int start = currentRowIndex; start < totalRows; start++) {
			nextBatch.add(sheet.getRow(start));
			if (currentRowIndex > 0 && currentRowIndex++ % batchSize == 0) {
				break;
			}
			currentRowIndex++;
		}
		return nextBatch;
	}

	@Override
	public boolean hasBatchNext() {
		return currentRowIndex < totalRows;
	}
}
