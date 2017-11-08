package com.sumscope.bab.store.externalinvoke;

import com.sumscope.bab.quote.client.BabQuoteHttpClient;
import com.sumscope.bab.quote.model.dto.QuotePriceTrendsDto;
import com.sumscope.bab.quote.model.dto.SSRQuoteDto;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.optimus.commons.exceptions.*;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 * quoteInterSystem 获取价格趋势 and 发布贴现
 */
@Component
public class BabQuoteHttpClientHelper {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BabQuoteHttpClient babQuoteHttpClient;

    @Cacheable(BabStoreConstant.QUOTE_CACHING_NAME)
    public List<QuotePriceTrendsDto> retrieveCurrentSSRPriceTrend(){
        try {
            return babQuoteHttpClient.retrieveCurrentSSRPriceTrends();
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"获取票据系统的报价价格统计信息失败！",e);
        }
        return null;
    }

    public int insertSSRQuotes(List<SSRQuoteDto> quoteDtos){
        try {
            List<SSRQuoteDto> ssrQuoteDtos = babQuoteHttpClient.insertSSRQuotes(quoteDtos);
            return ssrQuoteDtos!=null ? ssrQuoteDtos.size() : 0;
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger,"库存发布贴现失败！",e);
        }
        return 0;
    }
}
