package input.validation.main;

import java.util.Arrays;

import input.validation.exception.ErrorDetail;
import input.validation.handle.SimpleHandleRowData;
import input.validation.handle.RowDataHandle;

public class Main {

	public static void main(String[] args) {
		DataModel data = new DataModel();

		data.setDate("30-22-10212");
		data.setIntegerNumber("1234234324324");
		data.setMatchingPattern("hehe");
		data.setLength("2e02343242343");
		data.setMin("20");
		data.setMax("210");

		DataModel data1 = new DataModel();
		data1.setDate("q30-22-10212");
		data1.setIntegerNumber("1234234324324");
		data1.setMatchingPattern("hehe");
		data1.setLength("2e02343242343");
		data1.setMin("20");
		data1.setMax("210");
		data.setData(Arrays.asList(data1));

		RowDataHandle<DataModel> rowDataHandle = new SimpleHandleRowData<DataModel>(data);
		rowDataHandle.validateColumns();

		for (ErrorDetail err : rowDataHandle.getErrorDetails()) {
			System.out.println(err.toString());
		}
	}

}
