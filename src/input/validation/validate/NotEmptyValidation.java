package input.validation.validate;

import java.util.Collection;
import java.util.Map;

import input.validation.exception.ErrorDetail;

public class NotEmptyValidation<T> implements ValidationHandler<T> {

	@Override
	public boolean isValid(T data) {

		if (data == null)
			return false;

		if (data instanceof String) {
			return ((String) data).length() > 0;
		}

		if (data instanceof Collection<?>) {
			return ((Collection<?>) data).size() > 0;
		}

		if (data instanceof Map<?, ?>) {
			return ((Map<?, ?>) data).size() > 0;
		}

		return false;
	}

	@Override
	public ErrorDetail getErrorCause() {
		ErrorDetail err = new ErrorDetail(StringTypeValidation.class.getName(), "can not be empty");
		return err;
	}
}
