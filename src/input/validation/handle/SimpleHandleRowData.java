package input.validation.handle;

import java.util.ArrayList;
import java.util.List;

import input.validation.exception.ErrorDetail;
import input.validation.validate.ValidationHandler;

public class SimpleHandleRowData<R> extends SimpleAbstractHandleRowData<R> {
	
	protected List<ErrorDetail> errors;

	public SimpleHandleRowData(R rowData) {
		super(rowData);
		errors = new ArrayList<>();
	}
	
	public void validateColumnData(ValidationHandler<Object> handleError, Object value) {
		if (!handleError.isValid(value)) {
			ErrorDetail err = handleError.getErrorCause();
			err.setValue(value);
			errors.add(err);
		}
	}
	
	public List<ErrorDetail> getErrorDetails() {
		return errors;
	}

	public boolean isRowDataError() {
		return this.errors.size() == 0;
	}


}
