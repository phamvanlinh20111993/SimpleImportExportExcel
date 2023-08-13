package input.validation.validate;

import input.validation.exception.ErrorDetail;
import input.validation.exception.ValidationError;

public interface ValidationHandler<T> {
	
	public boolean isValid(T value);

	public ErrorDetail getErrorCause();
	
//	public void validate(T value) throws ValidationError;
}
