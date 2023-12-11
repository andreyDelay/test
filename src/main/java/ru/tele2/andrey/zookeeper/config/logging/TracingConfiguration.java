package ru.tele2.andrey.zookeeper.config.logging;

import brave.Tracing;
import brave.messaging.MessagingTracing;
import brave.sampler.BoundarySampler;
import brave.sampler.Sampler;
import brave.spring.rabbit.SpringRabbitTracing;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.zipkin2.ZipkinAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.Sender;
import zipkin2.reporter.amqp.RabbitMQSender;

@Configuration
public class TracingConfiguration {

    @Bean
    public SpringRabbitTracing springRabbitTracing(MessagingTracing messagingTracing) {
        return SpringRabbitTracing.newBuilder(messagingTracing).build();
    }

    @Bean
    public MessagingTracing messagingTracing(Tracing tracing) {
        return MessagingTracing.create(tracing);
    }

    @Bean
    @RefreshScope
    public CachingConnectionFactory cachingConnectionFactory(ZipkinConfig config) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(config.getMqHost());
        connectionFactory.setUsername(config.getMqLogin());
        connectionFactory.setPassword(config.getMqPassword());
        connectionFactory.setVirtualHost(config.getMqVirtualHost());
        connectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
        return connectionFactory;
    }

    @Bean(ZipkinAutoConfiguration.SENDER_BEAN_NAME)
    @RefreshScope
    public Sender sender(CachingConnectionFactory cachingConnectionFactory, ZipkinConfig config) {
        return RabbitMQSender.newBuilder()
                .connectionFactory(cachingConnectionFactory.getRabbitConnectionFactory())
                .queue(config.getMqQueue())
                .addresses(cachingConnectionFactory.getHost())
                .build();
    }

    @Bean
    @RefreshScope
    public Sampler sampler(ZipkinConfig config) {
        return BoundarySampler.create(config.getSamplerRatio());
    }

}
