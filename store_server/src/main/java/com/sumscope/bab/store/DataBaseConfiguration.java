package com.sumscope.bab.store;

import com.alibaba.druid.pool.DruidDataSource;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.optimus.commons.log.LogManager;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter(PropertyConfig.class)
public class DataBaseConfiguration {
    @Value("${application.datasource-primary.url}")
    private String primaryUrl;
    @Value("${application.datasource-primary.driverClassName}")
    private String primaryDriverClassName;
    @Value("${application.datasource-primary.username}")
    private String primaryUserName;
    @Value("${application.datasource-primary.password}")
    private String primaryPassword;
    @Value("${application.datasource-primary.min-idle}")
    private String primaryMinIdle;
    @Value("${application.datasource-primary.max-active}")
    private String primaryMaxActive;
    @Value("${application.datasource-primary.initial-size}")
    private String primaryInitialSize;
    @Value("${application.datasource-primary.validation-query}")
    private String primaryValidationQuery;

    @Bean(name = BabStoreConstant.BUSINESS_DATA_SOURCE, destroyMethod = "close", initMethod = "init")
    @Primary
    public DataSource dataSource() {
        LogManager.debug("Configruing DataSource");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(primaryUrl);
        datasource.setDriverClassName(primaryDriverClassName);
        datasource.setMaxActive(NumberUtils.toInt(primaryMaxActive));
        datasource.setMinIdle(NumberUtils.toInt(primaryMinIdle));
        datasource.setInitialSize(NumberUtils.toInt(primaryInitialSize));
        datasource.setValidationQuery(primaryValidationQuery);
        datasource.setUsername(primaryUserName);
        datasource.setPassword(primaryPassword);

        return datasource;
    }

}