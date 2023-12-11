package ru.tele2.andrey.zookeeper.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Data
@Component
public class ConfigurationService {

    private final ApplicationContext context;
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.cloud.zookeeper.connect-string}")
    private String zkConnectString;

    @Value("${management.metrics.tags.env:local}")
    private String environment;

    public ConfigurationService(ApplicationContext context) throws UnknownHostException {
        this.context = context;
        host = InetAddress.getLocalHost().getHostAddress();
    }

    public String getServiceInfo() {
        return serviceName + "_" + host + ":" + port;
    }

    public String getServiceVersion() {
        return context.getBeansWithAnnotation(SpringBootApplication.class)
                .entrySet().stream()
                .findFirst()
                .flatMap(es -> {
                    final String implementationVersion = es.getValue().getClass().getPackage().getImplementationVersion();
                    return Optional.ofNullable(implementationVersion);
                }).orElse("unknown");
    }
}
