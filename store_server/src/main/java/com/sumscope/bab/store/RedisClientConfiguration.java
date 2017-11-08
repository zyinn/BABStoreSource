package com.sumscope.bab.store;

import com.sumscope.optimus.commons.redis.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/3/6.
 * 操作redis 相关RedisClient配置
 */
@Configuration
public class RedisClientConfiguration {

    @Value("${application.redis.cluster.nodes}")
    private String redisNodes;

    @Bean
    public RedisClient createRedisClient() {
        String redis[] = redisNodes.split(":");
        String host = redis[0];
        int port = Integer.parseInt(redis[1]);
        RedisClient redisClient = new RedisClient(host, port, true);
        redisClient.open();
        return redisClient;
    }

}
