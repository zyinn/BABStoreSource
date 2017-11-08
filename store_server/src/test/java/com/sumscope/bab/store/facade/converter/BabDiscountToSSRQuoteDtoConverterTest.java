package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.externalinvoke.EdmHttpClientHelperWithCache;
import com.sumscope.bab.store.model.dto.BABPostDiscountDto;
import com.sumscope.bab.store.model.dto.BillInfoDiscountDto;
import com.sumscope.iam.edmclient.edmsource.dto.UserContactInfoRetDTO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 * 发布贴现converter
 */
public class BabDiscountToSSRQuoteDtoConverterTest extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BabDiscountToSSRQuoteDtoConverter converter;
    @Autowired
    private EdmHttpClientHelperWithCache edmHttpClientWithCache;

    @Test
    public void convertToModel(){
        List<BABPostDiscountDto> babPostDiscountDto = setBabPostDiscountDtoInitData();
        String operatorId="1c497085d6d511e48ec3000c29a13c19";
        List<SSRQuoteDto> ssrQuoteDtos = converter.convertToModel(babPostDiscountDto, operatorId);
        Assert.assertTrue("发布贴现转换失败",ssrQuoteDtos!=null && ssrQuoteDtos.size()==1);
    }


    @Test
    public void setUserContactAndCompanyDto(){
        List<BABPostDiscountDto> babPostDiscountDto = setBabPostDiscountDtoInitData();
        String operatorId="1c497085d6d511e48ec3000c29a13c19";
        List<SSRQuoteDto> ssrQuoteDtos = converter.convertToModel(babPostDiscountDto, operatorId);
        converter.setUserContactAndCompanyDto(operatorId,ssrQuoteDtos.get(0));
        Assert.assertTrue("发布贴现转换失败",ssrQuoteDtos!=null && ssrQuoteDtos.size()==1 && ssrQuoteDtos.get(0).getQuoteCompanyDto().getID()!=null);

    }

    @Test
    public void getUserMobileTel(){
        String operatorId="ff8081814db8c8a1014dc2301c1c160a";
        UserContactInfoRetDTO usersContactDto = edmHttpClientWithCache.getUserContactInfoRetDTO(operatorId);
        String userMobileTel = converter.getUserMobileTel(usersContactDto);
        Assert.assertTrue("发布贴现联系方式转换失败！",userMobileTel!=null && userMobileTel!="");
    }


    private List<BABPostDiscountDto> setBabPostDiscountDtoInitData() {
        List<BABPostDiscountDto> babPostDiscountDto = new ArrayList<>();
        BABPostDiscountDto dto = new BABPostDiscountDto();
        QuoteProvinceDto provinceDto = new QuoteProvinceDto();
        provinceDto.setProvinceCode("SH01");
        provinceDto.setProvinceName("上海");
        dto.setQuoteProvinces(provinceDto);
        dto.setPrice("2.36");
        BillInfoDiscountDto billInfoDiscountDto = new BillInfoDiscountDto();
        billInfoDiscountDto.setAmount(new BigDecimal(2.366));
        billInfoDiscountDto.setBillType(BABBillType.BKB);
        billInfoDiscountDto.setBillMedium(BABBillMedium.PAP);
        billInfoDiscountDto.setAcceptingCompanyName("上海银行");
        billInfoDiscountDto.setAcceptingCompanyType(BABAcceptingCompanyType.GG);
        billInfoDiscountDto.setDueDate(new Date());
        dto.setBillInfoDiscountDto(billInfoDiscountDto);
        babPostDiscountDto.add(dto);
        return babPostDiscountDto;
    }

}
