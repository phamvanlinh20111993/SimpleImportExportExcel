package main;

import java.math.BigDecimal;

import excel.importer.annotation.MappingHeader;
import excel.importer.utils.CellType;
import input.validation.annotation.Max;
import input.validation.annotation.NotEmpty;
import input.validation.annotation.NotNull;
import input.validation.annotation.Size;

public class PriceTableKiotVietDataModel {

	@Size(size = 10)
	@NotNull
	@MappingHeader(value = "Mã hàng", type = CellType.STRING)
	private String goodCode;

	@Max(value = 100)
	@NotNull
	@MappingHeader(value = "Tên hàng", type = CellType.STRING)
	private String goodName;

	@NotEmpty
	@MappingHeader(value = "Đơn vị tính", type = CellType.STRING)
	private String unit;

	@MappingHeader(value = "Nhóm hàng", type = CellType.STRING)
	private String goodGroup;

	@MappingHeader(value = "Tồn kho", type = CellType.DOUBLE)
	private Double inventoryNumber;

	@MappingHeader(value = "Giá vốn", type = CellType.DOUBLE)
	private BigDecimal costPrice;

	@MappingHeader(value = "Giá nhập cuối", type = CellType.DOUBLE)
	private BigDecimal lastCostPrice;

	@MappingHeader(value = "Giá chung", type = CellType.DOUBLE)
	private BigDecimal marketCostPrice;

	public PriceTableKiotVietDataModel(String goodCode, String goodName, String unit, String goodGroup,
			Double inventoryNumber, BigDecimal costPrice, BigDecimal lastCostPrice, BigDecimal marketCostPrice) {
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
