package excel.importer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utility {

	public static Date toDate(String dateStr, String pattern) throws ParseException {
		DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
		Date date = format.parse(dateStr);
		return date;
	}

	public static Map<String, Object> toMapObject(String jsonStr) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(jsonStr, HashMap.class);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}
		return new HashMap<>();
	}

	/**
	 * TODO need to implement the format is "[1,2,3,4,5,6,7,8]"
	 * 
	 * @param arrStr
	 * @return
	 */
	public static List<Object> toArray(String arrStr) {
		List<Object> datas = new ArrayList<Object>();

		return datas;
	}

}
