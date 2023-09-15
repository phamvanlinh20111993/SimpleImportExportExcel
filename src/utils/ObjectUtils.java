package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.Buffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObjectUtils {
	private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * all property of the object is empty. Note that only use for POJO object TODO
	 * add more type you can figure out.
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {

		if (obj == null)
			return true;

		if (obj instanceof Enum<?>) {
			return false;
		}

		// the primitive type, auto is not empty
		if (obj.getClass().isPrimitive()) {
			return true;
		}

		// for Wrapper class of primitive type, auto is not empty
		if ((obj instanceof Boolean || obj instanceof Byte || obj instanceof Short || obj instanceof Integer
				|| obj instanceof Long || obj instanceof Float || obj instanceof Double || obj instanceof Character)) {
			return true;
		}

		if (obj instanceof String) {
			return ((String) obj).length() == 0;
		}

		if (obj instanceof StringBuilder) {
			return ((StringBuilder) obj).length() == 0;
		}

		if (obj instanceof StringBuffer) {
			return ((StringBuffer) obj).length() == 0;
		}

		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}

		if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).size() == 0;
		}

		if (obj instanceof Buffer) {
			// TODO ....
			return false;
		}

		if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).size() == 0;
		}

		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				// System.out.println(field.get(obj) + " " + field.getName());
				if (!isEmpty(field.get(obj))) {
					return false;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println("Error ObjectUtils.isEmpty() " + e.getMessage());
				logger.error("Error ObjectUtils.isEmpty() {}", e.getMessage());
			}
		}

		return true;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * refer:
	 * https://www.baeldung.com/java-find-all-classes-in-package#:~:text=Class%20loaders%20are%20dynamic.&text=Hence%2C%20finding%20classes%20in%20a,find%20classes%20inside%20a%20package.
	 * 
	 * @param packageName
	 * @param type
	 * @return
	 */
	public static Optional<Object> createInstanceFromPackage(String packageName, Class<?> type) {
		@SuppressWarnings("static-access")
		InputStream inputStream = ClassLoader.getSystemClassLoader()
				.getSystemResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		List<Class<?>> listClass = reader.lines().filter(line -> line.endsWith(".class"))
				.map(line -> getClass(line, packageName)).collect(Collectors.toList());

		try {
			for (Class<?> clazz : listClass) {
				// System.out.println(clazz.getName());
				if (clazz.equals(type)) {
					Constructor<?> constructor = clazz.getDeclaredConstructor();
					constructor.setAccessible(true);
					Object instance = constructor.newInstance();
					return Optional.of(instance);
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.err.println("Error ObjectUtils.createInstanceFromPackage() " + e.getMessage());
			logger.error("Error ObjectUtils.createInstanceFromPackage() %s", e.getMessage());
		}
		return Optional.empty();
	}

	/**
	 * 
	 * @param className
	 * @param packageName
	 * @return
	 */
	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			System.err.println("Error ObjectUtils.getClass() " + e.getMessage());
		}
		return null;
	}
}
