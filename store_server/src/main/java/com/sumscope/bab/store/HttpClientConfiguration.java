package com.sumscope.bab.store;

import com.sumscope.bab.quote.client.BabQuoteHttpClient;
import com.sumscope.cdhplus.httpclient.CdhPlusHttpClientWithCache;
import com.sumscope.iam.edmclient.EdmHttpClientWithCache;
import com.sumscope.iam.emclient.EmHttpClientWithCache;
import com.sumscope.iam.iamclient.IamHttpClientWithCache;
import com.sumscope.x315.client.X315HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fan.bai on 2016/12/28.
 * 应用程序配置- 相关HttpClient配置
 */
@Configuration
public class HttpClientConfiguration {
    @Value("${application.x315client.token}")
    private String x315Token;

    @Value("${application.iam_clients.edm_server.url}")
    private String edmClientUrl;

    @Value("${application.iam_clients.em_server.url}")
    private String emServerUrl;

    @Value("${application.iam_clients.iam_server.url}")
    private String iamServerUrl;

    @Value("${application.iam_clients.iam_server.clientID}")
    private String clientId;

    @Value("${application.iam_clients.iam_server.client_secret}")
    private String clientSecret;

    @Value("${application.cdh_plus_client.cdh_plus_server.url}")
    private String cdhPlusServerUrl;

    @Value("${application.bab.price.trends.url}")
    private String babPriceTrendsUrl;

    @Bean
    public EdmHttpClientWithCache edmHttpClientWithCache() {
        return new EdmHttpClientWithCache(edmClientUrl);
    }

    @Bean
    public IamHttpClientWithCache iamHttpClient() {
        return new IamHttpClientWithCache(iamServerUrl, clientId, clientSecret);
    }

    @Bean
    public EmHttpClientWithCache emHttpClient() {
        return new EmHttpClientWithCache(emServerUrl);
    }

    @Bean
    public X315HttpClient x315HttpClient() {
        return new X315HttpClient(x315Token);
    }



    @Bean
    public CdhPlusHttpClientWithCache cdhPlusHttpClientWithCache() {
        return new CdhPlusHttpClientWithCache(cdhPlusServerUrl);
    }

    @Bean
    public BabQuoteHttpClient babQuoteHttpClient() {
        return new BabQuoteHttpClient(babPriceTrendsUrl);
    }
}
