package com.sumscope.bab.store.model.dto;

import java.math.BigDecimal;

/**
 * 附加票据信息的库存信息Dto
 */
public class BabStoreWithInfoDto extends BabStoreDto {

	/**
	 * 票据信息dto
	 */
	private BillInfoDto billInfoDto;

	/**
	 * 当日最优价，单位：百分之
	 */
	private BigDecimal bestPrice;

	/**
	 * 最低贴息
	 */
	private BigDecimal lowestDiscount;

	/**
	 * 最优收款
	 */
	private BigDecimal bestGathering;
	/**
	 * 当日最优收益=当日最优收款－应付金额
	 * 若无入库价格，则不显示
	 */
	private BigDecimal bestIncome;
	/**
	 * 点差
	 */
	private BigDecimal pointDifference;
	/**
	 * 总收益
	 */
	private BigDecimal totalIncome;
	/**
	 * 转出剩余天数
	 */
	private Integer remainingTermOut;
	/**
	 *转入剩余天数
	 */
	private Integer remainingTermIn;
	/**
	 * 调整天数
	 */
	private int adjustDays;
	/**
	 * 持票天数
	 */
	private Integer ticketDays;

	public BillInfoDto getBillInfoDto() {
		return billInfoDto;
	}

	public void setBillInfoDto(BillInfoDto billInfoDto) {
		this.billInfoDto = billInfoDto;
	}

	public BigDecimal getBestPrice() {
		return bestPrice;
	}

	public void setBestPrice(BigDecimal bestPrice) {
		this.bestPrice = bestPrice;
	}

	public BigDecimal getLowestDiscount() {
		return lowestDiscount;
	}

	public void setLowestDiscount(BigDecimal lowestDiscount) {
		this.lowestDiscount = lowestDiscount;
	}

	public BigDecimal getBestGathering() {
		return bestGathering;
	}

	public void setBestGathering(BigDecimal bestGathering) {
		this.bestGathering = bestGathering;
	}

	public BigDecimal getBestIncome() {
		return bestIncome;
	}

	public void setBestIncome(BigDecimal bestIncome) {
		this.bestIncome = bestIncome;
	}

	public BigDecimal getPointDifference() {
		return pointDifference;
	}

	public void setPointDifference(BigDecimal pointDifference) {
		this.pointDifference = pointDifference;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Integer getRemainingTermOut() {
		return remainingTermOut;
	}

	public void setRemainingTermOut(Integer remainingTermOut) {
		this.remainingTermOut = remainingTermOut;
	}

	public Integer getRemainingTermIn() {
		return remainingTermIn;
	}

	public void setRemainingTermIn(Integer remainingTermIn) {
		this.remainingTermIn = remainingTermIn;
	}

	public int getAdjustDays() {
		return adjustDays;
	}

	public void setAdjustDays(int adjustDays) {
		this.adjustDays = adjustDays;
	}

	public Integer getTicketDays() {
		return ticketDays;
	}

	public void setTicketDays(Integer ticketDays) {
		this.ticketDays = ticketDays;
	}
}
