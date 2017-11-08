package com.sumscope.bab.store.service;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BABBillStatus;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.bab.store.model.model.BabStoreModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.model.model.StoreOutModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bill_info_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bab_store_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class BABStoreManagementServiceTest  extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BABStoreManagementService babStoreManagementService;
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;

    @Test
    public void outStore(){
        String userId = "ff8081814989c8b1014bfd34e0677333";
        StoreOutModel model = new StoreOutModel();
        model.setOperatorId(userId);
        model.setOutDate(new Date());
        model.setOutPrice(new BigDecimal(10.1));
        model.setOutType(BABStoreOutType.OFK);
        List<String> list = new ArrayList<>();
        list.add("005c5df544694b7e8d6a3f1544118d6a");
        model.setIds(list);
        List<BabStoreModel> babStoreModels = babStoreManagementService.outStoreInTransaction(model, userId);
        Assert.assertTrue("出库失败！",babStoreModels!=null && babStoreModels.size()>0);
    }

    @Test
    public void goDownStoresInTest(){
        String userId="ff8081814411391d01441a75c45e2a6d";
        List<StoreGoDownDto> list = new ArrayList<>();
        StoreGoDownDto goDownDto = getStoreGoDownDto();
        list.add(goDownDto);

        List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertToModelList(list);
        Assert.assertTrue("库存入库参数dto转model失败！",storeGoDownModels!=null && storeGoDownModels.size()>0);
        List<String> strings = babStoreManagementService.goDownStoresInTransaction(storeGoDownModels, userId);
        Assert.assertTrue("库存入库失败！",strings!=null && strings.size()>0);
    }

    private StoreGoDownDto getStoreGoDownDto() {
        StoreGoDownDto goDownDto = new StoreGoDownDto();
        goDownDto.setGodownType(BABStoreGoDownType.BYI);
        goDownDto.setMemo("test测试");
        goDownDto.setGodownDate(new Date(1599599361271L));
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto dto = new BillInfoDto();
        dto.setBillNumber("1234565891234566");
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
