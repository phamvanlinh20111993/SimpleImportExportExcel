package database.in.exception;

public class AnnotationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String anotationType;

	private String message;

	public AnnotationNotFoundException(String anotationType, String message) {
		super();
		this.anotationType = anotationType;
		this.message = message;
	}

	public String getAnotationType() {
		return anotationType;
	}

	public void setAnotationType(String anotationType) {
		this.anotationType = anotationType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
