package ru.tele2.andrey.zookeeper.config.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.tele2.andrey.zookeeper.webclient.intepceptor.GenericClientRequestInterceptor;
import ru.tele2.andrey.zookeeper.webclient.intepceptor.ProductRequestInterceptor;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @RefreshScope
    public RestTemplate restTemplate(HttpClientConfig config, ObjectMapper mapper) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(config.getMaxPerRoute());
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                .setSocketTimeout(config.getSocketTimeout())
                .setConnectTimeout(config.getConnectTimeout())
                .build();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler((error, count, httpContext) -> count <= config.getMaxRetries() && error instanceof IOException)
                .build();
        ClientHttpRequestFactory requestFactory =
                new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(Arrays.asList(productInterceptor(mapper)));
        return restTemplate;
    }

    @Bean
    public GenericClientRequestInterceptor<?> productInterceptor(ObjectMapper mapper) {
        return new ProductRequestInterceptor(mapper);
    }
}
