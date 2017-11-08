package com.sumscope.bab.store.service;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BABBillStatus;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.commons.enums.BABStoreOutType;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;
import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreSearchParamDtoConverter;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.*;
import com.sumscope.optimus.commons.exceptions.ValidationExceptionDetails;
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
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bill_info_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bab_store_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class BABStoreManageServiceTest extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BABStoreManageServiceImpl babStoreManageService;
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;
    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;
    @Autowired
    private BABStoreQueryService babStoreService;

    @Test
    public void checkExistingAndValidateTest(){
        List<String> ids = new ArrayList<>();
        ids.add("005c5df544694b7e8d6a3f1544118d6a");
        String operatorId = "ff8081814989c8b1014bfd34e0677333";
        List<BabStoreModel> babStoreModels = babStoreManageService.checkExistingAndValidate(ids, operatorId);
        Assert.assertTrue("验证失败！",babStoreModels!=null && babStoreModels.size()>0);
    }

    @Test
    public void getBabStoreModelValidationTest(){
        List<ValidationExceptionDetails> detailsList = new ArrayList<>();
        String id = "005c5df544694b7e8d6a3f1544118d6a";
        String operatorId = "ff8081814989c8b1014bfd34e0677333";
        BabStoreModel babStoreModelValidation = babStoreManageService.getBabStoreModelValidation(operatorId, detailsList, id);
        Assert.assertTrue("验证失败！",babStoreModelValidation!=null);
    }

    @Test
    public void updateStoreTest(){
        StoreGoDownDto goDownDto = new StoreGoDownDto();
        goDownDto.setGodownType(BABStoreGoDownType.BYI);
        goDownDto.setMemo("test测试22");
        goDownDto.setGodownDate(new Date(1599599361271L));
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto dto = new BillInfoDto();
        dto.setId("055f89c692c747b5a2793ba2e52b864c");
        dto.setBillNumber("1234565891234569");
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
        goDownDto.setId("005c5df544694b7e8d6a3f1544118d6a");
        String userId ="ff8081814989c8b1014bfd34e0677333";
        StoreGoDownModel goDownModel = storeGoDownDtoConverter.convertToModel(goDownDto);
        babStoreManageService.updateStore(goDownModel,userId);

        List<ValidationExceptionDetails> detailsList = new ArrayList<>();
        BabStoreModel babStoreModel = babStoreManageService.getBabStoreModelValidation(userId, detailsList, "005c5df544694b7e8d6a3f1544118d6a");

        Assert.assertTrue("更新失败！",babStoreModel.getGodownDate().getTime()==new Date(1599599361271L).getTime());
    }

    @Test
    public void outStoreTest(){
        String operatorId="ff8081814989c8b1014bfd34e0677333";
        StoreOutModel outModel = new StoreOutModel();
        outModel.setOperatorId(operatorId);
        outModel.setOutType(BABStoreOutType.DSC);
        outModel.setOutPrice(new BigDecimal(3.26));
        outModel.setOutDate(new Date());
        outModel.setCounterPartyName("测试出库");
        List<String> list = new ArrayList<>();
        list.add("005c5df544694b7e8d6a3f1544118d6a");
        outModel.setIds(list);
        List<BabStoreModel> babStoreModels = babStoreManageService.outStore(outModel, operatorId);
        Assert.assertTrue("出库失败！",babStoreModels!=null && babStoreModels.size()>0 && babStoreModels.get(0).getStoreStatus()== BABStoreStatus.OTS);
    }

    @Test
    public void cancelStoreTest() throws Exception{
        List<String> list = new ArrayList<>();
        list.add("005c5df544694b7e8d6a3f1544118d6a");
        String operatorId = "ff8081814989c8b1014bfd34e0677333";
        babStoreManageService.cancelStore(list,operatorId);

        //删除库存中的id=005c5df544694b7e8d6a3f1544118d6a的数据
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
        List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreService.searchStoresByParam(storeSearchParam, operatorId);
        Assert.assertTrue("库存删除失败！",babStoreWithInfoModels!=null&&babStoreWithInfoModels.size()==1);
    }

    @Test
    public void searchStoresByBillNumberTest(){
        BillNumberWithOperatorIdDto searchParam = new BillNumberWithOperatorIdDto();
        searchParam.setOperatorId("ff8081814989c8b1014bfd34e0677344");
        searchParam.setBillNumber("26C9E240704C99CB16C9E240704888");
        BabStoreWithInfoModel babStoreWithInfoModel = babStoreManageService.searchStoresByBillNumber(searchParam);
        Assert.assertTrue("争取一次性成功",babStoreWithInfoModel!=null);
    }

}




















