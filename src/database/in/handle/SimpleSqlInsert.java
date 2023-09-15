package database.in.handle;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;

import database.in.utils.TransactionIsolationLevel;
import database.in.utils.Utils;

public class SimpleSqlInsert<T> extends AbstractSqlInsert<T> {

	public SimpleSqlInsert(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public SimpleSqlInsert(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel) {
		super();
		this.dataSource = dataSource;
		this.transactionIsolationLevel = transactionIsolationLevel;
	}

	@Override
	public String singleInsertValue(T entity) {
		String insertQuery = this.createInsertPrefixCommand(entity) + SPACE + INSERT_VALUE_KEY + SPACE
				+ this.createInsertSuffixCommand(entity);

		Connection conn = null;
		Statement statement = null;
		int result = -1;

		try {
			conn = getConnection(true);
			statement = conn.createStatement();
			DatabaseMetaData dbmd = conn.getMetaData();

			if (dbmd.supportsTransactionIsolationLevel(transactionIsolationLevel.getTypeValue())) {
				conn.setTransactionIsolation(transactionIsolationLevel.getTypeValue());
			}

			result = statement.executeUpdate(insertQuery);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				System.err.println(e1.getMessage());
			}
		}

		if (result > 0) {
			return "inserted " + result;
		}

		return "fail single inserted";
	}

	@Override
	public String batchInsertValues(List<T> entities, boolean isForceInsert) {
		if (entities == null || entities.size() == 0) {
			return EMPTY;
		}

		T entity = entities.get(0);
		String prefixInsertCommand = this.createInsertPrefixCommand(entity);
		Connection conn = null;
		Statement statement = null;
		int[] result = null;
		try {
			conn = getConnection(isForceInsert);
			statement = conn.createStatement();
			DatabaseMetaData dbmd = conn.getMetaData();
			if (dbmd.supportsTransactionIsolationLevel(transactionIsolationLevel.getTypeValue())) {
				conn.setTransactionIsolation(transactionIsolationLevel.getTypeValue());
			}
			for (T ent : entities) {
				String batchInsertQuery = prefixInsertCommand + SPACE + INSERT_VALUE_KEY + SPACE
						+ this.createInsertSuffixCommand(ent);
				statement.addBatch(batchInsertQuery);
			}
			result = statement.executeBatch();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			if (!isForceInsert) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.err.println(e1.getMessage());
				}
			}
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				System.err.println(e1.getMessage());
			}
		}

		if (result != null) {
			return "inserted " + Utils.sum(result);
		}

		if (isForceInsert) {
			return "still insert despite some error";
		}

		return "fail batch inserted";
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private Object protectValue(Object value) {
		if (value instanceof Number) {
			return value;
		}

		if (value instanceof String) {
			return "'" + StringEscapeUtils.escapeJava(value.toString()) + "'";
		}
		// TODO with array, json, date,. ..

		return value;
	}

	/**
	 * 
	 */
	public String createInsertSuffixCommand(T entity) {
		StringBuilder insertsuffixCommand = new StringBuilder(OPEN_PARENTHESIS);

		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object value = field.get(entity);
				insertsuffixCommand.append(this.protectValue(value));
				insertsuffixCommand.append(COMMA);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println(e.getMessage());
			}
		}

		insertsuffixCommand.replace(insertsuffixCommand.length() - 1, insertsuffixCommand.length(), EMPTY);
		insertsuffixCommand.append(CLOSE_PARENTHESIS);

		return insertsuffixCommand.toString();
	}

}
