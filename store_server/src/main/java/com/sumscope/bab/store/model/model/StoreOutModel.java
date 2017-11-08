package com.sumscope.bab.store.model.model;

import java.util.List;
import java.util.Date;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import java.math.BigDecimal;

/**
 * 出库单模型
 * 该模型没有对应数据表，是库存信息表的一部分。
 */
public class StoreOutModel {

	/**
	 * 库存信息逻辑主键列表，不可为空。
	 */
	private List<String> ids;

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

	private String operatorId;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
}
