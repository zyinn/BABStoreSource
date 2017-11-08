package com.sumscope.bab.store.model.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;

/**
 * 入库单模型
 * 该模型没有对应数据表，是库存信息表的一部分。
 */
public class StoreGoDownModel {

	/**
	 * 库存信息逻辑主键
	 */
	private String id;

	/**
	 * 票据信息模型列表。入库时根据该信息新建或更新票据信息。该模型在更新时可空。为空时不更新票据信息。新增时，不可空。
	 */
	private List<BillInfoModel> billInfoModelList;

	/**
	 * 入库日期
	 */
	private Date godownDate;

	/**
	 * 入库类型
	 */
	private BABStoreGoDownType godownType;

	/**
	 * 备注
	 */
	private String memo;

	private BABStoreStatus storeStatus;

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

	public List<BillInfoModel> getBillInfoModelList() {
		return billInfoModelList;
	}

	public void setBillInfoModelList(List<BillInfoModel> billInfoModelList) {
		this.billInfoModelList = billInfoModelList;
	}

	public Date getGodownDate() {
		return godownDate;
	}

	public void setGodownDate(Date godownDate) {
		this.godownDate = godownDate;
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

	public BABStoreStatus getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(BABStoreStatus storeStatus) {
		this.storeStatus = storeStatus;
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
