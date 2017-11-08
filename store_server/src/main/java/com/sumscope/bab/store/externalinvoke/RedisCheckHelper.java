package com.sumscope.bab.store.externalinvoke;

import com.alibaba.fastjson.JSONObject;
import com.sumscope.bab.quote.commons.util.UUIDUtils;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.bab.store.model.dto.TokenModelDto;
import com.sumscope.optimus.commons.exceptions.*;
import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import com.sumscope.optimus.commons.redis.RedisClient;
import com.sumscope.optimus.commons.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 * redission 操作redis,用户报价的token出来
 */
@Component
public class RedisCheckHelper {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String QUOTETOKEN = "quoteToken";
    private static final String TOKEN = "token";
    private static final String FROMPROJECT = "FROM_PROJECT";
    private static final String EFFECTIVEDATE = "EFFECTIVE_DATE";
    private static final String EXPIREDDATE = "EXPIRED_DATE";

    @Autowired
    private RedisClient redisClient;

    /**
     * 从heard中获取web端传的quoteToken
     */
    String getQuoteTokenFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(QUOTETOKEN);
    }

    /**
     *获取并验证报价token
     */
    public void getQuoteTokenAndCheckToken(HttpServletRequest request){
        String quoteToken = getQuoteTokenFromRequest(request);
        checkTokenModelDto(quoteToken);
    }
    /**
     * 验证redis token是否过期
     */
    private void checkTokenModelDto(String key) {
        if(key == null){
            //我们允许前端不传token进行报价，此时报价可能出现重复。
            //允许的原因在于我们暂时需要支持性能测试等工作。
            return;
        }
        String tokenJedis = getTokenJedis(key);
        if(tokenJedis == null){
            throwTokenErrorException();
        }else{
            //销毁token
            destroyToken(key);
            //验证token是否过期
            TokenModelDto tokenModelDto = JsonUtil.readValue(tokenJedis, TokenModelDto.class);
            if (tokenModelDto != null && (tokenModelDto.getEffectiveDate().getTime() >= (new Date()).getTime() ||
                    tokenModelDto.getExpiredDate().getTime() <= (new Date()).getTime())) {
                throwTokenErrorException();
            }
        }
    }

    private void throwTokenErrorException() {
        List<ValidationExceptionDetails> detailsList = new ArrayList<>();
        detailsList.add(new ValidationExceptionDetails
                (GeneralValidationErrorType.DATA_MISSING, "TokenModelDto", "token失效!"));
        throw new ValidationException(detailsList);
    }

    /**
     *web端初始化获取报价token方法
     */
    public String getToken(){
        TokenModelDto tokenModelDto = getTokenModelDto();
        setTokenJedis(tokenModelDto);
        return tokenModelDto.getToken();
    }

    /**
     * 设置token到redis
     */
    void setTokenJedis(TokenModelDto tokenModelDto) {
        try {
            String key = tokenModelDto.getToken();//key
            String value = doTokenModelDto(tokenModelDto);//value
            int autoDestroyTime =  BabStoreConstant.EXPIRED_DATE; //destroyTime
            // 为给定key设置生存时间。当key过期时，它会被自动删除
            redisClient.set(key, value,autoDestroyTime);
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "将报价token存入redis中出错！", e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "redis存入token失败!" + e);
        }
    }
    void destroyToken(String token){
        try {
            redisClient.delKeys(token);//删除token
        } catch (Exception e) {
            LogStashFormatUtil.logError(logger, "删除redis中token出错！", e);
            throw new BusinessRuntimeException(BusinessRuntimeExceptionType.OTHER, "删除redis中token失败!" + e);
        }
    }
    /**
     * 获取redis中的token
     */
    String getTokenJedis(String key) {
        return redisClient.get(key);
    }

    TokenModelDto getTokenModelDto() {
        long effectiveDates = new Date().getTime() / 1000 + BabStoreConstant.EFFECTIVE_DATE;
        long expiredDates = new Date().getTime() / 1000 + BabStoreConstant.EXPIRED_DATE;

        TokenModelDto dto = new TokenModelDto();
        dto.setToken(BabStoreConstant.FROM_PROJECT +UUIDUtils.generatePrimaryKey());
        dto.setEffectiveDate(new Date(effectiveDates * 1000));
        dto.setExpiredDate(new Date(expiredDates * 1000));
        dto.setFromProject(BabStoreConstant.FROM_PROJECT);
        return dto;
    }

    private String doTokenModelDto(TokenModelDto tokenModelDto) {
        JSONObject json = new JSONObject();
        json.put(TOKEN, tokenModelDto.getToken());
        json.put(FROMPROJECT, BabStoreConstant.FROM_PROJECT);
        json.put(EFFECTIVEDATE, tokenModelDto.getEffectiveDate());
        json.put(EXPIREDDATE, tokenModelDto.getExpiredDate());
        return json.toJSONString();
    }

}
