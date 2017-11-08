package com.sumscope.bab.store.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 入库单Dto
 */
public class StoreGoDownDto {

	/**
	 * 库存信息逻辑主键，新增时为空
	 */
	private String id;

	/**
	 * 票据信息模型。使用列表结构支持一次录入多张票据。入库时根据该信息新建或更新票据信息。该模型在更新时可空。为空时不更新票据信息。新增时，ID不可空。
	 */
	@Valid
	private List<BillInfoDto> billInfoDtoList;

	/**
	 * 入库类型
	 */
	@NotNull
	private BABStoreGoDownType godownType;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 入库日期
	 */
	private Date godownDate;
	/**
	 * 入库价格
	 */
	@DecimalMax(value = BabStoreConstant.PRICE_MAX_VALUE)
	@DecimalMin(value = BabStoreConstant.PRICE_MIN_VALUE)
	private BigDecimal godownPrice;

	/**
	 * 调整天数
	 */
	private Integer adjustDays;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BillInfoDto> getBillInfoDtoList() {
		return billInfoDtoList;
	}

	public void setBillInfoDtoList(List<BillInfoDto> billInfoDtoList) {
		this.billInfoDtoList = billInfoDtoList;
	}

	public BABStoreGoDownType getGodownType() {
		return godownType;
	}

	public void setGodownType(BABStoreGoDownType godownType) {
		this.godownType = godownType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getGodownDate() {
		return godownDate;
	}

	public void setGodownDate(Date godownDate) {
		this.godownDate = godownDate;
	}

	public BigDecimal getGodownPrice() {
		return godownPrice;
	}

	public void setGodownPrice(BigDecimal godownPrice) {
		this.godownPrice = godownPrice;
	}

	public Integer getAdjustDays() {
		return adjustDays;
	}

	public void setAdjustDays(Integer adjustDays) {
		this.adjustDays = adjustDays;
	}
}
