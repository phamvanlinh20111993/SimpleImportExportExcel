package input.validation.validate;

import input.validation.exception.ErrorDetail;

public abstract class TypeStringDataValidation<T> implements ValidationHandler<T> {

	protected String pattern;

	public TypeStringDataValidation(String pattern) {
		this.pattern = pattern;
	}

	public boolean isValid(T value) {
		if (value == null || !(value instanceof String)) {
			return false;
		}

		return true;
	}

	public ErrorDetail getErrorCause() {
		return new ErrorDetail(this.getClass().toString(), "Not matching pattern " + pattern);
	}
}
