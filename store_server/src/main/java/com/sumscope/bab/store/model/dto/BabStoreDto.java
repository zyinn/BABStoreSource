package com.sumscope.bab.store.model.dto;

import java.util.Date;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import java.math.BigDecimal;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;

/**
 * 库存信息Dto，用于查询列表
 */
public class BabStoreDto {

	/**
	 * 库存信息逻辑主键
	 */
	private String id;

	/**
	 * 票据信息逻辑主键
	 */
	private String billInfoId;

	/**
	 * 入库日期
	 */
	private Date godownDate;

	/**
	 * 入库类型
	 */
	private BABStoreGoDownType godownType;
	/**
	 * 入库价格
	 */
	private BigDecimal godownPrice;
	/**
	 * 应付金额=票面金额－票面金额*入库价格（%）*剩余期限÷360
	 * 若无入库价格，则不显示
	 */
	private BigDecimal amountsPayable;
	/**
	 * 应收金额
	 *  应付金额=票面金额－票面金额*出库价格（%）*转出剩余期限÷360
	 */
	private BigDecimal amountDue;

	/**
	 * 出库日期
	 */
	private Date outDate;

	/**
	 * 出库类型
	 */
	private BABStoreOutType outType;

	/**
	 * 出库价格
	 */
	private BigDecimal outPrice;

	/**
	 * 对手方名称
	 */
	private String counterPartyName;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 库存状态
	 */
	private BABStoreStatus storeStatus;

	/**
	 * 操作人ID，一条库存信息仅有一个操作人。
	 */
	private String operatorId;

	/**
	 * 数据建立日期
	 */
	private Date createDate;

	/**
	 * 数据最后更新日期
	 */
	private Date latestUpdateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillInfoId() {
		return billInfoId;
	}

	public void setBillInfoId(String billInfoId) {
		this.billInfoId = billInfoId;
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

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public BABStoreOutType getOutType() {
		return outType;
	}

	public void setOutType(BABStoreOutType outType) {
		this.outType = outType;
	}

	public BigDecimal getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(BigDecimal outPrice) {
		this.outPrice = outPrice;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLatestUpdateDate() {
		return latestUpdateDate;
	}

	public void setLatestUpdateDate(Date latestUpdateDate) {
		this.latestUpdateDate = latestUpdateDate;
	}

	public BigDecimal getGodownPrice() {
		return godownPrice;
	}

	public void setGodownPrice(BigDecimal godownPrice) {
		this.godownPrice = godownPrice;
	}

	public BigDecimal getAmountsPayable() {
		return amountsPayable;
	}

	public void setAmountsPayable(BigDecimal amountsPayable) {
		this.amountsPayable = amountsPayable;
	}

	public BigDecimal getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(BigDecimal amountDue) {
		this.amountDue = amountDue;
	}
}
