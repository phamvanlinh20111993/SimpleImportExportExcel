package database.in.handle;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.in.utils.InsertCodeStatus;
import database.in.utils.TransactionIsolationLevel;
import database.in.utils.Utils;

/**
 * 
 * @author PhamLinh
 *
 * @param <T>
 */
public class SimpleSqlInsert<T> extends AbstractSqlInsert<T> {

	private static final Logger logger = LoggerFactory.getLogger(SimpleSqlInsert.class);

	public SimpleSqlInsert(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public SimpleSqlInsert(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel) {
		super();
		this.dataSource = dataSource;
		this.transactionIsolationLevel = transactionIsolationLevel;
	}

	/**
	 * {@inheritDoc} refer: https://www.baeldung.com/java-jdbc-auto-commit
	 * https://stackoverflow.com/questions/14625371/rollback-batch-execution-when-using-jdbc-with-autocommit-true
	 */
	@Override
	public String singleInsertValue(T entity) {
		logger.info("SimpleSqlInsert.singleInsertValue() start");
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
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("SimplePrepareStatementSqlInsert.class batchInsertValues(): {}", e1.getMessage());
			}
			logger.error("SimpleSqlInsert.class singleInsertValue(): {}", e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				logger.error("SimpleSqlInsert.class singleInsertValue(): {}", e1.getMessage());
			}
		}

		if (result > 0) {
			return InsertCodeStatus.SUCCESS.getTypeValue() + " " + result;
		}

		logger.info("SimpleSqlInsert.singleInsertValue() end");

		return InsertCodeStatus.FAIL.getTypeValue();
	}

	/**
	 * {@inheritDoc} refer
	 * https://stackoverflow.com/questions/14625371/rollback-batch-execution-when-using-jdbc-with-autocommit-true
	 */
	@Override
	public String batchInsertValues(List<T> entities, boolean isForceInsert) {

		logger.info("SimpleSqlInsert.batchInsertValues() start");

		if (entities == null || entities.size() == 0) {
			return EMPTY;
		}

		T entity = entities.get(0);
		String prefixInsertCommand = this.createInsertPrefixCommand(entity);
		Connection conn = null;
		Statement statement = null;
		int[] result = null;

		boolean isAutoCommit = !isForceInsert;
		try {
			conn = getConnection(isAutoCommit);
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
			logger.error("SimpleSqlInsert.class batchInsertValues(): {}", e.getMessage());
			if (!isAutoCommit) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.error("SimpleSqlInsert.class batchInsertValues(): {}", e1.getMessage());
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
				logger.error("SimpleSqlInsert.class batchInsertValues(): {}", e1.getMessage());
			}
		}

		if (result != null) {
			return InsertCodeStatus.SUCCESS_FORCE_INSERT.getTypeValue() + " " + Utils.sum(result);
		}

		if (isForceInsert) {
			return InsertCodeStatus.FAIL_FORCE_INSERT.getTypeValue();
		}

		logger.info("SimpleSqlInsert.batchInsertValues() end");

		return InsertCodeStatus.FAIL_FORCE_INSERT.getTypeValue();
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
				logger.error("SimpleSqlInsert.class createInsertSuffixCommand(): {}", e.getMessage());
			}
		}

		insertsuffixCommand.replace(insertsuffixCommand.length() - 1, insertsuffixCommand.length(), EMPTY);
		insertsuffixCommand.append(CLOSE_PARENTHESIS);

		return insertsuffixCommand.toString();
	}

}
