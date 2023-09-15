package input.validation.validate;

import java.util.Collection;
import java.util.Map;

import input.validation.exception.ErrorDetail;
import input.validation.exception.ValidationError;

public class MaxValidation<T> implements ValidationHandler<T> {

	private long maxValue;

	public MaxValidation(long maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public boolean isValid(T value) {

		if (value == null)
			return false;

		if (value instanceof Long) {
			return ((Long) value).longValue() <= maxValue;
		}

		if (value instanceof Float) {
			return ((Float) value).floatValue() <= maxValue;
		}

		if (value instanceof Double) {
			return ((Double) value).doubleValue() <= maxValue;
		}

		if (value instanceof Integer) {
			return ((Integer) value).intValue() <= maxValue;
		}

		if (value instanceof String) {
			return ((String) value).length() <= maxValue;
		}

		if (value instanceof Collection<?>) {
			return ((Collection<?>) value).size() <= maxValue;
		}

		if (value instanceof Map<?, ?>) {
			return ((Map<?, ?>) value).size() <= maxValue;
		}

		return false;
	}

	@Override
	public ErrorDetail getErrorCause() {
		return new ErrorDetail(MinValidation.class.getCanonicalName(), "The max length must be " + maxValue);
	}

	// @Override
	// public void validate(T value) throws ValidationError {
	// if(!this.isValid(value)) throw new ValidationError(MaxValidation.class, "The
	// max value only " + maxValue);
	// }

}
