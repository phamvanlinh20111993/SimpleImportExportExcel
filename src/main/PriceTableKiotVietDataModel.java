package main;

import java.math.BigDecimal;

import input.validation.annotation.Max;
import input.validation.annotation.NotEmpty;
import input.validation.annotation.NotNull;
import input.validation.annotation.Size;

public class PriceTableKiotVietDataModel {
	
	@Size(size=10)
	@NotNull
	private String goodCode;
	
	@Max(value=100)
	@NotNull
	private String goodName;
	
	@NotEmpty
	private String unit;

	private String goodGroup;
	
	private Double inventoryNumber;

	private BigDecimal costPrice;

	private BigDecimal lastCostPrice;

	private BigDecimal marketCostPrice;

	public PriceTableKiotVietDataModel(String goodCode, String goodName, String unit, String goodGroup, Double inventoryNumber,
			BigDecimal costPrice, BigDecimal lastCostPrice, BigDecimal marketCostPrice) {
		super();
		this.goodCode = goodCode;
		this.goodName = goodName;
		this.unit = unit;
		this.goodGroup = goodGroup;
		this.inventoryNumber = inventoryNumber;
		this.costPrice = costPrice;
		this.lastCostPrice = lastCostPrice;
		this.marketCostPrice = marketCostPrice;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getGoodGroup() {
		return goodGroup;
	}

	public void setGoodGroup(String goodGroup) {
		this.goodGroup = goodGroup;
	}

	public Double getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(Double inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getLastCostPrice() {
		return lastCostPrice;
	}

	public void setLastCostPrice(BigDecimal lastCostPrice) {
		this.lastCostPrice = lastCostPrice;
	}

	public BigDecimal getMarketCostPrice() {
		return marketCostPrice;
	}

	public void setMarketCostPrice(BigDecimal marketCostPrice) {
		this.marketCostPrice = marketCostPrice;
	}
}
