package excel.importer.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import database.in.annotation.Column;
import database.in.annotation.Table;
import lombok.Data;

@Data
@Table(name = "kiot_viet_price")
public class PriceTableKiotVietTableEntity {
	@Column(name="code")
	private String goodCode;
	
	@Column(name="name")
	private String goodName;
	
	private String unit;

	@Column(name="good_group")
	private String goodGroup;

	@Column(name="invent_num")
	private Double inventoryNumber;

	@Column(name="cost_price")
	private BigDecimal costPrice;

	@Column(name="last_cost_price")
	private BigDecimal lastCostPrice;

	@Column(name="market_cost_price")
	private BigDecimal marketCostPrice;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
