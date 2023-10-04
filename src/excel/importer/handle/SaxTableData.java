package excel.importer.handle;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SAXTableData {
	
	private Map<String, String> header = new LinkedHashMap<>();
	
	private List<Map<String, String>> rowDatas = new LinkedList<>();
}
