package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.model.model.StoreOutModel;
import com.sumscope.bab.store.model.dto.StoreOutDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * StoreOutDto转换器
 */
@Component
public class StoreOutDtoConverter {

	/**
	 * 转换dto至对应model
	 */
	public StoreOutModel convertToStoreOutModel(StoreOutDto dto, String operatorId) {
		StoreOutModel model = new StoreOutModel();
		BeanUtils.copyProperties(dto,model);
		model.setCounterPartyName(SecurityStringUtil.validateStr(dto.getCounterPartyName()));
		model.setIds(SecurityStringUtil.securityCopyListStr(dto.getIds()));
		model.setOperatorId(operatorId);
		return model;
	}
}
