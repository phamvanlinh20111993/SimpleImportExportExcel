package input.validation.validate;

public class StringTypeValidation<T> extends TypeStringDataValidation<T> {

	public StringTypeValidation(String pattern) {
		super(pattern);
	}

	public boolean isValid(T data) {

		if (!super.isValid(data))
			return false;

		if (pattern != null && pattern.length() > 0) {
			return data.toString().matches(pattern);
		}

		return true;
	}

}
