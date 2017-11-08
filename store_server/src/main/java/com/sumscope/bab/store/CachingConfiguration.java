package com.sumscope.bab.store;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumscope.bab.store.commons.BabStoreConstant;
import com.sumscope.iam.edmclient.EdmClientConstant;
import com.sumscope.iam.emclient.EmClientConstant;
import com.sumscope.iam.iamclient.IamClientConstant;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.management.ManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.Set;


/**
 * 缓存配置
 */
@Configuration
@EnableCaching
public class CachingConfiguration extends CachingConfigurerSupport {

    /**
     * 从配置文件读取的Redis服务地址，应该基于域名加端口号模式，例如： redis-cdh.dev.sumscope.com:6379
     */
    @Value("${application.redis.cluster.nodes}")
    private String redisNodes;


    /**
     * @return 使用的KeyGenerator，如果不设置，Spring使用的SimpleKeyGenerator生成的Key限制不足，很容易造成Key值冲突
     */
    @Bean(name = "DefaultKeyGenerator")
    public KeyGenerator keyGenerator(){
        return new CacheKeyGenerator();
    }

    /**
     * RedisTemplate为用于Redis操作的工具类，该类不仅可用于CacheManager，也可在应用中通过@Autowired方式引入并手动操作
     * Redis缓存库。本实例使用了定义的Serializer。
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    /**
     * 创建Redis连接工厂，配置必要参数。参数可从配置文件读取也可以如下直接写入常数。
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        //配置连接域名与端口号
        Set<String> strings = StringUtils.commaDelimitedListToSet(redisNodes);
        //使用Cluster方式提供服务的Redis集群需要实现以下类实例，Sentinel方式则使用对应类。
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(strings);
        //连接池的配置，可从配置文件读取，也可写入常量
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxWaitMillis(0);
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(clusterConfiguration,poolConfig);
        redisConnectionFactory.setUsePool(true); //使用连接池！
        return redisConnectionFactory;
    }



    @Override
    @Bean(name = BabStoreConstant.DEFAULT_CACHING_NAME)
    public CacheManager cacheManager() {
        net.sf.ehcache.CacheManager ehCacheManager = appEhCacheManager();
        EhCacheCacheManager cacheManager =  new EhCacheCacheManager(ehCacheManager);
        //配置MBean用于JConsole监控
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ManagementService.registerMBeans(ehCacheManager, mBeanServer, true, true, true, true);
        return cacheManager;
    }
    private CacheConfiguration getDefaultSingleCacheConfiguration(String cachingName, int maxLocalHeap) {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(cachingName);
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(maxLocalHeap);
        cacheConfiguration.setTimeToLiveSeconds(30*60);
        return cacheConfiguration;
    }
    private net.sf.ehcache.CacheManager appEhCacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        CacheConfiguration edmCacheConfig = getDefaultSingleCacheConfiguration(EdmClientConstant.EDM_CACHING_NAME, 5000);
        config.addCache(edmCacheConfig);
        CacheConfiguration emCacheConfig = getDefaultSingleCacheConfiguration(EmClientConstant.EM_CACHING_NAME, 5000);
        config.addCache(emCacheConfig);
        CacheConfiguration iamCacheConfig = getDefaultSingleCacheConfiguration(IamClientConstant.IAM_CACHING_NAME, 5000);
        config.addCache(iamCacheConfig);
        CacheConfiguration appDefaultCache = getDefaultSingleCacheConfiguration(BabStoreConstant.DEFAULT_CACHING_NAME, 1000);
        config.addCache(appDefaultCache);
        CacheConfiguration quoteCacheConfig = getDefaultSingleCacheConfiguration(BabStoreConstant.QUOTE_CACHING_NAME, 1000);
        quoteCacheConfig.setTimeToIdleSeconds(2*60);
        config.addCache(quoteCacheConfig);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }
}

