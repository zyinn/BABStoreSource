package com.sumscope.bab.store.dao;

import com.sumscope.bab.store.BABStoreApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by fan.bai on 2017/3/22.
 * 集成测试Redis缓存是否成功
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = BABStoreApplication.class)
@ActiveProfiles("test")
public class RedisCacheIntegrationTest {
    @Autowired
    private CacheTestDao cacheTestDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisCache(){
        resetCache();

        cacheTestDao.getUserModelByNameAndId("001", "UserA");
        assertWithSize(1,"getUserModelByNameAndId方法未能被缓存");

        cacheTestDao.getUserModelById("002");
        assertWithSize(2,"getUserModelById方法未能被缓存");

        TestUserModel updateModel = new TestUserModel();
        updateModel.setId("002");
        updateModel.setName("NewNameAfterUpdated");
        updateModel.setPrice(new BigDecimal(20.54));
        cacheTestDao.updateCacheUserModel(updateModel);

        TestUserModel modelInCache = cacheTestDao.getUserModelById("002");
        Assert.assertTrue("更新缓存失败！",modelInCache.getName().equals("NewNameAfterUpdated"));
        Assert.assertTrue("更新缓存失败！",modelInCache.getPrice().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() == 20.54);

        resetCache();

//        redisTemplate.opsForValue().set("BAB_TEST_0005",new BigDecimal(123.021));
//        redisTemplate.expire("BAB_TEST_0005",60, TimeUnit.SECONDS);
//        Object bab_test_0005 = redisTemplate.opsForValue().get("BAB_TEST_0005");
    }

    private void resetCache() {
        cacheTestDao.resetCache();
        int size = 0;
        String msg = "清除Redis缓存失败!";
        assertWithSize(size, msg);
    }

    private void assertWithSize(int size, String msg) {
        Set keys = redisTemplate.keys("*CacheTestDaoImpl*");
        Assert.assertTrue(msg,keys.size() == size);
    }
}*/
