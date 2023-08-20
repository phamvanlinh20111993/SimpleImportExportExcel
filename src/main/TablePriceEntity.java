package main;

import java.math.BigDecimal;

import database.in.annotation.Column;
import database.in.annotation.Table;

@Table(name = "table_price")
public class TablePriceEntity {

	@Column(name = "good_code")
	private String goodCode;

	@Column(name = "good_name")
	private String goodName;

	private String unit;

	@Column(name = "good_group")
	private String goodGroup;

	@Column(name = "inventory_number")
	private Double inventoryNumber;

	@Column(name = "cost_price")
	private BigDecimal costPrice;

	@Column(name = "last_cost_price")
	private BigDecimal lastCostPrice;

	@Column(name = "market_cost_price")
	private BigDecimal marketCostPrice;
	
	
}
