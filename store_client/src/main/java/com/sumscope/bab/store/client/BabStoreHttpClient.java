package com.sumscope.bab.store.client;

import com.sumscope.bab.quote.client.QuoteHttpInvocationException;
import com.sumscope.bab.quote.commons.util.CollectionsUtil;
import com.sumscope.bab.store.model.dto.BillInfoWithGoDownPriceDto;
import com.sumscope.bab.store.model.dto.BillNumberWithOperatorIdDto;
import com.sumscope.httpclients.commons.ExternalInvocationFailedException;
import com.sumscope.httpclients.commons.HttpInvocationUtil;
import com.sumscope.optimus.commons.exceptions.ExceptionDto;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class BabStoreHttpClient {

    private static final String GET_STORE_INFO_URL = "http://%s/bab_store/interSystem/getStoreWithInfo";
    private final String urlWithPort;

    public BabStoreHttpClient(String url){
        this.urlWithPort = url;
    }

    private <T> List<T> processReturnEntityString(String entityString, Class<T> rClass) {
        JSONObject resultJson = new JSONObject(entityString);
        int returnCode = resultJson.getInt("return_code");
        if(returnCode == 0) {
            int returnMessage1 = resultJson.getInt("result_count");
            if(returnMessage1 > 0) {
                JSONArray exceptionDto1 = resultJson.getJSONArray("result");
                return this.getDtoListFromEntityString(exceptionDto1, rClass);
            } else {
                return new ArrayList();
            }
        } else {
            JSONObject returnMessage = resultJson.getJSONObject("return_message");
            ExceptionDto exceptionDto = (ExceptionDto)JsonUtil.readValue(returnMessage.toString(), ExceptionDto.class);
            throw new QuoteHttpInvocationException("Failed to invoke remote service", exceptionDto);
        }
    }
    private <T> List<T> getDtoListFromEntityString(JSONArray resultJson, Class<T> rClass) {
        ArrayList resultList = new ArrayList();

        for(int i = 0; i < resultJson.length(); ++i) {
            JSONObject jsonObject = resultJson.getJSONObject(i);
            Object dto = JsonUtil.readValue(jsonObject.toString(), rClass);
            resultList.add(dto);
        }

        return resultList;
    }
    private String getFullUrl(String ssrPriceTrendsUrl) {
        return String.format(ssrPriceTrendsUrl,urlWithPort);
    }

    public BillInfoWithGoDownPriceDto getBillInfoDtoByNumberWithOperatorId(BillNumberWithOperatorIdDto dto) throws ExternalInvocationFailedException {
        String paramString = JsonUtil.writeValueAsString(dto);
        String fullUrl = getFullUrl(GET_STORE_INFO_URL);
        String entityString = HttpInvocationUtil.invokeJsonEntityPostRequest(paramString, fullUrl);
        List<BillInfoWithGoDownPriceDto> dtos = processReturnEntityString(entityString, BillInfoWithGoDownPriceDto.class);
        if(!CollectionsUtil.isEmptyOrNullCollection(dtos)){
            return dtos.get(0);
        }
        return null;
    }

}
