package input.validation.validate;

import input.validation.exception.ErrorDetail;

public class NumberValidation<T> extends TypeStringDataValidation<T> {

	private static final String NUMBER_PATTERN = "^-?[1-9]\\d*(\\.\\d+)?$";

	public NumberValidation(String pattern) {
		super(pattern);
	}

	@Override
	public boolean isValid(T data) {
		
		if (!super.isValid(data))
			return false;
		
		if (!data.toString().matches(NUMBER_PATTERN)) {
			return false;
		}

		if (pattern == null || (pattern != null && pattern.isBlank())) {
			return true;
		}

		return data.toString().matches(pattern);
	}

	public ErrorDetail getErrorCause() {
		return new ErrorDetail(this.getClass().getCanonicalName(), "Not matching pattern " + (pattern.equals("") ? NUMBER_PATTERN : pattern));
	}

}
