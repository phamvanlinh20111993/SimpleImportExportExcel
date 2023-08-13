package database.in.utils;

/**
 * 
 * @author PhamLinh
 * refer #{@link java.sql.Connection}
 */
public enum TransactionIsolationLevel {
	TRANSACTION_NONE(0), 
	TRANSACTION_READ_UNCOMMITTED(1), 
	TRANSACTION_READ_COMMITTED(2), 
	TRANSACTION_REPEATABLE_READ(4), 
	TRANSACTION_SERIALIZABLE(8);

	private Integer typeValue;

	private TransactionIsolationLevel(Integer typeValue) {
		this.typeValue = typeValue;
	}

	public Integer getTypeValue() {
		return this.typeValue;
	}
}
