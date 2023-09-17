package excel.importer.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import excel.importer.annotation.MappingField;
import excel.importer.utils.CellType;
import input.validation.annotation.Max;
import input.validation.annotation.NotNull;
import input.validation.annotation.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceTableKiotVietImportDataModel {

	@Size(size = 8)
	@NotNull
	@MappingField(value = "Mã hàng", type = CellType.STRING)
	private String goodCode;

	@Max(value = 30)
	@NotNull
	@MappingField(value = "Tên hàng", type = CellType.STRING)
	private String goodName;

	// @NotEmpty
	@MappingField(value = "Đơn vị tính", type = CellType.STRING)
	private String unit;

	@MappingField(value = "Nhóm hàng", type = CellType.STRING)
	private String goodGroup;

	@MappingField(value = "Tồn kho", type = CellType.DOUBLE)
	private Double inventoryNumber;

	@MappingField(value = "Giá vốn", type = CellType.DECIMAL)
	private BigDecimal costPrice;

	@MappingField(value = "Giá nhập cuối", type = CellType.DECIMAL)
	private BigDecimal lastCostPrice;

	@MappingField(value = "Giá chung", type = CellType.DECIMAL)
	private BigDecimal marketCostPrice;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
