package database.in.handle;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.in.utils.TransactionIsolationLevel;

/**
 * 
 * @author PhamLinh
 *
 * @param <R>
 */
public abstract class AbstractSqlInsert<R> implements SqlInsert<R> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractSqlInsert.class);

	protected DataSource dataSource;

	protected TransactionIsolationLevel transactionIsolationLevel = TransactionIsolationLevel.TRANSACTION_READ_COMMITTED;
	
	/**
	 * 
	 * @param autoCommit
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(boolean autoCommit) throws SQLException {
		if (dataSource == null) {
			throw new NullPointerException("Datasource can not be null");
		}

		Connection conn = dataSource.getConnection();
		conn.setAutoCommit(autoCommit);
		return conn;
	}
	
	/**
	 * 
	 * @param entity
	 * @param statement
	 * @throws SQLException
	 */
	protected void extractEntity(R entity, PreparedStatement statement) throws SQLException {
		int index = 1;
		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object valueObject = field.get(entity);
				setPrepareStatement(statement, valueObject, index++);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("AbstractSqlInsert.extractEntity(): {}", e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param statement
	 * @param value
	 * @param index
	 * @throws SQLException
	 */
	protected void setPrepareStatement(PreparedStatement statement, Object value, int index) throws SQLException {
		
		if(value == null) {
			statement.setNull(index, Types.NULL);
		}
		
		if (value instanceof String) {
			statement.setString(index, value.toString());
			return;
		}

		if (value instanceof Integer) {
			statement.setInt(index, (int) value);
			return;
		}

		if (value instanceof Long) {
			statement.setLong(index, (long) value);
			return;
		}

		if (value instanceof Double) {
			statement.setDouble(index, (Double) value);
			return;
		}

		if (value instanceof Float) {
			statement.setFloat(index, (float) value);
			return;
		}

		if (value instanceof BigDecimal) {
			statement.setBigDecimal(index, (BigDecimal) value);
			return;
		}

		// TODO ..
	}

}
