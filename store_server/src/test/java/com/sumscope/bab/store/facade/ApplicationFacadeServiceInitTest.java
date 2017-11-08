package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.model.dto.AppInitialDataDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 * 页面初始化test
 */
public class ApplicationFacadeServiceInitTest  extends AbstractBabStoreIntegrationTest {
    @Autowired
    private ApplicationFacadeService applicationFacadeService;

    @Test
    public void getStoreViewInitDataTest(){
        List<AppInitialDataDto> initData = applicationFacadeService.getInitData();
        Assert.assertTrue("页面初始化失败！",initData!=null && initData.size()>0);
    }
}
