package ru.tele2.andrey.zookeeper.config.logging;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("zipkin")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZipkinConfig {
    String mqHost;
    String mqLogin;
    String mqPassword;
    String mqVirtualHost;
    String mqQueue;
    Float samplerRatio;
}
