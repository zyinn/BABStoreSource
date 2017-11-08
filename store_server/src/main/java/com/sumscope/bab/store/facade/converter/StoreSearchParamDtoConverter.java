package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.store.commons.enums.WEBBillType;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.WEBBillTypeModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StoreSearchParamDtoConverter {

	/**
	 * 根据查询参数Dto生成查询参数模型
	 */
	public StoreSearchParam convertToModel(StoreSearchParamDto dto) {
		StoreSearchParam storeSearchParam = new StoreSearchParam();
		BeanUtils.copyProperties(dto,storeSearchParam);
		if(storeSearchParam.isPaging()){
			storeSearchParam.setPageNumber(dto.getPageNumber()==0 ? 0 : dto.getPageNumber()*dto.getPageSize()+1);
			storeSearchParam.setPageSize((dto.getPageNumber()+1)*dto.getPageSize());
		}
		storeSearchParam.setBillTypeModel(converterBABBillTypeToModel(dto.getWebBillType()));
		return storeSearchParam;
	}

	List<WEBBillTypeModel> converterBABBillTypeToModel(List<WEBBillType> webBillType){
		if(CollectionsUtil.isEmptyOrNullCollection(webBillType)){
			return null;
		}
		List<WEBBillTypeModel> list = new ArrayList<>();
		for(WEBBillType billType : webBillType){
			WEBBillTypeModel webBillTypeModel = converterWEBBillTypeModel(billType);
			if(webBillTypeModel!=null){
				list.add(webBillTypeModel);
			}
		}
		return list;
	}

	private WEBBillTypeModel converterWEBBillTypeModel(WEBBillType billType){
		for(WEBBillType webBillType : WEBBillType.values()){
			if(webBillType == billType){
				WEBBillTypeModel model = new WEBBillTypeModel();
				model.setBillMedium(webBillType.getBabBillMedium());
				model.setBillType(webBillType.getBabBillType());
				return model;
			}
		}
		return null;
	}

}
