package com.sumscope.bab.store.dao;

import com.sumscope.bab.store.commons.BabStoreConstant;
import org.springframework.cache.annotation.*;

/**
 * Created by fan.bai on 2017/3/22.
 * 测试用类
 */
@CacheConfig(cacheNames = {BabStoreConstant.DEFAULT_CACHE})
public interface CacheTestDao {

    /**
     * 根据用户ID获取用户模型，使用固定Key值，以供@CachePut方式更新缓存
     */
    @Cacheable(key = "#root.targetClass + 'TEST' + #userId")
    TestUserModel getUserModelById(String userId);

    /**
     * 根据提供上面方法一致的Key值生成方式，返回值将更新对应缓存数据
     */
    @CachePut(key = "#root.targetClass + 'TEST' + #newUserModel.id")
    TestUserModel updateCacheUserModel(TestUserModel newUserModel);

    /**
     * 清空缓存
     */
    @CacheEvict(allEntries = true)
    void resetCache();

    /**
     * 使用系统配置的KeyGenerator生成Key值并缓存结果
     */
    @Cacheable
    TestUserModel getUserModelByNameAndId(String userId, String userName);

}
