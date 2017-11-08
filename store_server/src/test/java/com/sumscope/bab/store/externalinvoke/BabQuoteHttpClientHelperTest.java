package com.sumscope.bab.store.externalinvoke;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.facade.converter.BabDiscountToSSRQuoteDtoConverter;
import com.sumscope.bab.store.model.dto.BABPostDiscountDto;
import com.sumscope.bab.store.model.dto.BillInfoDiscountDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *redisToken 测试
 */
public class BabQuoteHttpClientHelperTest extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BabQuoteHttpClientHelper quoteHttpClientHelper;
    @Autowired
    private BabDiscountToSSRQuoteDtoConverter discountToSSRQuoteDtoConverter;

    @Test
    public void retrieveCurrentSSRPriceTrendTest(){
        List<QuotePriceTrendsDto> quotePriceTrendsDtos = quoteHttpClientHelper.retrieveCurrentSSRPriceTrend();
//        Assert.assertTrue("获取票据系统报价走势失败！", quotePriceTrendsDtos != null && quotePriceTrendsDtos.size()>0);
    }

    @Test
    public void insertSSRQuotes(){
        List<BABPostDiscountDto> babPostDiscountDto = new ArrayList<>();
        String userId="ff8081814411391d01441a75c45e2a6d";
        BABPostDiscountDto dto = new BABPostDiscountDto();
        BillInfoDiscountDto infoDto = new BillInfoDiscountDto();
        infoDto.setBillType(BABBillType.BKB);
        infoDto.setAcceptingCompanyType(BABAcceptingCompanyType.GG);
        infoDto.setAmount(new BigDecimal(32600));
        infoDto.setDueDate(new Date());
        infoDto.setBillMedium(BABBillMedium.PAP);
        dto.setBillInfoDiscountDto(infoDto);
        dto.setPrice("3.36");
        QuoteProvinceDto provinceDto =  new QuoteProvinceDto();
        provinceDto.setProvinceCode("SH01");
        provinceDto.setProvinceName("上海");
        dto.setQuoteProvinces(provinceDto);
        babPostDiscountDto.add(dto);
        List<SSRQuoteDto> quoteDto = discountToSSRQuoteDtoConverter.convertToModel(babPostDiscountDto,userId);
        int ssrQuote = quoteHttpClientHelper.insertSSRQuotes(quoteDto);
        Assert.assertTrue("发布贴现失败！", ssrQuote == 1);
    }

}
