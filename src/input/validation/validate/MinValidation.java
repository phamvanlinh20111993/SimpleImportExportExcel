package input.validation.validate;

import java.util.Collection;
import java.util.Map;

import input.validation.exception.ErrorDetail;

public class MinValidation<T> implements ValidationHandler<T> {

	private long minVal;

	public MinValidation(long minVal) {
		this.minVal = minVal;
	}

	@Override
	public boolean isValid(T value) {

		if (value == null)
			return false;

		if (value instanceof Long) {
			return ((Long) value).longValue() >= minVal;
		}

		if (value instanceof Float) {
			return ((Float) value).floatValue() >= minVal;
		}

		if (value instanceof Double) {
			return ((Double) value).doubleValue() >= minVal;
		}

		if (value instanceof Integer) {
			return ((Integer) value).intValue() >= minVal;
		}

		if (value instanceof String) {
			return ((String) value).length() >= minVal;
		}

		if (value instanceof Collection<?>) {
			return ((Collection<?>) value).size() >= minVal;
		}

		if (value instanceof Map<?, ?>) {
			return ((Map<?, ?>) value).size() >= minVal;
		}

		return false;
	}

	@Override
	public ErrorDetail getErrorCause() {
		return new ErrorDetail(MinValidation.class.getCanonicalName(), "The min length must be " + minVal);
	}
}
