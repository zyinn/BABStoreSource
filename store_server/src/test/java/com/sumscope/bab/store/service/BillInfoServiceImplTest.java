package com.sumscope.bab.store.service;

import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.model.model.BillInfoModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

/**
 * Created by Administrator on 2017/4/10.
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bill_info_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
@Sql(scripts = {"/com/sumscope/bab/store/bab_store_init_data.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class BillInfoServiceImplTest  extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BillInfoServiceImpl billInfoService;

    @Test
    public void getAndValidateBillInfoInDBTest(){
        String infoId = "055f89c692c747b5a2793ba2e52b864c";
        String operatorId = "ff808181431eed020143b7cab7851b9a";
        String billNumber = "B6C9E250704C8899";
        BillInfoModel billInfoModel = billInfoService.getAndValidateBillInfoInDB(infoId, operatorId, billNumber);
        Assert.assertTrue("票据验证失败！",billInfoModel!=null);
    }

    @Test
    public void checkBillInfoByNumberTest(){
        String billNumber = "B6C9E250704C8899";
        String operatorId = "ff808181431eed020143b7cab7851b9a";
        BillInfoUsage billInfoUsage = billInfoService.checkBillInfoByNumber(billNumber, operatorId);
        Assert.assertTrue("验证票据使用情况失败！",billInfoUsage!=null && billInfoUsage == BillInfoUsage.USE_BY_CURRENT);
    }

    @Test
    public void setupBillInfoTest(){
        String operatorId = "ff808181431eed020143b7cab7851b9a";
        BillInfoModel billInfoModel = new BillInfoModel();
        billInfoModel.setBillNumber("B6C9E250704C8899");
        billInfoModel.setId("055f89c692c747b5a2793ba2e52b864c");
        billInfoModel.setOperatorId(operatorId);
        billInfoModel.setAcceptingCompanyName("test");
        billInfoModel.setDrawerName("test");
        BillInfoModel infoModel = billInfoService.setupBillInfo(billInfoModel, operatorId);
        Assert.assertTrue("更新票据失败！",infoModel!=null && infoModel.getDrawerName().equals("test"));
    }
}
