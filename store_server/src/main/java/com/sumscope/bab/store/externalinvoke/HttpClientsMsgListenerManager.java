package com.sumscope.bab.store.externalinvoke;

import com.sumscope.iam.edmclient.EdmHttpClientWithCache;
import com.sumscope.iam.edmclient.EdmMsgListenerManger;
import com.sumscope.iam.emclient.EmHttpClientWithCache;
import com.sumscope.iam.emclient.EmMsgListenerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 用于启动client监听器的管理类，通过Configuration类的@Bean方法启动
 */
public class HttpClientsMsgListenerManager {
    private static final int HEART_BEAT = 5;
    @Value("${application.messagebus.url}")
    private String host;
    @Value("${application.messagebus.edm_user_change_topic}")
    private String userQueueName;
    @Value("${application.messagebus.edm_company_change_topic}")
    private String companyQueueName;
    @Value("${application.messagebus.em_user_permissions_topic}")
    private String permissionsQueueName;
    @Value("${application.messagebus.port}")
    private int port;
    @Autowired
    private EdmHttpClientWithCache edmHttpClientWithCache;
    @Autowired
    private EmHttpClientWithCache emHttpClientWithCache;

    public void start() {

        EdmMsgListenerManger.startMessageListener(edmHttpClientWithCache,host,port,HEART_BEAT,companyQueueName,userQueueName);

        EmMsgListenerManager.startListener(emHttpClientWithCache,host,port,HEART_BEAT,permissionsQueueName);

    }

}
