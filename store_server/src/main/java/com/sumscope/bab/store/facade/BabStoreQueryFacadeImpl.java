package com.sumscope.bab.store.facade;

import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.store.externalinvoke.BabQuoteHttpClientHelper;
import com.sumscope.bab.store.externalinvoke.IAMEntitlementCheck;
import com.sumscope.bab.store.facade.converter.BabStoreWithInfoDtoConverter;
import com.sumscope.bab.store.facade.converter.StoreSearchParamDtoConverter;
import com.sumscope.bab.store.model.dto.StoreSearchParamDto;
import com.sumscope.bab.store.model.model.BabStoreWithInfoModel;
import com.sumscope.bab.store.model.model.StoreSearchParam;
import com.sumscope.bab.store.service.BABStoreQueryService;
import com.sumscope.optimus.commons.facade.AbstractPerformanceLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 * 查询API入口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/storeQuery", produces = MediaType.APPLICATION_JSON_VALUE)
public class BabStoreQueryFacadeImpl extends AbstractPerformanceLogFacade implements BabStoreQueryFacade {
    
    @Autowired
    private StoreSearchParamDtoConverter storeSearchParamDtoConverter;
    @Autowired
    private BabStoreWithInfoDtoConverter babStoreWithInfoDtoConverter;
    @Autowired
    private BABStoreQueryService babStoreQueryService;
    @Autowired
    private BabQuoteHttpClientHelper babQuoteHttpClientHelper;
    @Autowired
    private IAMEntitlementCheck iamEntitlementCheck;

    @Override
    @RequestMapping(value = "/searchStoresByParam", method = RequestMethod.POST)
    public void searchStoresByParam(HttpServletRequest request, HttpServletResponse response, @RequestBody StoreSearchParamDto param) {
        performWithExceptionCatch(response, () -> {
            String userId = iamEntitlementCheck.getUserIdAndCheckValidUser(request);
            StoreSearchParam storeSearchParam = storeSearchParamDtoConverter.convertToModel(param);
            List<BabStoreWithInfoModel> babStoreWithInfoModels = babStoreQueryService.searchStoresByParam(storeSearchParam, userId);
            List<QuotePriceTrendsDto> quotePriceTrendsDtos = babQuoteHttpClientHelper.retrieveCurrentSSRPriceTrend();
            return  babStoreWithInfoDtoConverter.convertToDtoList(babStoreWithInfoModels,quotePriceTrendsDtos);
        });
    }
}
