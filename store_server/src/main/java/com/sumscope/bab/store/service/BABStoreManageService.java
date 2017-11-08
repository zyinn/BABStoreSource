package com.sumscope.bab.store.service;

import com.sumscope.bab.store.model.model.*;

import java.util.List;

/**
 * 库存相关服务接口，由于不进行事务控制仅向service层提供服务。Facade级需要调用# {@link BABStoreManagementService}接口
 */
interface BABStoreManageService {

	/**
	 * 新增入库数据
	 * 先调用BillInfoService的建立票据信息服务，并根据返回值设置对应票据信息ID。
	 * 再建立库存信息模型并调用Dao新增数据。
	 */
	void goDownStore(BabStoreModel storeModel);

	/**
	 * 根据入库单更新当前库存信息。
	 * 
	 * 更新前，需要做一些验证：该库存ID存在且被该用户创建，同时仍处于有效状态。已出库与已作废库存不可更新并抛出异常。
	 */
	void updateStore(StoreGoDownModel model,String operatorId);

	/**
	 * 出库一条库存数据。前提为该ID存在且有效。已出库或已作废库存不可出库。方法为按ID读取库存信息，检查操作人是否为当前用户，是则按出库model数据更新该信息，设置库存状态为已出库，并更新数据库。
	 * 若不是当前用户所有报价单则抛出异常。
	 */
	List<BabStoreModel> outStore(StoreOutModel outModel, String operatorId);

	/**
	 * 作废库存。按id读取库存信息，设置库存状态为已作废，更新数据库，将ID列表中的库存数据作废。前提是库存信息是由该用户创建。
	 */
	void cancelStore(List<String> ids, String operatorId);

}
