package com.sumscope.bab.store.service;

import com.sumscope.bab.store.model.model.BabStoreModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.model.model.StoreOutModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存管理服务，该服务调用对应的库存信息及票据信息服务，完成库存入库，出库等完整功能。
 */
public interface BABStoreManagementService {

	/**
	 * 根据入库单完成票据信息创建、更新以及库存信息建立。该方法受事务控制。
	 * 返回建立的库存信息ID列表
	 */
	@Transactional
	List<String> goDownStoresInTransaction(List<StoreGoDownModel> storeModel, String operatorId);

	/**
	 * 根据入库单更新库存数据。使用事务控制。
	 */
	@Transactional
	void updateStoreInTransaction(StoreGoDownModel storeModel, String operatorId);

	/**
	 * 根据出库单更新库存信息。使用事务控制。
	 */
	@Transactional
	List<BabStoreModel> outStoreInTransaction(StoreOutModel outModel, String operatorId);

	/**
	 * 作废一条库存信息。使用事务控制。
	 */
	@Transactional
	void cancelStoreInTransaction(List<String> ids,String operatorId);


}
