package input.validation.exception;

public class ErrorDetail {

	private Object value;

	private String errorType;

	private String cause;

	public ErrorDetail(String errorType, String cause) {
		super();
		this.errorType = errorType;
		this.cause = cause;
	}

	public ErrorDetail(String errorType, String cause, Object value) {
		super();
		this.errorType = errorType;
		this.cause = cause;
		this.value = value;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ErrorDetail [errorType=" + errorType + ", cause=" + cause + ", value=" + value.toString() + "]";
	}

}
