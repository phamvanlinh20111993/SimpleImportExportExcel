package database.in.utils;

public final class Utils {
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static final long sum(int... arg) {
		long s = 0l;
		for (int ind = 0; ind < arg.length; ind++) {
			s += arg[ind];
		}
		return s;
	}
}
