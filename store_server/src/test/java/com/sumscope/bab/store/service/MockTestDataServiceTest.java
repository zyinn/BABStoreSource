package com.sumscope.bab.store.service;

import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */
@Sql(scripts = {"/com/sumscope/bab/store/schema.sql"},config = @SqlConfig(dataSource = BabStoreConstant.BUSINESS_DATA_SOURCE))
public class MockTestDataServiceTest extends AbstractBabStoreIntegrationTest {

    @Autowired
    private MockTestDataServiceImpl mockTestDataServic;

    @Test
    public void setStoreGoDownDtoListTest(){
        List<StoreGoDownDto> storeGoDownDtos = mockTestDataServic.setStoreGoDownDtoList();
        Assert.assertTrue("mock数据生成参数失败！",storeGoDownDtos!=null && storeGoDownDtos.size()==1);
    }
    @Test
    public void generateMockTestDataTest(){
        long l = mockTestDataServic.generateMockTestData(1);
        Assert.assertTrue("mock数据失败！",l==1);
    }
}
