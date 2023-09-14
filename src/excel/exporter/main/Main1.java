package excel.exporter.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main1 {

	public static Object convertAnnotationToConcreteObject(Object obj, Annotation annotation) {

		Method[] methods = annotation.annotationType().getDeclaredMethods();

		for (Method method : methods) {
			try {
				Object value = method.invoke(annotation);
				Field nameField;
				nameField = obj.getClass().getDeclaredField(method.getName());
				nameField.setAccessible(true);

				if (method.getReturnType().isAnnotation()) {
					Annotation nestedAnnotation = (Annotation) value;
					nameField.set(obj, convertAnnotationToConcreteObject(obj, nestedAnnotation));
					continue;
				}
				nameField.set(obj, value);

			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException
					| InvocationTargetException e) {
				System.err.println("Error " + e.getMessage());
			}
		}

		return obj;
	}

	public static void main(String[] args) {
		Test test = new Test();
		test.example = "1000";

		Example1 example1 = new Example1();

		Field[] fields = test.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Example.class)) {
				Annotation annotation = field.getAnnotation(Example.class);

				example1 = (Example1) convertAnnotationToConcreteObject(new Example1(), annotation);

				break;
			}
		}

		System.out.println(example1.toString());

	}
}
