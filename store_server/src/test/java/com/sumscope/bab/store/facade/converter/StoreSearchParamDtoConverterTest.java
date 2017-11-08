package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.enums.BABStoreStatus;
import com.sumscope.bab.store.commons.enums.WEBBillType;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.model.model.WEBBillTypeModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class StoreSearchParamDtoConverterTest extends AbstractBabStoreIntegrationTest{

    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;

    @Test
    public void convertToModelTest(){
        StoreSearchParamDto dto = new StoreSearchParamDto();
        dto.setBabStoreStatus(BABStoreStatus.ITS);
        List<WEBBillType> webBillType = new ArrayList<>();
        webBillType.add(WEBBillType.ELE_BKB);
        dto.setWebBillType(webBillType);
        StoreSearchParam param = storeSearchParamDtoConverter.convertToModel(dto);
        Assert.assertTrue("查询参数 dto转SearchParam失败！",param!=null && param.getBabStoreStatus() == BABStoreStatus.ITS);
    }

    @Test
    public void converterBABBillTypeToModelTest(){
        List<WEBBillType> webBillType = new ArrayList<>();
        webBillType.add(WEBBillType.ELE_BKB);
        List<WEBBillTypeModel> webBillTypeModels = storeSearchParamDtoConverter.converterBABBillTypeToModel(webBillType);
        Assert.assertTrue("查询参数纸银电商转换失败",webBillTypeModels!=null && webBillTypeModels.size() == 1
                && webBillTypeModels.get(0).getBillMedium()== BABBillMedium.ELE && webBillTypeModels.get(0).getBillType()== BABBillType.BKB);
    }
}
