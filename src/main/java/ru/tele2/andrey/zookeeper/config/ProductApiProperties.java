package ru.tele2.andrey.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "external-api.product")
public class ProductApiProperties {
    private String url;
    private String remoteSystem;
}
