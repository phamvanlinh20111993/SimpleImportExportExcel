package database.in.handle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import database.in.utils.TransactionIsolationLevel;
import database.in.utils.Utils;

public class SimplePrepareStatementSqlInsert<T> extends AbstractSqlInsert<T> {

	public SimplePrepareStatementSqlInsert(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public SimplePrepareStatementSqlInsert(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel) {
		super();
		this.dataSource = dataSource;
		this.transactionIsolationLevel = transactionIsolationLevel;
	}

	@Override
	public String singleInsertValue(T entity) {

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

		if (rs > -1) {
			return "Inserted " + rs;
		}
		return "fail single insert";
	}

	@Override
	public String batchInsertValues(List<T> entities, boolean isForceInsert) {
		if (entities == null || entities.size() == 0) {
			return EMPTY;
		}

		T entity = entities.get(0);

		String sqlInsertStatement = this.createInsertPrefixCommand(entity) + SPACE + INSERT_VALUE_KEY + SPACE
				+ this.createInsertSuffixCommand(entity);

		Connection conn = null;
		PreparedStatement statement = null;
		int[] res = null;
		try {
			conn = getConnection(isForceInsert);
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
			return "Inserted " + Utils.sum(res);
		} catch (SQLException e) {
			if (!isForceInsert) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.err.println(e1.getMessage());
				}
			}
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

		if (res != null) {
			return "Inserted " + Utils.sum(res);
		}

		if (isForceInsert) {
			return "still insert despite some error";
		}

		return "Fail batch insert";
	}
}
