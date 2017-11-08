package com.sumscope.bab.store;

import com.sumscope.bab.store.externalinvoke.HttpClientsMsgListenerManager;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by fan.bai on 2016/4/28.
 * 总线监听及信息发布相关配置
 */
@Configuration
public class MessageBusConfiguration implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment env) {

    }

    //client总线初始化
    @Bean(initMethod = "start")
    public HttpClientsMsgListenerManager gatewayinvokeBusManager(){
        return new HttpClientsMsgListenerManager();
    }
}
