package database.in.main;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import database.in.handle.SimplePrepareStatementSqlInsert;
import database.in.handle.SimpleSqlInsert;
import database.in.handle.SqlInsert;
import database.in.utils.TransactionIsolationLevel;

public class Main {

	public static void main(String[] args) {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/fake?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		dataSource.setMinIdle(5);
		dataSource.setMaxIdle(10);
		dataSource.setMaxTotal(25);
		dataSource.setMaxWaitMillis(30000);

		Employee employee = new Employee("linh", "12132132", "DU5", 26);
		Employee employee1 = new Employee("linh1", "121132132", "DU51", 21);
		Employee employee2 = new Employee("linh1", "12132132", "DU52", 9200);
		Employee employee3 = new Employee("linh12", "121321132", "DU521", 11);
		Employee employee4 = new Employee("linh11", "1=1", "DU521", 02);

		List<Employee> listE = Arrays.asList(employee, employee1, employee2, employee3, employee4);

		SqlInsert<Employee> sqlInsertCommand = new SimpleSqlInsert<Employee>(dataSource,
				TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);

		System.out.println(sqlInsertCommand.singleInsertValue(employee));

		System.out.println(sqlInsertCommand.batchInsertValues(listE, true));

		SqlInsert<Employee> prepareStatementSQLInsert = new SimplePrepareStatementSqlInsert<Employee>(dataSource,
				TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);

		System.out.println(prepareStatementSQLInsert.singleInsertValue(employee));

		System.out.println(prepareStatementSQLInsert.batchInsertValues(listE, true));
	}

}
