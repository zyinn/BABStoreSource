package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */
public class BabStoreWithInfoDtoConverterTest extends AbstractBabStoreIntegrationTest {

    @Autowired
    private BabStoreWithInfoDtoConverter storeWithInfoDtoConverter;

    @Test
    public void convertToDtoListTest(){
        java.util.List<BabStoreWithInfoModel> storeWithInfoModel = new ArrayList<>();
        List<QuotePriceTrendsDto > quotePriceTrendsDtoList = new ArrayList<>();
        QuotePriceTrendsDto dto = new QuotePriceTrendsDto();

        quotePriceTrendsDtoList.add(dto);
//        storeWithInfoDtoConverter.convertToDtoList()
    }
}
