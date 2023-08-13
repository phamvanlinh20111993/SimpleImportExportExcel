package input.validation.validate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import input.validation.exception.ErrorDetail;

public class DateTypeValidation<T> extends TypeStringDataValidation<T> {

	public DateTypeValidation(String pattern) {
		super(pattern);
	}

	@Override
	public boolean isValid(T data) {

		if (!super.isValid(data))
			return false;

		DateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		try {
			sdf.parse(data.toString());
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	@Override
	public ErrorDetail getErrorCause() {
		return new ErrorDetail(DateTypeValidation.class.toString(), "Not matching pattern " + pattern);
	}

}
