package excel.importer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * the valid format is:  "[1,2,3,'4',5,6,"7",8, "hello friend"]"
	 * 
	 * @param arrStr
	 * @return
	 */
	public static List<String> toArray(String arrStr) {
		List<String> datas = new ArrayList<String>();

		final String ARRAY_PATTERN = "^\\[(?:(?:('|\")?.*\\1?),)*(?:('|\")?.*\\2?)*\\]$";
		Pattern isArrayPatten = Pattern.compile(ARRAY_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcherFound = isArrayPatten.matcher(arrStr);

		if (!matcherFound.find()) {
			return datas;
		}
		final String ELEMENT_PATTERN = "('|\")?([^,]+)\\1?";

		Pattern p = Pattern.compile(ELEMENT_PATTERN, Pattern.DOTALL);
		arrStr = arrStr.trim().substring(1, arrStr.length() - 1);

		Matcher m = p.matcher(arrStr);

		while (m.find()) {
			if(m.group(2) != null) {
				datas.add(m.group(2).trim()
						.replaceAll("^('|\")", "")
						.replaceAll("('|\")$", ""));
			}
		}
		
		return datas;
	}

}
