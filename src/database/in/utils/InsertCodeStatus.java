package database.in.utils;

public enum InsertCodeStatus {
	SUCCESS("Success Inserted "),
	FAIL("Fail Inserted"),
	SUCCESS_FORCE_INSERT("Success with Batch Inserted"),
	FAIL_FORCE_INSERT("Fail with Batch Inserted");
	
	private String codeValue;

	private InsertCodeStatus(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getTypeValue() {
		return this.codeValue;
	}
}
