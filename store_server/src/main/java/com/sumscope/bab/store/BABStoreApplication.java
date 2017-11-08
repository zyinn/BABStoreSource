package com.sumscope.bab.store;

import com.sumscope.optimus.commons.log.LogStashFormatUtil;
import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.TimeZone;


/**
 * 用于开发人员本地使用mvn spring-boot:run 启动使用
 */
@SpringBootApplication
public class BABStoreApplication implements
        EmbeddedServletContainerCustomizer {

    private static final String APPLICATION_ZOOKEEPER_CONNECT = "application.zookeeper.connect";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${application.tomcat.maxConnections}")
    private int maxConnections;

    @Value("${application.tomcat.maxThreads}")
    private int maxThreads;

    @Value("${application.tomcat.minSpareThreads}")
    private int minSpareThreads;


    public static void main(String[] args) {
        SpringApplication.run(BABStoreApplication.class, args);
    }

    public void customize(
            ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        String port = setPort(configurableEmbeddedServletContainer);
        startLeaderSelector(port);
        TomcatEmbeddedServletContainerFactory tomcatFactory = (TomcatEmbeddedServletContainerFactory) configurableEmbeddedServletContainer;
        tomcatFactory.setContextPath("/bab_store");
        tomcatFactory.addConnectorCustomizers(getTomcatCustomizer());
        final TimeZone zone = TimeZone.getTimeZone("GMT+8"); //获取中国时区
        TimeZone.setDefault(zone); //设置时区
    }

    private TomcatConnectorCustomizer getTomcatCustomizer() {
        return new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocolHandler = (Http11NioProtocol) connector.getProtocolHandler();
                protocolHandler.setMaxConnections(maxConnections);
                protocolHandler.setMinSpareThreads(minSpareThreads);
                protocolHandler.setMaxThreads(maxThreads);
            }
        };

    }

    private String setPort(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        String port = System.getProperty("port");
        if (StringUtils.isNotBlank(port)) {
            Integer portInt = Integer.valueOf(port);
            configurableEmbeddedServletContainer.setPort(portInt);
        } else {
            port = "8888";
            configurableEmbeddedServletContainer.setPort(8888);
        }
        return port;

    }

    /**
     * 启动Leader选举功能
     * @param name 用于标识Leader的名称，可以使用本机localname或者应用启动的端口号等
     */
    private void startLeaderSelector(String name) {
        //从启动命令参数获取ZK连接配置，该配置类似：-Dapplication.zookeeper.connect=172.16.87.2:2181
        String zkConnect = System.getProperty(APPLICATION_ZOOKEEPER_CONNECT);
        if (StringUtils.isNotBlank(zkConnect)) {
            CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnect, new ExponentialBackoffRetry(1000, 3));
            ApplicationLeaderSelector applicationLeaderSelector = new ApplicationLeaderSelector(client, "/bab_store.leader_selector", name);
            LogStashFormatUtil.logInfo(logger, "开始选择领导者");
            client.start();
            try {
                applicationLeaderSelector.start();
            } catch (IOException e) {
                LogStashFormatUtil.logWarrning(logger, "选择领导者出现IO异常，放弃选择。");
                ApplicationLeaderEnviornment.setLeader(false);
                CloseableUtils.closeQuietly(applicationLeaderSelector);
            }
        }

    }

    private class ApplicationLeaderSelector extends LeaderSelectorListenerAdapter implements Closeable {
        private final String name;
        private final LeaderSelector leaderSelector;

        ApplicationLeaderSelector(CuratorFramework client, String path, String name) {
            this.name = name;
            leaderSelector = new LeaderSelector(client, path, this);
            //保证此实例在释放领导权后还可能获得领导权
            leaderSelector.autoRequeue();
        }

        public void start() throws IOException {
            // the selection for this instance doesn't start until the leader selector is started
            // leader selection is done in the background so this call to leaderSelector.start() returns immediately
            leaderSelector.start();
        }

        @Override
        public void close() throws IOException {
            ApplicationLeaderEnviornment.setLeader(false);
            leaderSelector.close();
        }

        @Override
        public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
            //获得领导权时该方法被调用，若不想释放领导权则不退出该方法。
            LogStashFormatUtil.logInfo(logger, LogStashFormatUtil.hostName() + ":" + name + "获取了领导权限！");
            //应用全局变量设置为leader，供其他服务判断是否为领导者。
            ApplicationLeaderEnviornment.setLeader(true);
            try {
                //获取领导权限之后，此时不再释放，直到应用退出，大约40秒后剩余的应用实例将自动选出下一位领导者
                while (true) {
                    LogStashFormatUtil.logDebug(logger, "获取领导权限不再释放，进入sleep状态");
                    Thread.sleep(2 * 60 * 60 * 1000);
                }
            } catch (Exception e) {
                LogStashFormatUtil.logWarrning(logger, "主节点选举出现错误！", e);

            } finally {
                ApplicationLeaderEnviornment.setLeader(false);
                LogStashFormatUtil.logInfo(logger, LogStashFormatUtil.hostName() + ":" + name + "放弃领导地位！");
            }
        }
    }

}
