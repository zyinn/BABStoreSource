package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.WEBEnum;
import com.sumscope.bab.store.model.dto.WEBParameterEnumDto;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class WEBParameterEnumConverter {

	public List<WEBParameterEnumDto> convertToDtoList(WEBEnum[] enumModel) {
        List<WEBParameterEnumDto> list =new ArrayList<>();
        for (WEBEnum anEnumModel : enumModel) {
            WEBParameterEnumDto webParameterEnumDto = new WEBParameterEnumDto();
            webParameterEnumDto.setCode(anEnumModel.getCode());
            webParameterEnumDto.setName(anEnumModel.getDisplayName());
            list.add(webParameterEnumDto);
        }
        return list;
	}
}
