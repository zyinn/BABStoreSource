package com.sumscope.bab.store.externalinvoke;

import com.sumscope.bab.store.AbstractBabStoreIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *redisToken 测试
 */
public class RedisTokenTest extends AbstractBabStoreIntegrationTest {
    @Autowired
    private RedisCheckHelper redisUtils;

    @Test
    public void redisTokenDtoTest(){
        String token = redisUtils.getToken();
        Assert.assertTrue("获取RedisToken失败！", token != null);
        redisUtils.destroyToken(token);
        String BTokenRedis= redisUtils.getTokenJedis(token);
        Assert.assertTrue("删除RedisToken失败！", BTokenRedis == null);
    }


}
