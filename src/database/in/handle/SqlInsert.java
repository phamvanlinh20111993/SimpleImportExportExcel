package database.in.handle;

import java.lang.reflect.Field;
import java.util.List;

import database.in.annotation.Column;
import database.in.annotation.Table;
import utils.Constants;

public interface SqlInsert<T> {

	public String INSERT_VALUE_KEY = "VALUES";

	public String SPACE = " ";
	
	public String EMPTY = Constants.EMPTY;

	public String COMMA = ",";

	public String OPEN_PARENTHESIS = "(";

	public String CLOSE_PARENTHESIS = ")";
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public String singleInsertValue(T entity);
	
	/**
	 * 
	 * @param entities
	 * @param isForceInsert
	 * @return
	 */
	public String batchInsertValues(List<T> entities, boolean isForceInsert);

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public default String createInsertPrefixCommand(T entity) {
		final String INSERT_KEY = "INSERT INTO";

		StringBuilder insertPrefixCommand = new StringBuilder(INSERT_KEY);

		Class<?> clazzScope = entity.getClass();
		String tableName = clazzScope.getSimpleName();

		if (clazzScope.isAnnotationPresent(Table.class)) {
			Table tableAnnotation = clazzScope.getAnnotation(Table.class);
			tableName = tableAnnotation.name();
		}
		insertPrefixCommand.append(SPACE).append(tableName).append(OPEN_PARENTHESIS);

		StringBuilder columns = new StringBuilder(EMPTY);
		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String columnName = field.getName();
			if (field.isAnnotationPresent(Column.class)) {
				Column colAnnotation = field.getAnnotation(Column.class);
				columnName = colAnnotation.name();
			}
			columns.append(columnName).append(COMMA);
		}

		columns.replace(columns.length() - 1, columns.length(), EMPTY);

		insertPrefixCommand.append(columns).append(CLOSE_PARENTHESIS);

		return insertPrefixCommand.toString();
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public default String createInsertSuffixCommand(T entity) {
		final String QUESTION_MASK = "?";
		StringBuilder insertsuffixCommand = new StringBuilder(OPEN_PARENTHESIS);

		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			insertsuffixCommand.append(QUESTION_MASK).append(COMMA);
		}

		insertsuffixCommand.replace(insertsuffixCommand.length() - 1, insertsuffixCommand.length(), EMPTY);
		insertsuffixCommand.append(CLOSE_PARENTHESIS);

		return insertsuffixCommand.toString();
	}
}
