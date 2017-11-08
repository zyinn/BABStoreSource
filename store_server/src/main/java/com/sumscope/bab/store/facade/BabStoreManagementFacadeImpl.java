package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.store.commons.enums.BillInfoUsage;
import com.sumscope.bab.store.externalinvoke.BabQuoteHttpClientHelper;
import com.sumscope.bab.store.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.store.externalinvoke.RedisCheckHelper;
import com.sumscope.bab.store.facade.converter.BabDiscountToSSRQuoteDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreOutDtoConverter;
import com.sumscope.bab.store.model.dto.*;
import com.sumscope.bab.store.model.model.BillInfoModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.model.model.StoreOutModel;
import com.sumscope.bab.store.service.BABStoreManagementService;
import com.sumscope.bab.store.service.BillInfoQueryService;
import com.sumscope.bab.store.service.MockTestDataService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/13.
 * 库存管理API入口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/management", produces = MediaType.APPLICATION_JSON_VALUE)
public class BabStoreManagementFacadeImpl extends AbstractPerformanceLogFacade implements BabStoreManagementFacade{

    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;
    @Autowired
    private BabDiscountToSSRQuoteDtoConverter discountToSSRQuoteDtoConverter;
    @Autowired
    private BABStoreManagementService babStoreManagementService;
    @Autowired
    private BillInfoQueryService billInfoQueryService;
    @Autowired
    private StoreOutDtoConverter storeOutDtoConverter;
    @Autowired
    private StoreParserFacadeService storeParserFacadeService;
    @Autowired
    private StoreParserExportsFacadeService parserExports;
    @Autowired
    private BabQuoteHttpClientHelper babQuoteHttpClientHelper;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;
    @Autowired
    private RedisCheckHelper redis;
    @Autowired
    private MockTestDataService mockTestDataService;

    @Override
    @RequestMapping(value = "/getAndCheckBillInfo", method = RequestMethod.POST)
    public void getAndCheckBillInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String billNumber) {
        performWithExceptionCatch(response, () -> {
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            BillInfoUsage billInfoUsage = billInfoQueryService.checkBillInfoByNumber(billNumber, userId);
            BillInfoModel billInfo = billInfoQueryService.getBillInfoByNumber(billNumber, userId);
            return storeGoDownDtoConverter.convertBillInfoWithUsageDto(billInfoUsage,billInfo);
        });
    }

    @Override
    @RequestMapping(value = "/createGoDownStore", method = RequestMethod.POST)
    public void createGoDownStore(HttpServletRequest request, HttpServletResponse response, @RequestBody List<StoreGoDownDto> goDownDto) {
        performWithExceptionCatch(response, () -> {
            redis.getQuoteTokenAndCheckToken(request);
            //对应的生成一个入库单，多张入库单可对应一张票据
            //多对一的关系，update接口是更新对应的已存在入库单，且web票号不可更改
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertToModelList(goDownDto);
            return babStoreManagementService.goDownStoresInTransaction(storeGoDownModels,userId);
        });
    }

    @Override
    @RequestMapping(value = "/updateGoDownStore", method = RequestMethod.POST)
    public void updateGoDownStore(HttpServletRequest request, HttpServletResponse response,@RequestBody StoreGoDownDto storeGoDownDto) {
        performWithExceptionCatch(response, () -> {
            redis.getQuoteTokenAndCheckToken(request);
            String userId =  iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            StoreGoDownModel goDownModel = storeGoDownDtoConverter.convertToModel(storeGoDownDto);
            babStoreManagementService.updateStoreInTransaction(goDownModel,userId);
            return true;
        });
    }

    @Override
    @RequestMapping(value = "/outStore", method = RequestMethod.POST)
    public void outStore(HttpServletRequest request, HttpServletResponse response,@RequestBody StoreOutDto storeOutDto) {
        performWithExceptionCatch(response, () -> {
            redis.getQuoteTokenAndCheckToken(request);
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            StoreOutModel storeOutModel = storeOutDtoConverter.convertToStoreOutModel(storeOutDto,userId);
            return babStoreManagementService.outStoreInTransaction(storeOutModel,userId);
        });
    }

    @Override
    @RequestMapping(value = "/cancelStore", method = RequestMethod.POST)
    public void cancelStore(HttpServletRequest request, HttpServletResponse response,@RequestBody List<String> storeID) {
        performWithExceptionCatch(response, () -> {
            redis.getQuoteTokenAndCheckToken(request);
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            babStoreManagementService.cancelStoreInTransaction(storeID,userId);
            return true;
        });
    }

    @Override
    @RequestMapping(value = "/postDiscount", method = RequestMethod.POST)
    public void postDiscount(HttpServletRequest request, HttpServletResponse response,@RequestBody List<BABPostDiscountDto> babPostDiscountDto) {
        performWithExceptionCatch(response, () -> {
            redis.getQuoteTokenAndCheckToken(request);
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            List<SSRQuoteDto> quoteDtos = discountToSSRQuoteDtoConverter.convertToModel(babPostDiscountDto,userId);
            return babQuoteHttpClientHelper.insertSSRQuotes(quoteDtos);
        });

    }

    @Override
    @RequestMapping(value = "/parserGoDownStores", method = RequestMethod.POST)
    public void parserGoDownStores(HttpServletRequest request, HttpServletResponse response,@RequestBody ExcelFileDto excelFileDto) {
        performWithExceptionCatch(response, () -> {
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            return storeParserFacadeService.parserExcelFile(excelFileDto,userId);
        });
    }

    @Override
    @RequestMapping(value = "/excelExports", method = RequestMethod.POST)
    public void excelExports(HttpServletRequest request, HttpServletResponse response, @RequestBody StoreSearchParamDto param){
            String userId = iamEntitlementCheck.getUserIdFromRequest(request);
            parserExports.getExcelExports(response,param,userId);
    }

    @Override
    @RequestMapping(value = "/getDataToken", method = RequestMethod.POST)
    public void getDataToken(HttpServletRequest request, HttpServletResponse response) {
        performWithExceptionCatch(response, () -> redis.getToken());
    }


    @Override
    @RequestMapping(value = "/MockDataCreation", method = RequestMethod.POST)
    public void generateMockTestData(HttpServletRequest request, HttpServletResponse response, @RequestBody Map number) {
        performWithExceptionCatch(response, () -> generateMockTestData(number));
    }

    private long generateMockTestData(Map number) {
//        TODO:增加根据配置文件是否允许生成mock数据判断，生产环境不允许使用该方法
        Integer num = (Integer)number.get("number");
        return mockTestDataService.generateMockTestData(num);
    }
}
