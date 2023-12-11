package ru.tele2.andrey.zookeeper.config.http;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("httpclientconfiguration")
public class HttpClientConfig {
    private int connectTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
    private int maxRetries;
    private int maxTotal;
    private int maxPerRoute;
}
