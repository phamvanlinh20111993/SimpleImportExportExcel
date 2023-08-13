package input.validation.validate;

import input.validation.exception.ErrorDetail;

public class NotNullValidation<T> implements ValidationHandler<T> {

	public NotNullValidation() {

	}

	@Override
	public boolean isValid(T value) {
		return value != null;
	}

	@Override
	public ErrorDetail getErrorCause() {
		return new ErrorDetail(NotNullValidation.class.getCanonicalName(), "can not be null");
	}
}
