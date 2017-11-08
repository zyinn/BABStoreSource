package com.sumscope.bab.store.model.model;

/**
 * 带票据信息model的库存数据。用于库存查询。
 */
public class

BabStoreWithInfoModel extends BabStoreModel {

	private BillInfoModel billInfoModel;

	public BillInfoModel getBillInfoModel() {
		return billInfoModel;
	}

	public void setBillInfoModel(BillInfoModel billInfoModel) {
		this.billInfoModel = billInfoModel;
	}
}
