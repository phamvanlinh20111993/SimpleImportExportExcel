package database.in.handle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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
public class SimplePrepareStatementSqlInsert<T> extends AbstractSqlInsert<T> {

	private static final Logger logger = LoggerFactory.getLogger(SimplePrepareStatementSqlInsert.class);

	public SimplePrepareStatementSqlInsert(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public SimplePrepareStatementSqlInsert(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel) {
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
		logger.info("SimplePrepareStatementSqlInsert.singleInsertValue() start");
		String sqlInsertStatement = this.createInsertPrefixCommand(entity) + SPACE + INSERT_VALUE_KEY + SPACE
				+ this.createInsertSuffixCommand(entity);

		Connection conn = null;
		PreparedStatement statement = null;
		int rs = -1;
		try {
			conn = getConnection(false);
			statement = conn.prepareStatement(sqlInsertStatement);
			DatabaseMetaData dbmd = conn.getMetaData();

			if (dbmd.supportsTransactionIsolationLevel(transactionIsolationLevel.getTypeValue())) {
				conn.setTransactionIsolation(transactionIsolationLevel.getTypeValue());
			}

			this.extractEntity(entity, statement);
			rs = statement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				logger.error("SimplePrepareStatementSqlInsert.class batchInsertValues(): {}", e1.getMessage());
			}
			logger.error("SimplePrepareStatementSqlInsert.class singleInsertValue(): {}", e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				logger.error("SimplePrepareStatementSqlInsert.class singleInsertValue(): {}", e1.getMessage());
			}
		}

		if (rs > -1) {
			return InsertCodeStatus.SUCCESS.getTypeValue() + " " + rs;
		}

		logger.info("SimplePrepareStatementSqlInsert.singleInsertValue() end");

		return InsertCodeStatus.FAIL.getTypeValue();
	}

	/**
	 * {@inheritDoc} refer: https://www.baeldung.com/java-jdbc-auto-commit
	 * https://stackoverflow.com/questions/14625371/rollback-batch-execution-when-using-jdbc-with-autocommit-true
	 */
	@Override
	public String batchInsertValues(List<T> entities, boolean isForceInsert) {

		logger.info("SimplePrepareStatementSqlInsert.batchInsertValues() start");

		if (entities == null || entities.size() == 0) {
			return EMPTY;
		}

		T entity = entities.get(0);

		String sqlInsertStatement = this.createInsertPrefixCommand(entity) + SPACE + INSERT_VALUE_KEY + SPACE
				+ this.createInsertSuffixCommand(entity);

		Connection conn = null;
		PreparedStatement statement = null;
		int[] res = null;
		boolean isAutoCommit = !isForceInsert;
		try {
			conn = getConnection(isAutoCommit);
			statement = conn.prepareStatement(sqlInsertStatement);
			DatabaseMetaData dbmd = conn.getMetaData();
			if (dbmd.supportsTransactionIsolationLevel(transactionIsolationLevel.getTypeValue())) {
				conn.setTransactionIsolation(transactionIsolationLevel.getTypeValue());
			}
			for (T ent : entities) {
				this.extractEntity(ent, statement);
				statement.addBatch();
			}
			res = statement.executeBatch();
			conn.commit();
			return InsertCodeStatus.SUCCESS_FORCE_INSERT + " " + Utils.sum(res);
		} catch (SQLException e) {
			if (!isAutoCommit && conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.error("SimplePrepareStatementSqlInsert.class batchInsertValues(): {}", e1.getMessage());
				}
			}
			logger.error("SimplePrepareStatementSqlInsert.class batchInsertValues(): {}", e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				logger.error("SimplePrepareStatementSqlInsert.class batchInsertValues(): {}", e1.getMessage());
			}
		}

		logger.info("SimplePrepareStatementSqlInsert.batchInsertValues() ends");

		if (res != null) {
			return InsertCodeStatus.SUCCESS_FORCE_INSERT + " " + Utils.sum(res);
		}

		if (isForceInsert) {
			return "Still insert despite some error";
		}

		return InsertCodeStatus.FAIL_FORCE_INSERT.getTypeValue();
	}
}
