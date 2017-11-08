package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.*;
import com.sumscope.bab.store.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.store.facade.converter.WEBParameterEnumConverter;
import com.sumscope.bab.store.model.dto.*;
import com.sumscope.iam.edmclient.edmsource.dto.IamProvinceAndIdDTO;
import com.sumscope.iam.iamclient.model.AccessTokenResultDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为了不在Facade的实现类中写入太多业务代码，抽取出FacadeService用于业务处理。
 */
@Component
public class ApplicationFacadeService {

    @Autowired
    private WEBParameterEnumConverter webParameterEnumConverter;
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientWithCache;
    /**
     *页面初始化返回web 相关数据
     */
    public final List<AppInitialDataDto> getInitData(){
        List<AppInitialDataDto> list = new ArrayList<>();
        setWeBParameterEnumDtoForWebBillType(list);
        setWEBParameterEnumDtoForBABStoreGoDownType(list);
        setWEBParameterEnumDtoForBABStoreOutType(list);
        setWEBParameterEnumDtoForBabQuotePriceType(list);
        setWeBParameterEnumDtoForProvince(list);
        return list;
    }
    public final LoginResultDto loginUser(AccessTokenResultDto accessTokenResultDto,List<String> userPermittedFunctions){
        LoginResultDto dto = new LoginResultDto();
        Map<String , Object> map = new HashMap<>();
        map.put(BabStoreConstant.USER_TOKEN, accessTokenResultDto.getAccess_token());

        if(userPermittedFunctions!=null && userPermittedFunctions.size()>0){
            for(String code : userPermittedFunctions){
                map.put(code,"true");
            }
        }
        dto.setInfo(map);
        dto.setCurrentUser(setLoginUserDto(accessTokenResultDto));
        return dto;
    }
    private LoginUserDto setLoginUserDto(AccessTokenResultDto accessTokenResultDto){
        LoginUserDto dto = new LoginUserDto();
        dto.setUserName(accessTokenResultDto.getUsername());
        dto.setTokenExpiredTime(accessTokenResultDto.getExpires_in());
        dto.setId(accessTokenResultDto.getUserId());
        return dto;
    }
    //出库类型
    private void setWEBParameterEnumDtoForBABStoreOutType(List<AppInitialDataDto> list) {
        List<WEBParameterEnumDto> webParameterEnumDto= webParameterEnumConverter.convertToDtoList(BABStoreOutType.values());
        setAppInitialDataDto(list, webParameterEnumDto,BABStoreConditionName.BABOutStoreType.getDisplayName(),WEBSearchParameterMode.MULTIPLE);
    }

    //入库类型
    private void setWEBParameterEnumDtoForBABStoreGoDownType(List<AppInitialDataDto> list) {
        List<WEBParameterEnumDto> webParameterEnumDto= webParameterEnumConverter.convertToDtoList(BABStoreGoDownType.values());
        setAppInitialDataDto(list, webParameterEnumDto,BABStoreConditionName.BABInStoreType.getDisplayName(),WEBSearchParameterMode.MULTIPLE);
    }
    //交易地点
    private List<AppInitialDataDto> setWeBParameterEnumDtoForProvince(List<AppInitialDataDto> list) {
        List<IamProvinceAndIdDTO> cn = edmHttpClientWithCache.getIamProvinceAndIdDTOToList();
        List<WEBParameterEnumDto> webParameterEnumDto = new ArrayList<>();
        for (IamProvinceAndIdDTO province : cn) {
            ProvinceDto provinceDto=new ProvinceDto();
            BeanUtils.copyProperties(province,provinceDto);
            webParameterEnumDto.add(provinceDto);
        }
        setAppInitialDataDto(list, webParameterEnumDto,BABStoreConditionName.Province.getDisplayName(),WEBSearchParameterMode.MULTIPLE);
        return list;
    }

    //票据类型
    private void setWeBParameterEnumDtoForWebBillType(List<AppInitialDataDto> list) {
        List<WEBParameterEnumDto> webBillTypeDto = webParameterEnumConverter.convertToDtoList(WEBBillType.values());
        setAppInitialDataDto(list, webBillTypeDto,BABStoreConditionName.TicketType.getDisplayName(),WEBSearchParameterMode.MULTIPLE);
    }

    private void setAppInitialDataDto(List<AppInitialDataDto> list, List<WEBParameterEnumDto> enumDto,String conditionName,WEBSearchParameterMode mode) {
        AppInitialDataDto dto = new AppInitialDataDto();
        dto.setConditions(enumDto);
        dto.setConditionMode(mode);
        dto.setConditionName(conditionName);
        list.add(dto);
    }

    //承兑行类别
    private void setWEBParameterEnumDtoForBabQuotePriceType(List<AppInitialDataDto> list) {
        List<WEBParameterEnumDto> babAcceptingCompanyType = webParameterEnumConverter.convertToDtoList(BABAcceptingCompanyType.values());
        setAppInitialDataDto(list, babAcceptingCompanyType,BABStoreConditionName.AcceptanceBankCategory.getDisplayName(),WEBSearchParameterMode.SINGLE);
    }
}
