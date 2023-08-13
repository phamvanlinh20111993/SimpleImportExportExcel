package input.validation.main;

import java.util.List;

import input.validation.annotation.Max;
import input.validation.annotation.Min;
import input.validation.annotation.Size;
import input.validation.annotation.Type;
import input.validation.annotation.Validated;
import input.validation.utils.ValidateType;

public class DataModel {

	@Size(size = 10)
	private String length;

	@Type(type = ValidateType.DATE, pattern = "dd-mm-yyyy")
	private String date;

	@Type(type = ValidateType.NUMBER)
	private String IntegerNumber;

	@Type(pattern = "([a-zA-Z])+")
	private String matchingPattern;

	@Max(20)
	private String max;

	@Min(10)
	private String min;

	@Validated
	private List<DataModel> data;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIntegerNumber() {
		return IntegerNumber;
	}

	public void setIntegerNumber(String integerNumber) {
		IntegerNumber = integerNumber;
	}

	public String getMatchingPattern() {
		return matchingPattern;
	}

	public void setMatchingPattern(String matchingPattern) {
		this.matchingPattern = matchingPattern;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public List<DataModel> getData() {
		return data;
	}

	public void setData(List<DataModel> data) {
		this.data = data;
	}

}
