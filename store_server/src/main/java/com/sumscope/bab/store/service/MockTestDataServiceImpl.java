package com.sumscope.bab.store.service;

import com.sumscope.bab.quote.commons.enums.BABAcceptingCompanyType;
import com.sumscope.bab.quote.commons.enums.BABBillMedium;
import com.sumscope.bab.quote.commons.enums.BABBillType;
import com.sumscope.bab.store.commons.enums.BABBillStatus;
import com.sumscope.bab.store.commons.enums.BABStoreGoDownType;
import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.model.dto.BillInfoDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/3/17.
 * mock逻辑实现
 */
@Component
public class MockTestDataServiceImpl implements MockTestDataService {
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;
    @Autowired
    private BABStoreManagementService babStoreManagementService;

    private Random random = new Random();

    private static final Double[] PRICE_CANDIDATES = {2.559,1.25,1.11,1.254,1.268,4.546,3.123,3.258,3.98,4.78,4.66,5.54,5.98};

    private static final Integer[] AMOUNT_CANDIDATES = {2354680,3224646,2400000,3000000,3000001,300000,300001,500000,10000000,10000001,50000000,50000001};

    private String[] qbUserIdArray = {"1c497085d6d511e48ec3000c29a13c19","1c497085d6d511e48ec3000c29a13c19","1b4d07bcfffe4d52a2637eb743a99f8a","1bd723555f6b11e68d7f000c29a13c19","001fe19f6c724023b93bf6717088c523","1b4d07bcfffe4d52a2637eb743a99f8a"};

    private String[] name = {"测试用户名3","测试用户名2","测试用户名3","测试用户名4","测试用户名5","测试用户名6","测试用户名7","测试用户名8"};
    private String[] billNumberPAPBKB = {"9876545070400001","9876545070400002","9876545070400003","9876545070400004","9876545070400005","9876545070400006","9876545070400007","9876545070400008"};
    private String[] billNumberPAPCMB = {"9876566070500001","9876566070500002","9876566070500003","9876566070500004","9876566070500005","9876566070500006","9876566070500007","9876566070500008"};
    private String[] billNumberELEBKB = {"101234567899876543210987450001","101234567899876543210987450002","101234567899876543210987450003","101234567899876543210987450004","101234567899876543210987450005","101234567899876543210987450006","101234567899876543210987450007","101234567899876543210987450008"};
    private String[] billNumberELECMB = {"201234567899876543210987450001","201234567899876543210987450002","201234567899876543210987450003","201234567899876543210987450004","201234567899876543210987450005","201234567899876543210987450006","201234567899876543210987450007","201234567899876543210987450008"};
    private Integer[] days={2,3,4,7,6,3,2,1,7,0,3,5,9,8};
    private String[] cmb ={"CET","SOE","LET","OTH"};
    private String[] bkb ={"GG","CS","NS","NX","NH","CZ","WZ","CW"};
    @Override
    public long generateMockTestData(long numberOfData) {
        for(long i = 0;i<numberOfData;i++){
            generateSingleMockTestData();
        }
        return numberOfData;
    }
    private void generateSingleMockTestData() {
        List<StoreGoDownDto> storeGoDownDtos = setStoreGoDownDtoList();
        List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertToModelList(storeGoDownDtos);
        babStoreManagementService.goDownStoresInTransaction(storeGoDownModels,getRandomValueFromList(qbUserIdArray));
    }
   List<StoreGoDownDto> setStoreGoDownDtoList(){
        List<StoreGoDownDto> list = new ArrayList<>();
        StoreGoDownDto storeGoDownDto = new StoreGoDownDto();
        storeGoDownDto.setGodownType(getRandomValueFromList(BABStoreGoDownType.values()));
        storeGoDownDto.setGodownPrice(new BigDecimal(getRandomValueFromList(PRICE_CANDIDATES)));
        storeGoDownDto.setGodownDate(new Date());
        storeGoDownDto.setMemo("test data 测试用例");
        List<BillInfoDto> billInfoDtoList = new ArrayList<>();
        BillInfoDto infoDto = new BillInfoDto();
        infoDto.setOperatorId(getRandomValueFromList(qbUserIdArray));
        infoDto.setCreateDate(new Date());
        infoDto.setLatestUpdateDate(new Date());
        infoDto.setBillStartDate(getDateWithOutTime(getRandomValueFromList(days)));
        infoDto.setBillDueDate(getDateWithSpecifiedTime(getRandomValueFromList(days)));
        infoDto.setAmount(new BigDecimal(getRandomValueFromList(AMOUNT_CANDIDATES)));
        infoDto.setBillMedium(getRandomValueFromList(BABBillMedium.values()));
        infoDto.setBillStatus(getRandomValueFromList(BABBillStatus.values()));
        infoDto.setBillType(getRandomValueFromList(BABBillType.values()));
        if(infoDto.getBillMedium()==BABBillMedium.ELE && infoDto.getBillType()==BABBillType.BKB){
            infoDto.setBillNumber(getRandomValueFromList(billNumberELEBKB));
            String bkbEle = getRandomValueFromList(bkb);
            infoDto.setAcceptingCompanyType(getBABAcceptingCompanyType(bkbEle));
            infoDto.setAcceptingCompanyName("电银测试机构用例");
        }
        if(infoDto.getBillMedium()==BABBillMedium.ELE && infoDto.getBillType()==BABBillType.CMB){
            infoDto.setBillNumber(getRandomValueFromList(billNumberELECMB));
            String cmbPap = getRandomValueFromList(cmb);
            infoDto.setAcceptingCompanyType(getBABAcceptingCompanyType(cmbPap));
            infoDto.setAcceptingCompanyName("电商测试机构用例");
        }
        if(infoDto.getBillMedium()==BABBillMedium.PAP && infoDto.getBillType()==BABBillType.BKB){
            infoDto.setBillNumber(getRandomValueFromList(billNumberPAPBKB));
            String bkbEle = getRandomValueFromList(bkb);
            infoDto.setAcceptingCompanyType(getBABAcceptingCompanyType(bkbEle));
            infoDto.setAcceptingCompanyName("纸银测试机构用例");
        }
        if(infoDto.getBillMedium()==BABBillMedium.PAP && infoDto.getBillType()==BABBillType.CMB){
            infoDto.setBillNumber(getRandomValueFromList(billNumberPAPCMB));
            String cmbPap = getRandomValueFromList(cmb);
            infoDto.setAcceptingCompanyType(getBABAcceptingCompanyType(cmbPap));
            infoDto.setAcceptingCompanyName("纸商测试机构用例");
        }
        infoDto.setDrawerName(getRandomValueFromList(name));
        infoDto.setPayeeName(getRandomValueFromList(name));
        billInfoDtoList.add(infoDto);
        storeGoDownDto.setBillInfoDtoList(billInfoDtoList);
        storeGoDownDto.setAdjustDays(getRandomValueFromList(days));
        list.add(storeGoDownDto);
        return list;
    }

    private <T> T getRandomValueFromList(T[] values){
        int size = values.length;
        int randomIndex = random.nextInt(size);
        return values[randomIndex];
    }

    public static Date getDateWithSpecifiedTime(int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, d);
        return calendar.getTime();
    }
    public static Date getDateWithOutTime(int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(d*10));
        return calendar.getTime();
    }
    private BABAcceptingCompanyType getBABAcceptingCompanyType(String code){
        for(BABAcceptingCompanyType type:BABAcceptingCompanyType.values()){
            if(type.getCode().equals(code)){
                return type;
            }
        }
        return null;
    }
}
