package input.validation.handle;

import java.util.List;
import input.validation.exception.ErrorDetail;

public interface RowDataHandle<R> {
	
	public boolean isRowDataError();
	
	public void validateColumns();
	
	public List<ErrorDetail> getErrorDetails();
}
