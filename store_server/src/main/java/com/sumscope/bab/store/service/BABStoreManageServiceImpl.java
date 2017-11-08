package com.sumscope.bab.store.service;

import com.sumscope.bab.store.commons.enums.BABStoreStatus;
import com.sumscope.bab.store.commons.util.CopyNotNullBeanUtils;
import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.dao.StoreDao;
import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.model.*;
import com.sumscope.optimus.commons.exceptions.GeneralValidationErrorType;
import com.sumscope.optimus.commons.exceptions.ValidationException;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 接口实现类
 */
@Component
public class BABStoreManageServiceImpl implements BABStoreManageService,BABStoreQueryService {

	@Autowired
	private StoreDao storeDao;

	/**
	 * 根据库存信息ID与当前操作人ID进行库存信息数据验证。验证包括：
	 * 1. 是否存在对应ID的库存信息。
	 * 2. 库存信息是否处于可更新状态，既处于“已入库”状态
	 * 3. 库存信息是否有当前操作人创建。
	 * 
	 * 以上任意检查错误则抛出验证异常。若检查均通过则返回对应的BABStoreModel。
	 */
	public List<BabStoreModel> checkExistingAndValidate(List<String> ids, String operatorId) {
		List<BabStoreModel> list = new ArrayList<>();
		List<ValidationExceptionDetails> detailsList = new ArrayList<>();
		for(String id : ids){
			BabStoreModel model = getBabStoreModelValidation(operatorId, detailsList, SecurityStringUtil.validateStr(id));
			if(model!=null){
				list.add(model);
			}
		}
		if(detailsList!=null && detailsList.size()>0){
			throw new ValidationException(detailsList);
		}
		return list;
	}


	BabStoreModel getBabStoreModelValidation(String operatorId, List<ValidationExceptionDetails> detailsList, String id) {
		BabStoreModel babStoreModel = storeDao.getStoreById(id);
		if(babStoreModel!=null && babStoreModel.getOperatorId().equals(operatorId) &&
				babStoreModel.getStoreStatus() != BABStoreStatus.CAL){
			return babStoreModel;
		}else{
            detailsList.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING,"id", "根据库存信息ID与当前操作人ID进行库存信息数据验证失败！"));
			return null;
        }
	}

	/**
	 * 根据入库单模型生成库存信息模型。当库存信息模型已经在数据库中存在并以参数传递，这合并入库单信息至对应的库存信息中。若库存信息不存在，则生成一条新的库存信息并生成ID。
	 */
	private BabStoreModel generateStoreFromGoDownModel(StoreGoDownModel goDownModel, BabStoreModel modelInDB) {
		CopyNotNullBeanUtils.copyNotNullProperties(goDownModel,modelInDB);
		return modelInDB;
	}

	/**
	 * 根据出库单模型生成库存信息模型。当库存信息模型已经在数据库中存在并以参数传递，这合并入库单信息至对应的库存信息中。若库存信息不存在，则生成一条新的库存信息并生成ID。
	 */
	private BabStoreModel generateStoreFromOutModel(StoreOutModel outModel, BabStoreModel modelInDB) {
		CopyNotNullBeanUtils.copyNotNullProperties(outModel,modelInDB);
		return modelInDB;
	}

	/**
	 * @see BABStoreManageService#goDownStore(com.sumscope.bab.store.model.model.BabStoreModel)
	 */
	public void goDownStore(BabStoreModel storeModel) {
		checkExistingAndValidate(storeModel);
		storeModel.setStoreStatus(BABStoreStatus.ITS);
		storeDao.insertStores(Collections.singletonList(storeModel));
	}

	private void checkExistingAndValidate(BabStoreModel storeModel) {
		BabStoreWithInfoModel babStoreModel = storeDao.getStoreByKeys(storeModel);
		if(babStoreModel!=null){
			List<ValidationExceptionDetails> detailsList = new ArrayList<>();
			detailsList.add(new ValidationExceptionDetails(GeneralValidationErrorType.DATA_MISSING,"billNumber", "该用户和该票据已入库并且状态为库存中"));
			throw new ValidationException(detailsList);
		}
	}
	/**
	 * @see BABStoreQueryService#searchStoresByParam(com.sumscope.bab.store.model.model.StoreSearchParam, java.lang.String)
	 */
	public List<BabStoreWithInfoModel> searchStoresByParam(StoreSearchParam param, String operatorId) {
		param.setOperatorId(operatorId);
		return storeDao.searchStoresByParam(param);
	}

	@Override
	public BabStoreWithInfoModel searchStoresByBillNumber(BillNumberWithOperatorIdDto searchParam) {
		return storeDao.searchDealStoresByParam(searchParam);
	}


	/**
	 * @see BABStoreManageService#updateStore(com.sumscope.bab.store.model.model.StoreGoDownModel, java.lang.String)
	 */
	public void updateStore(StoreGoDownModel model,String operatorId) {
		List<ValidationExceptionDetails> detailsList = new ArrayList<>();
		BabStoreModel storeModel = getBabStoreModelValidation(operatorId, detailsList, model.getId());
		if(detailsList!=null && detailsList.size()>0){
			throw new ValidationException(detailsList);
		}
		BabStoreModel babStoreModel = generateStoreFromGoDownModel(model, storeModel);
		storeDao.updateStore(babStoreModel);
	}


	/**
	 * @see BABStoreManageService#outStore(com.sumscope.bab.store.model.model.StoreOutModel, java.lang.String)
	 */
	public List<BabStoreModel> outStore(StoreOutModel outModel, String operatorId) {
		List<BabStoreModel> babStoreModels = checkExistingAndValidate(outModel.getIds(), operatorId);
		List<BabStoreModel> list = new ArrayList<>();
		//此处生成list仅为了生成方法返回值。实际数据库修改并不需要此列表
		for(BabStoreModel babStoreModel: babStoreModels){
			babStoreModel.setStoreStatus(BABStoreStatus.OTS);
			BabStoreModel model = generateStoreFromOutModel(outModel, babStoreModel);
			list.add(model);
		}
		storeDao.outStores(outModel);
		return list;
	}


	/**
	 * @see BABStoreManageService#cancelStore(List, java.lang.String)
	 */
	public void cancelStore(List<String> ids, String operatorId) {
		List<BabStoreModel> babStoreModel = checkExistingAndValidate(ids, operatorId);
		for(BabStoreModel model:babStoreModel){
			model.setStoreStatus(BABStoreStatus.CAL);
			storeDao.updateStore(model);
		}
	}
}
