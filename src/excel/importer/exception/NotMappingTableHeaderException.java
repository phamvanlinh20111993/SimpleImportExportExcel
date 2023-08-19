package excel.importer.exception;

public class NotMappingTableHeaderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public NotMappingTableHeaderException() {
		super();
	}

	public NotMappingTableHeaderException(String message) {
		super();
		this.message = message;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
