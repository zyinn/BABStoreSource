package com.sumscope.bab.store.dao;

import java.util.List;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.BillInfoValidationModel;

public interface BillInfoDao {

	/**
	 * 根据票据号获取当前票据信息应用情况。执行数据库操作时需要进行表的关联。
	 */
	List<BillInfoValidationModel> searchBillInfoByNumber(String billNumber);

	/**
	 * 新增票据信息数据
	 */
	void insertNewBillInfo(BillInfoModel billInfoModel);

	/**
	 * 根据票据号及操作人ID更新票据信息
	 */
	void updateBillInfo(BillInfoModel billInfoModel);

	/**
	 * 根据ID获取票据信息数据
	 */
	BillInfoModel getBillInfoById(String id);

	/**
	 * 根据业务主键获取票据信息数据
	 */
	BillInfoModel getBillInfoByKeys(String billNumber, String operatorId);

}
