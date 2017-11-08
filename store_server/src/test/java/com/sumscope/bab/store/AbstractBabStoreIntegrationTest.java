package com.sumscope.bab.store;

import com.sumscope.bab.store.commons.BabStoreConstant;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * Created by fan.bai on 2016/12/12.
 * BAB 库存系统集成测试抽象父类。所有集成测试子类集成于该父类。
 * 本父类定义了Unit Test的启动方法和实际profile环境（test）。在此环境下，我们将使用H2内存数据库
 * 用于集成测试。
 * 并且预定义了两个jdbcTemplate类，子类可用于数据库直接查询。
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BABStoreApplication.class)
@ActiveProfiles("test")
public abstract class AbstractBabStoreIntegrationTest {
    @Autowired
    @Qualifier(BabStoreConstant.BUSINESS_DATA_SOURCE)
    private DataSource businessDataSource;

    private JdbcTemplate businessJdbcTemplate;

    /**
     * 测试环境下业务数据库的JdbcTemplate，可用于子类直连数据库操作。
     * @return 业务数据库JdbcTemplate
     */
    protected JdbcTemplate getBusinessJdbcTemplate(){
        if(businessJdbcTemplate == null){
            businessJdbcTemplate = new JdbcTemplate(businessDataSource);
        }
        return businessJdbcTemplate;
    }

}
