package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.quote.model.dto.QuoteProvinceDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BABBillStatus;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.externalinvoke.BabQuoteHttpClientHelper;
import com.sumscope.bab.store.facade.converter.BabDiscountToSSRQuoteDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreSearchParamDtoConverter;
import com.sumscope.bab.store.model.dto.*;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.service.BABStoreManagementService;
import com.sumscope.bab.store.service.BABStoreQueryService;
import com.sumscope.bab.store.service.BillInfoQueryService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 * storeManagementFacade测试
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bill_info_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bab_store_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class BabStoreManagementFacadeTest extends AbstractBabStoreIntegrationTest{

    @Autowired
    private BillInfoQueryService billInfoQueryService;
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;
    @Autowired
    private BABStoreManagementService babStoreManagementService;
    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;
    @Autowired
    private BABStoreQueryService babStoreService;
    @Autowired
    private BabDiscountToSSRQuoteDtoConverter discountToSSRQuoteDtoConverter;
    @Autowired
    private BabQuoteHttpClientHelper babQuoteHttpClientHelper;

    @Test
    public void getAndCheckBillInfoTest(){
        String userID="ff8081814411391d01441a75c45e2a6d";
        String billNumber="26C9E240704C99CB16C9E240704C99";
        BillInfoUsage billInfoUsage = billInfoQueryService.checkBillInfoByNumber(billNumber, userID);
        Assert.assertTrue("验证票据使用信息失败",billInfoUsage!=null);
        BillInfoModel billInfo = billInfoQueryService.getBillInfoByNumber(billNumber, userID);
        Assert.assertTrue("获取票据信息失败",billInfo!=null);
        BillInfoWithUsageDto billInfoWithUsageDto = storeGoDownDtoConverter.convertBillInfoWithUsageDto(billInfoUsage, billInfo);
        Assert.assertTrue("验证票据信息使用状态和并获取票据信息转换失败",billInfoWithUsageDto!=null && billInfoWithUsageDto.getBillInfoDto()!=null);
    }

    @Test
    public void createGoDownStoreTest(){
        String userId="ff8081814411391d01441a75c45e2a6d";
        List<StoreGoDownDto> list = new ArrayList<>();
        StoreGoDownDto goDownDto = getStoreGoDownDto("test测试", "1234565891234565");
        list.add(goDownDto);

        List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertToModelList(list);
        Assert.assertTrue("库存入库参数dto转model失败！",storeGoDownModels!=null && storeGoDownModels.size()>0);
        List<String> strings = babStoreManagementService.goDownStoresInTransaction(storeGoDownModels, userId);
        Assert.assertTrue("库存入库失败！",strings!=null);
    }

    @Test
    public void updateGoDownStore() throws Exception{
        StoreGoDownDto goDownDto = getStoreGoDownDto("test测试22", "1234565891234565");
        goDownDto.setId("005c5df544694b7e8d6a3f1544118d6a");
        String userId ="ff8081814989c8b1014bfd34e0677333";
        StoreGoDownModel goDownModel = storeGoDownDtoConverter.convertToModel(goDownDto);
        babStoreManagementService.updateStoreInTransaction(goDownModel,userId);
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
        List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreService.searchStoresByParam(storeSearchParam, userId);
        Assert.assertTrue("修改成功！",babStoreWithInfoModels!=null&&babStoreWithInfoModels.size()==2);

    }

    @Test
    public void cancelStoreTest() throws Exception{
        List<String> storeIdList = new ArrayList<>();
        storeIdList.add("035da806d065405998132b6450b91dfe");
        String userId ="ff8081814989c8b1014bfd34e0677333";
        babStoreManagementService.cancelStoreInTransaction(storeIdList,userId);
        //删除库存中的id=035da806d065405998132b6450b91dfe的数据
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
        List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreService.searchStoresByParam(storeSearchParam, userId);
        Assert.assertTrue("库存删除失败！",babStoreWithInfoModels!=null&&babStoreWithInfoModels.size()==1);
    }

    @Test
    public void postDiscount(){
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

        List<SSRQuoteDto> quoteDtos = discountToSSRQuoteDtoConverter.convertToModel(babPostDiscountDto,userId);
        Assert.assertTrue("发布贴现dto转换失败!",quoteDtos!=null&&quoteDtos.size()==1);

        int i = babQuoteHttpClientHelper.insertSSRQuotes(quoteDtos);
        Assert.assertTrue("发布贴现失败!",i==1);
    }

    private StoreGoDownDto getStoreGoDownDto(String test测试, String billNumber) {
        StoreGoDownDto goDownDto = new StoreGoDownDto();
        goDownDto.setGodownType(BABStoreGoDownType.BYI);
        goDownDto.setMemo(test测试);
        goDownDto.setGodownDate(new Date(1599599361271L));
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto dto = new BillInfoDto();
        dto.setBillNumber(billNumber);
        dto.setOperatorId("1c497085d6d511e48ec3000c29a13c19");
        dto.setBillMedium(BABBillMedium.PAP);
        dto.setBillType(BABBillType.BKB);
        dto.setAmount(new BigDecimal(1));
        dto.setPayeeName("test 单元测试");
        dto.setDrawerName("test 单元测试");
        dto.setAcceptingCompanyName("test");
        dto.setAcceptingCompanyType(BABAcceptingCompanyType.GG);
        dto.setBillStartDate(new Date(1599599361271L));
        dto.setBillDueDate(new Date(1599599361271L));
        dto.setCreateDate(new Date());
        dto.setLatestUpdateDate(new Date());
        dto.setBillStatus(BABBillStatus.VLD);
        billInfoDtoList.add(dto);
        goDownDto.setBillInfoDtoList(billInfoDtoList);
        return goDownDto;
    }
}
