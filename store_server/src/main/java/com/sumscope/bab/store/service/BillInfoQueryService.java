package com.sumscope.bab.store.service;

import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.model.model.BillInfoModel;

public interface BillInfoQueryService {

	/**
	 * 根据票据号和操作人ID检查该票据是否被其他库存信息引用。
	 * 2：当前操作人创建并使用了同一个票据号的票据信息，且该信息仍有有效库存。
	 * 1：有其他操作人创建并使用了同一个票据号的票据信息，且该信息仍有有效库存。
	 * 0：没有相关信息
	 */
	BillInfoUsage checkBillInfoByNumber(String billNumber, String operatorId);

	/**
	 *根据票号和操作人获取票据信息
     */
	BillInfoModel getBillInfoByNumber(String billNumber, String operatorId);

}
