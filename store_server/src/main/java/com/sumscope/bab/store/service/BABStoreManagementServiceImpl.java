package com.sumscope.bab.store.service;

import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.model.model.BabStoreModel;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.model.model.StoreOutModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口实现类
 */
@Service
public class BABStoreManagementServiceImpl implements BABStoreManagementService {

	@Autowired
	private BABStoreManageService storeService;

	@Autowired
	private BillInfoManageService billInfoManageService;


	/**
	 * @see com.sumscope.bab.store.service.BABStoreManagementService#goDownStoresInTransaction(List, String)
	 */
	@Transactional
	public List<String> goDownStoresInTransaction(List<StoreGoDownModel> storeModel, String operatorId) {
		List<String> list = new ArrayList<>();
		for(StoreGoDownModel goDownModel:storeModel){
			for(BillInfoModel model : goDownModel.getBillInfoModelList()){
				BabStoreModel babStoreModel = setupStoreModelWithDefaultValue(operatorId, goDownModel);
				BillInfoModel billInfoModel = billInfoManageService.setupBillInfo(model, model.getOperatorId());
				babStoreModel.setBillInfoId(billInfoModel.getId());
				storeService.goDownStore(babStoreModel);
				list.add(babStoreModel.getId());
			}
		}
		return list;
	}

	private BabStoreModel setupStoreModelWithDefaultValue(String operatorId, StoreGoDownModel goDownModel) {
		BabStoreModel babStoreModel= new BabStoreModel();
		BeanUtils.copyProperties(goDownModel,babStoreModel);
		babStoreModel.setGodownDate(goDownModel.getGodownDate()!=null ? goDownModel.getGodownDate() : new Date());
		babStoreModel.setOperatorId(operatorId);
		babStoreModel.setId(UUIDUtils.generatePrimaryKey());
		babStoreModel.setCreateDate(new Date());
		babStoreModel.setLatestUpdateDate(new Date());
		return babStoreModel;
	}

	/**
	 * @see com.sumscope.bab.store.service.BABStoreManagementService#updateStoreInTransaction(com.sumscope.bab.store.model.model.StoreGoDownModel, java.lang.String)
	 */
	@Transactional
	public void updateStoreInTransaction(StoreGoDownModel storeModel, String operatorId) {
		List<BillInfoModel> billInfoModelList = storeModel.getBillInfoModelList();
		//入库单更新时仅能更新一个票据信息，验证list中有且只有一个票据信息，且票据id与库存中的票据id一致
		if(!CollectionsUtil.isEmptyOrNullCollection(billInfoModelList)){
			for(BillInfoModel model : billInfoModelList){
				billInfoManageService.setupBillInfo(model,operatorId);
			}
		}
		storeService.updateStore(storeModel,operatorId);
	}


	/**
	 * @see com.sumscope.bab.store.service.BABStoreManagementService#outStoreInTransaction(com.sumscope.bab.store.model.model.StoreOutModel, java.lang.String)
	 */
	@Transactional
	public List<BabStoreModel> outStoreInTransaction(StoreOutModel outModel, String operatorId) {
		return storeService.outStore(outModel, operatorId);
	}


	/**
	 * @see com.sumscope.bab.store.service.BABStoreManagementService#cancelStoreInTransaction(List, java.lang.String)
	 */
	public void cancelStoreInTransaction(List<String> ids,String operatorId) {
		storeService.cancelStore(SecurityStringUtil.securityCopyListStr(ids),operatorId);
	}

}
