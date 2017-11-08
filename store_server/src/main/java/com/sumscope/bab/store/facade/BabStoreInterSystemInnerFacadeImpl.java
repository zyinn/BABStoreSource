package com.sumscope.bab.store.facade;

import com.sumscope.bab.store.facade.converter.StoreGoDownDtoConverter;
import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.bab.store.model.dto.StoreGoDownDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreGoDownModel;
import com.sumscope.bab.store.service.BABStoreManagementService;
import com.sumscope.bab.store.service.BABStoreQueryService;
import com.sumscope.bab.store.service.BillInfoQueryService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/interSystem", produces = MediaType.APPLICATION_JSON_VALUE)
public class BabStoreInterSystemInnerFacadeImpl extends AbstractPerformanceLogFacade implements BabStoreInterSystemInnerFacade {
    @Autowired
    private StoreGoDownDtoConverter storeGoDownDtoConverter;
    @Autowired
    private BABStoreManagementService babStoreManagementService;
    @Autowired
    private BillInfoQueryService billInfoQueryService;
    @Autowired
    private BABStoreQueryService babStoreQueryService;

    @Override
    @RequestMapping(value = "/getStoreWithInfoByDealIds", method = RequestMethod.POST)
    public void getStoreWithInfoByDealIds(HttpServletRequest request, HttpServletResponse response,@RequestBody String dealIds) {
        performWithExceptionCatch(response,()->{
           return null;
        });
    }

    @Override
    @RequestMapping(value = "/createGoDownStore", method = RequestMethod.POST)
    public void createGoDownStore(HttpServletRequest request, HttpServletResponse response,@RequestBody StoreGoDownDto goDownDto) {
        performWithExceptionCatch(response,()->{
            StoreGoDownModel storeGoDownModel = storeGoDownDtoConverter.convertToModel(goDownDto);
            List<StoreGoDownModel> storeGoDownModels = storeGoDownDtoConverter.convertModelToList(storeGoDownModel);
            return babStoreManagementService.goDownStoresInTransaction(storeGoDownModels,"");
        });
    }

    @Override
    @RequestMapping(value = "/getStoreWithInfo", method = RequestMethod.POST)
    public void getStoreWithInfoByNumberWithOperatorId(HttpServletRequest request, HttpServletResponse response,@RequestBody BillNumberWithOperatorIdDto dto) {
        performWithExceptionCatch(response,()->{
            BillNumberWithOperatorIdDto searchParam = storeGoDownDtoConverter.converterBillNumberWithOperatorIdDtoToModel(dto);
            BabStoreWithInfoModel babStoreWithInfoModel = babStoreQueryService.searchStoresByBillNumber(searchParam);
            return storeGoDownDtoConverter.converterBabStoreWithInfoModelToDto(babStoreWithInfoModel);
        });
    }
}
