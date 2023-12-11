package ru.tele2.andrey.zookeeper.config.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationSchedulerConfig implements SchedulingConfigurer {

    private final ApplicationSchedulerProperties applicationProperties;

    @Bean(destroyMethod = "shutdown")
    public Executor executor() {
        return Executors.newScheduledThreadPool(applicationProperties.getScheduler().getThreadPoolSize());
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executor());
    }

}
