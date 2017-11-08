package com.sumscope.bab.store.model.model;

/**
 * 用于票据信息检查的数据模型。该模型返回票据信息被其他库存信息引用的情况。
 */
public class BillInfoValidationModel {

	/**
	 * 票据信息ID
	 */
	private String id;

	/**
	 * 票据信息操作人ID
	 */
	private String operatorId;

	/**
	 * 库存信息ID
	 */
	private String storeId;

	/**
	 * 票据号
	 */
	private String billNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
}
