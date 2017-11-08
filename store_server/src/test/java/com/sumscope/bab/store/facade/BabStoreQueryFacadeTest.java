package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.externalinvoke.BabQuoteHttpClientHelper;
import com.sumscope.bab.store.facade.converter.BabStoreWithInfoDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreSearchParamDtoConverter;
import com.sumscope.bab.store.model.dto.BabStoreWithInfoDto;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.service.BABStoreQueryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 * 库存信息查询功能test测试
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bill_info_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bab_store_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class BabStoreQueryFacadeTest  extends AbstractBabStoreIntegrationTest {

    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;
    @Autowired
    private BabStoreWithInfoDtoConverter babStoreWithInfoDtoConverter;
    @Autowired
    private BABStoreQueryService babStoreQueryService;
    @Autowired
    private BabQuoteHttpClientHelper babQuoteHttpClientHelper;

    @Test
    public void searchStoresByParamTest() throws Exception{
        String userID="ff8081814989c8b1014bfd34e0677333";
        StoreSearchParamDto param = new StoreSearchParamDto();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateEnd="2017-03-23 00:00:00";
        String dateStart="2017-03-22 00:00:00";
        Date end = format.parse(dateEnd);
        Date start = format.parse(dateStart);
        param.setBillDueDateStart(start);
        param.setBillDueDateEnd(end);
        param.setPaging(false);

        StoreSearchParam storeSearchParam = storeSearchParamDtoConverter.convertToModel(param);
        Assert.assertTrue("查询库存信息参数转换失败",storeSearchParam!=null);
        List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreQueryService.searchStoresByParam(storeSearchParam, userID);
        Assert.assertTrue("查询库存信息失败",babStoreWithInfoModels!=null&&babStoreWithInfoModels.size()>0);

        List<QuotePriceTrendsDto> quotePriceTrendsDtos = babQuoteHttpClientHelper.retrieveCurrentSSRPriceTrend();

        List<BabStoreWithInfoDto> babStoreWithInfoDtos = babStoreWithInfoDtoConverter.convertToDtoList(babStoreWithInfoModels, quotePriceTrendsDtos);
        Assert.assertTrue("查询库存信息失败",babStoreWithInfoDtos!=null && babStoreWithInfoDtos.size()>0);
    }


}
