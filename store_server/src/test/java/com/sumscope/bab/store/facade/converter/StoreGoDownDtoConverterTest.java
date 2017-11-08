package com.sumscope.bab.store.facade.converter;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import com.sumscope.bab.store.commons.enums.BABBillStatus;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */
public class StoreGoDownDtoConverterTest extends AbstractBabStoreIntegrationTest {
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;

    @Test
    public void convertToModelListTest(){
        List<StoreGoDownDto> list = new ArrayList<>();
        StoreGoDownDto goDownDto = getStoreGoDownDto("test测试");
        list.add(goDownDto);
        List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertToModelList(list);
        Assert.assertTrue("库存入库参数list dto转model失败！",storeGoDownModels!=null && storeGoDownModels.size()==1
        && storeGoDownModels.get(0).getBillInfoModelList().size()==1);
    }

    @Test
    public void convertToModelTest(){
        StoreGoDownDto goDownDto = getStoreGoDownDto("test测试22");
        goDownDto.setId("005c5df544694b7e8d6a3f1544118d6a");
        StoreGoDownModel goDownModel = storeGoDownDtoConverter.convertToModel(goDownDto);
        Assert.assertTrue("库存入库参数dto转model失败！",goDownModel!=null&&goDownModel.getBillInfoModelList().size()==1);
    }

    @Test
    public void billTypeFormatValidateTest(){
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto dto = new BillInfoDto();
        dto.setBillMedium(BABBillMedium.PAP);
        dto.setBillType(BABBillType.BKB);
        dto.setBillNumber("1111115111111111");
        billInfoDtoList.add(dto);
        storeGoDownDtoConverter.billTypeFormatValidate(billInfoDtoList,0);
        Assert.assertTrue("检验票据格式失败！",billInfoDtoList.get(0).getBillNumber().equals(dto.getBillNumber()));
    }

    private StoreGoDownDto getStoreGoDownDto(String test) {
        StoreGoDownDto goDownDto = new StoreGoDownDto();
        goDownDto.setGodownType(BABStoreGoDownType.BYI);
        goDownDto.setMemo(test);
        goDownDto.setGodownDate(new Date(1599599361271L));
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto dto = new BillInfoDto();
        dto.setBillNumber("1111115111111111");
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
