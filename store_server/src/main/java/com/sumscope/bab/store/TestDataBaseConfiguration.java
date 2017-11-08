package com.sumscope.bab.store;

import com.sumscope.bab.store.commons.BabStoreConstant;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 用于测试环境的数据库链接配置。设置了profile为test，既在测试环境时使用的profle（见AbstractBabQuoteTest）。
 */
@Configuration
@EnableTransactionManagement
@Profile("test")
public class TestDataBaseConfiguration implements EnvironmentAware {

    public void setEnvironment(Environment env) {

    }

    @Bean(name = BabStoreConstant.BUSINESS_DATA_SOURCE)
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName("bab_store_1.0").build();
    }


}