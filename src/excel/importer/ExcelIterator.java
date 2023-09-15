package excel.importer;

import java.util.List;

public interface ExcelIterator<E> {

	public final Integer DEFAULT_BATCH = 10000;

	List<E> batchNext();

	boolean hasBatchNext();
}
