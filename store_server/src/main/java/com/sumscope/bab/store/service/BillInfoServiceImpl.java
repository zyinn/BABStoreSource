package com.sumscope.bab.store.service;

import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.commons.util.CopyNotNullBeanUtils;
import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.dao.BillInfoDao;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.BillInfoValidationModel;
import com.sumscope.optimus.commons.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口实现类
 */
@Service
public class BillInfoServiceImpl implements BillInfoQueryService,BillInfoManageService {

	@Autowired
	private BillInfoDao billInfoDao;

	/**
	 * 当入库更新或者出库时，需要获取可能存在的票据信息并更新。更新时需要先检查
	 * a:
	 * 1.根据参数传来的ID是否存在于数据库中，
	 * 2.该id对应的数据与参数中的票据号和当前用户是否符合。
	 * 若以上检查失败，抛出验证异常。
	 * b:
	 * 1. 根据业务主键（票据号，操作人）获取票据信息。
	 * 
	 */
	BillInfoModel getAndValidateBillInfoInDB(String infoId, String operatorId, String billNumber) {
        if(operatorId!=null && billNumber!=null){
            return billInfoDao.getBillInfoByKeys(billNumber, operatorId);
        }
		if(infoId!=null){
			BillInfoModel billInfo= billInfoDao.getBillInfoById(infoId);
			if(billInfo!=null){
				//当以id方式获取数据时，应检查operatorId与传入的参数是否一致
				if(operatorId.equals(billInfo.getOperatorId())){
					return billInfo;
				}else {
					List<ValidationExceptionDetails> eDetailsList = new ArrayList<>();
					eDetailsList.add(new ValidationExceptionDetails
							(GeneralValidationErrorType.DATA_MISSING, "billNumber", "该操作人对此票据无权操作！"));
					throw new ValidationException(eDetailsList);
				}
			}
		}
		return null;
	}

	/**
	 * 对票据数据进行更新时，我们先根据ID读取当前数据，再根据参数更新数据并调用Dao进行数据库更新。在更新时，一些字段不会被改变。
	 */
	private void mergeToDBModelForUpdate(BillInfoModel sourceModel, BillInfoModel modelInDB) {
		CopyNotNullBeanUtils.copyNotNullProperties(sourceModel,modelInDB);
		modelInDB.setLatestUpdateDate(new Date());
	}


	/**
	 * @see com.sumscope.bab.store.service.BillInfoQueryService#checkBillInfoByNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public BillInfoUsage checkBillInfoByNumber(String billNumber, String operatorId) {
        List<BillInfoValidationModel> billInfoValidationModels = billInfoDao.searchBillInfoByNumber(SecurityStringUtil.validateStr(billNumber));
        if(billInfoValidationModels ==null){
            return BillInfoUsage.NO_USG;
        }
        if(!CollectionsUtil.isEmptyOrNullCollection(billInfoValidationModels)){
            for(BillInfoValidationModel billInfo:billInfoValidationModels){
                if(billInfo.getOperatorId().equals(operatorId)){
                    return BillInfoUsage.USE_BY_CURRENT;
                }
            }
            return BillInfoUsage.USE_BY_OTHERS;
        }
		return BillInfoUsage.NO_USG;
	}

	@Override
	public BillInfoModel getBillInfoByNumber(String billNumber, String operatorId) {
		return billInfoDao.getBillInfoByKeys(SecurityStringUtil.validateStr(billNumber), operatorId);
	}


	/**
	 * @see com.sumscope.bab.store.service.BillInfoManageService#setupBillInfo(com.sumscope.bab.store.model.model.BillInfoModel, java.lang.String)
	 */
	@Override
	public BillInfoModel setupBillInfo(BillInfoModel billInfoModel, String operatorId) {
		BillInfoModel model = getAndValidateBillInfoInDB(billInfoModel.getId(), operatorId, billInfoModel.getBillNumber());
        if(model!=null){
            mergeToDBModelForUpdate(billInfoModel,model);
            billInfoDao.updateBillInfo(model);
            return model;
        }else{
            billInfoModel.setId(UUIDUtils.generatePrimaryKey());
			billInfoModel.setLatestUpdateDate(new Date());
			billInfoModel.setCreateDate(new Date());
            billInfoDao.insertNewBillInfo(billInfoModel);
            return billInfoModel;
        }
	}

}
