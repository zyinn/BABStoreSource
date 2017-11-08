package com.sumscope.bab.store.dao;

import java.util.List;

import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.model.model.BabStoreModel;
import com.sumscope.bab.store.model.model.StoreOutModel;

public interface StoreDao {

	/**
	 * 查询入库信息
	 */
	List<BabStoreWithInfoModel> searchStoresByParam(StoreSearchParam param);

	/**
	 * 新增一条入库信息
	 */
	void insertStores(List<BabStoreModel> models);

	/**
	 * 根据输入模型更新数据库中数据
	 */
	void updateStore(BabStoreModel model);

	/**
	 * 按ID获取库存信息
	 */
	BabStoreModel getStoreById(String id);

	/**
	 * 根据出库单信息更新库存信息。一张出库单可以对应多张库存信息，使用批量update的方式更新数据。
	 */
	void outStores(StoreOutModel outModel);

	/**
	 *根据操作人id和票据id验证是否用户已入库并且状态为库存中
     */
	BabStoreWithInfoModel getStoreByKeys(BabStoreModel storeModel);

	/**
	 * 根据票号和操作人 ID 查询票据信息
	 * 供成交单使用
     */
	BabStoreWithInfoModel searchDealStoresByParam(BillNumberWithOperatorIdDto searchParam);
}
