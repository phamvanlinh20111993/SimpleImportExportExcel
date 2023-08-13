package input.validation.utils;

public final class Utils {
	
	public static final Number toRealNumber(Number num) {
		
		if (num instanceof Long) {
			return num.longValue();
		}

		if (num instanceof Float) {
			return num.floatValue();
		}

		if (num instanceof Double) {
			return num.doubleValue();
		}

		return num.intValue();

	}
}
