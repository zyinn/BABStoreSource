package com.sumscope.bab.store;

import com.sumscope.cdh.sumscopezk4j.support.spring.ZooKeeperPropertyPlaceholderConfigurer;
import com.sumscope.optimus.commons.log.LogManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

/**
 * Created by wenshuai.li on 2016/11/9.
 * 多种配置文件支持类。可以支持使用jar包内的不同名称，也可以支持使用jar包外的配置文件，也支持使用ZooKeeper。
 */
@Component
@Configuration
public class PropertyConfig {

    /**
     * 启动参数
     */
    private static final String APPLICATION_ZOOKEEPER_CONNECT = "application.zookeeper.connect";
    private static final String APPLICATION_ZOOKEEPER_PATH = "application.zookeeper.path";
    private static final String APPLICATION_CONFIG_NAME = "application.config.name";
    private static final String APPLICATION_CONFIG_URL = "application.config.url";


    //本地多个配置文件路径
    private AbstractFileResolvingResource[] configFiles = null;
    //zk上多个配置文件路径
    private String[] configPaths = null;

    @Bean
    public PropertyPlaceholderConfigurer zooKeeperPropertyPlaceholderConfigurer() throws MalformedURLException {
        preparePropertiesByEnv();
        String zkConnect = System.getProperty(APPLICATION_ZOOKEEPER_CONNECT);
        PropertyPlaceholderConfigurer configurer;

        if(StringUtils.isNotBlank(zkConnect)){
            configurer = new ZooKeeperPropertyPlaceholderConfigurer();
            ((ZooKeeperPropertyPlaceholderConfigurer)configurer).setConnectString(zkConnect);
            ((ZooKeeperPropertyPlaceholderConfigurer)configurer).setPath(configPaths);
        }else{
            configurer = new PropertyPlaceholderConfigurer();
        }
        configurer.setIgnoreResourceNotFound(true);
        configurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
        configurer.setLocations(configFiles);
        return configurer;
    }

    private void preparePropertiesByEnv() throws MalformedURLException {
        initConfigFiles();//本地多个配置文件
        initConfigFilesByUrl(); //按文件位置进行配置
        initConfigPaths();//zk上多个配置文件path
        initDefaultConfig();

    }

    private void initDefaultConfig() {
        String configNames = System.getProperty(APPLICATION_CONFIG_NAME);
        String configUrls = System.getProperty(APPLICATION_CONFIG_URL);
        String zkConnect = System.getProperty(APPLICATION_ZOOKEEPER_CONNECT);
        //如果启动命令什么配置信息都没有设置，则默认设置为application_dev.properties
        if (StringUtils.isBlank(configNames) && StringUtils.isBlank(configUrls) && StringUtils.isBlank(zkConnect)) {
            LogManager.info("使用默认application_dev.properties配置信息。");
            configFiles = new ClassPathResource[1];
            configFiles[0] = new ClassPathResource("application_dev.properties");
        }
    }

    private void initConfigFilesByUrl() throws MalformedURLException {
        String currentConfigs = System.getProperty(APPLICATION_CONFIG_URL);
        if (StringUtils.isNotBlank(currentConfigs)) {
            String[] strs = StringUtils.split(currentConfigs, ",");
            configFiles = new UrlResource[strs.length];
            for (int i = 0; i < strs.length; i++) {
                configFiles[i] = new UrlResource(strs[i]);
            }

        }
    }

    private void initConfigFiles() {
        String currentConfigs = System.getProperty(APPLICATION_CONFIG_NAME);
        if (StringUtils.isNotBlank(currentConfigs)) {
            String[] strs = StringUtils.split(currentConfigs, ",");
            configFiles = new ClassPathResource[strs.length];
            for (int i = 0; i < strs.length; i++) {
                configFiles[i] = new ClassPathResource(strs[i]);
            }
        }
    }

    private void initConfigPaths() {
        String currentConfigs = System.getProperty(APPLICATION_ZOOKEEPER_PATH);
        if (StringUtils.isNotBlank(currentConfigs)) {
            configPaths = StringUtils.split(currentConfigs, ",");
        }

    }
}
