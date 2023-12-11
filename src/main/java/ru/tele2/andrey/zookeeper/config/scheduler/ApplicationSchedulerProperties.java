package ru.tele2.andrey.zookeeper.config.scheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties
public class ApplicationSchedulerProperties {

    private Scheduler scheduler;

    @Data
    public static class Scheduler {

        /**
         * Время между запросами к целевому свойству берётся из
         * общего конфига /application/zk-app.scheduler.print-property-value-interval
         */
        private Duration printPropertyValueInterval;

        private Integer threadPoolSize;

    }

}
