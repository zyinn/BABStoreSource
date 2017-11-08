package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.commons.enums.BABQuotePriceType;
import com.sumscope.bab.quote.commons.enums.Direction;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.quote.model.dto.*;
import com.sumscope.bab.store.commons.util.SecurityStringUtil;
import com.sumscope.bab.store.commons.util.ValidationUtil;
import com.sumscope.bab.store.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.store.model.dto.BABPostDiscountDto;
import com.sumscope.bab.store.model.dto.BillInfoDiscountDto;
import com.sumscope.iam.edmclient.edmsource.dto.IdbFinancialCompanyAndIdDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import com.sumscope.iam.edmclient.edmsource.dto.UserRetDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 * 发布贴现converter
 */
@Component
public class BabDiscountToSSRQuoteDtoConverter {
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientWithCache;

    public List<SSRQuoteDto> convertToModel(List<BABPostDiscountDto> babPostDiscountDto,String operatorId) {
        ValidationUtil.validateModel(babPostDiscountDto);
        List<SSRQuoteDto> list = new ArrayList<>();
        if(!CollectionsUtil.isEmptyOrNullCollection(babPostDiscountDto)){
            for(BABPostDiscountDto dto : babPostDiscountDto){
                SSRQuoteDto quoteDto = new SSRQuoteDto();
                BeanUtils.copyProperties(dto,quoteDto);
                quoteDto.setPrice(SecurityStringUtil.validateStr(dto.getPrice()));
                BillInfoDiscountDto discountDto = dto.getBillInfoDiscountDto();
                BABAcceptingCompanyType acceptingCompanyType = discountDto.getAcceptingCompanyType();
                quoteDto.setQuotePriceType(getBABQuotePriceType(acceptingCompanyType,discountDto.getBillType()));
                if(discountDto.getBillType() == BABBillType.CMB){
                    QuoteAdditionalInfoDto additionalInfoDto = new QuoteAdditionalInfoDto();
                    UserRetDTO userRetDto = edmHttpClientWithCache.getUserRetDTO(operatorId);
                    UserContactInfoRetDTO usersContactDto = edmHttpClientWithCache.getUserContactInfoRetDTO(operatorId);
                    additionalInfoDto.setContactName(userRetDto.getUsername());
                    additionalInfoDto.setQuoteCompanyName(userRetDto.getCompanyName());
                    additionalInfoDto.setContactTelephone(getUserMobileTel(usersContactDto));
                    additionalInfoDto.setCompanyType(dto.getBillInfoDiscountDto().getAcceptingCompanyType());
                    additionalInfoDto.setAcceptingHouseName(SecurityStringUtil.validateStr(dto.getBillInfoDiscountDto().getAcceptingCompanyName()));
                    quoteDto.setContainsAdditionalInfo(true);
                    quoteDto.setAdditionalInfo(additionalInfoDto);
                }else{
                    setUserContactAndCompanyDto(operatorId, quoteDto);
                }
                BeanUtils.copyProperties(discountDto,quoteDto);
                quoteDto.setOperatorId(SecurityStringUtil.validateStr(operatorId));
                quoteDto.setQuoteStatus(BABQuoteStatus.DSB);
                quoteDto.setDirection(Direction.OUT);
                quoteDto.setEffectiveDate(new Date());
                list.add(quoteDto);
            }
        }
        return list;
    }

    final void setUserContactAndCompanyDto(String operatorId, SSRQuoteDto quoteDto) {
        UserContactInfoRetDTO usersContactDto = edmHttpClientWithCache.getUserContactInfoRetDTO(operatorId);
        UserRetDTO userRetDto = edmHttpClientWithCache.getUserRetDTO(operatorId);
        IdbFinancialCompanyAndIdDTO idbFinancial =null;
        IAMUserReferenceDto iamDto = new IAMUserReferenceDto();
        if(userRetDto!=null){
            idbFinancial = edmHttpClientWithCache.getIdbFinancialCompanyAndIdDTO(userRetDto.getCompanyId());
            iamDto.setID(userRetDto.getId());
            iamDto.setName(userRetDto.getName());
            iamDto.setUserName(userRetDto.getUsername());
        }
        if(usersContactDto!=null){
            iamDto.setMobileTel(getUserMobileTel(usersContactDto));
            iamDto.setQq(usersContactDto.getQq().contains(";") ? usersContactDto.getQq().replace(";","") : usersContactDto.getQq());
        }
        quoteDto.setContactDto(iamDto);
        IAMCompanyReferenceDto iamCompanyReferenceDto = new IAMCompanyReferenceDto();
        if(idbFinancial!=null){
            iamCompanyReferenceDto.setID(idbFinancial.getId());
            iamCompanyReferenceDto.setName(idbFinancial.getName());
            iamCompanyReferenceDto.setFullName(idbFinancial.getFullName());
            iamCompanyReferenceDto.setBankNature(idbFinancial.getBankNature());
            iamCompanyReferenceDto.setProvince(idbFinancial.getCity());
        }
        quoteDto.setQuoteCompanyDto(iamCompanyReferenceDto);
    }

    final String getUserMobileTel(UserContactInfoRetDTO usersContactDto) {
        if (usersContactDto != null) {
            String mobile = usersContactDto.getMobile() == null ? "" : usersContactDto.getMobile();
            String telephone = usersContactDto.getTelephone() == null ? "" : usersContactDto.getTelephone();
            if (mobile.contains(";")) {
                mobile = mobile.replace(";", "");
            }
            if (telephone.contains(";")) {
                telephone = telephone.replace(";", "");
            }
            if (StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(telephone)) {
                return mobile + "," + telephone;
            } else {
                return mobile + telephone;
            }
        }
        return "";
    }
    private BABQuotePriceType getBABQuotePriceType(BABAcceptingCompanyType acceptingCompanyType, BABBillType billType){
        if(billType == BABBillType.BKB){
            BABQuotePriceType babQuotePriceType = getBabQuotePriceType(acceptingCompanyType);
            if (babQuotePriceType != null){
                return babQuotePriceType;
            }
        }
        return null;
    }

    private BABQuotePriceType getBabQuotePriceType(BABAcceptingCompanyType acceptingCompanyType) {
        for(BABQuotePriceType babQuotePriceType : BABQuotePriceType.values()){
            if(acceptingCompanyType.getCode().equals(babQuotePriceType.getCode())){
                return babQuotePriceType;
            }
        }
        return null;
    }
}
