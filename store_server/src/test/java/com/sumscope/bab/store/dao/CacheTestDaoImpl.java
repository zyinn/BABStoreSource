package com.sumscope.bab.store.dao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by fan.bai on 2017/3/23.
 * 实例类
 */
@Component
public class CacheTestDaoImpl implements CacheTestDao {
    public TestUserModel getUserModelById(String userId) {
        TestUserModel model = new TestUserModel();
        model.setId(userId);
        model.setName("TestName");
        model.setPrice(new BigDecimal(19.12));
        return model;
    }

    public TestUserModel updateCacheUserModel(TestUserModel newUserModel) {
        return newUserModel;
    }

    public void resetCache() {

    }

    public TestUserModel getUserModelByNameAndId(String userId, String userName) {
//        System.out.println("getUserModelByNameAndId() 被触发");
        TestUserModel model = new TestUserModel();
        model.setId(userId);
        model.setName(userName);
        model.setPrice(new BigDecimal(102.815));
        return model;
    }


}
