package input.validation.handle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import input.validation.annotation.Max;
import input.validation.annotation.Min;
import input.validation.annotation.NotEmpty;
import input.validation.annotation.NotNull;
import input.validation.annotation.Size;
import input.validation.annotation.Type;
import input.validation.annotation.Validated;
import input.validation.exception.ValidationError;
import input.validation.validate.DateTypeValidation;
import input.validation.validate.MaxValidation;
import input.validation.validate.MinValidation;
import input.validation.validate.NotEmptyValidation;
import input.validation.validate.NotNullValidation;
import input.validation.validate.NumberValidation;
import input.validation.validate.SizeValidation;
import input.validation.validate.StringTypeValidation;
import input.validation.validate.ValidationHandler;

/**
 * 
 * @author PhamLinh
 *
 * @param <R>
 */
public abstract class SimpleAbstractHandleRowData<R> implements RowDataHandle<R> {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleAbstractHandleRowData.class);

	protected R rowData;
	
	public SimpleAbstractHandleRowData() {}

	public SimpleAbstractHandleRowData(R rowData) {
		this.rowData = rowData;
	}

	public void validateColumns() {
		traversalObjectProperty(rowData);
	}

	/**
	 * 
	 * @param rowData
	 */
	private void traversalObjectProperty(Object rowData) {

		if (rowData == null)
			return;

		for (Field field : rowData.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value;

			try {
				value = field.get(rowData);
				// recursion
				if (field.isAnnotationPresent(Validated.class)) {
					if (value instanceof Collection<?>) {
						Collection<?> coll = (Collection<?>) value;
						Iterator<?> it = coll.iterator();
						while (it.hasNext()) {
							traversalObjectProperty(it.next());
						}
					} else {
						traversalObjectProperty(value);
					}
				} else {
					Annotation[] annotations = field.getAnnotations();
					for (Annotation annotation : annotations) {
						if (annotation.annotationType() != Validated.class) {
							handleValidationField(annotation, field, value);
						}
					}
				}

			} catch (IllegalArgumentException e) {
				logger.error("Error SimpleAbstractHandleRowData.traversalObjectProperty() {}", e.getMessage());
				System.err.println(e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("Error SimpleAbstractHandleRowData.traversalObjectProperty() {}", e.getMessage());
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * 
	 * @param annotation
	 * @param field
	 * @param value
	 */
	private void handleValidationField(Annotation annotation, Field field, Object value) {

		if (annotation.annotationType() == Size.class) {
			Size size = field.getAnnotation(Size.class);
			validateColumnData(new SizeValidation<Object>(size.size()), value);
			return;
		}

		if (annotation.annotationType() == Max.class) {
			Max val = field.getAnnotation(Max.class);
			validateColumnData(new MaxValidation<Object>(val.value()), value);
			return;
		}

		if (annotation.annotationType() == Min.class) {
			Min val = field.getAnnotation(Min.class);
			validateColumnData(new MinValidation<Object>(val.value()), value);
			return;
		}

		if (annotation.annotationType() == NotNull.class) {
			validateColumnData(new NotNullValidation<Object>(), value);
			return;
		}

		if (annotation.annotationType() == NotEmpty.class) {
			validateColumnData(new NotEmptyValidation<>(), value);
			return;
		}

		if (annotation.annotationType() == Type.class) {
			Type type = field.getAnnotation(Type.class);
			ValidationHandler<Object> handleError = null;

			switch (type.type()) {
			case DATE:
				handleError = new DateTypeValidation<Object>(type.pattern());
				break;

			case NUMBER:
				handleError = new NumberValidation<Object>(type.pattern());
				break;

			default:
				handleError = new StringTypeValidation<Object>(type.pattern());
				break;
			}

			validateColumnData(handleError, value.toString());
			return;
		}
	}

	abstract void validateColumnData(ValidationHandler<Object> handleError, Object value) throws ValidationError;

	public R getRowData() {
		return rowData;
	}

	public void setRowData(R rowData) {
		this.rowData = rowData;
	}

}
