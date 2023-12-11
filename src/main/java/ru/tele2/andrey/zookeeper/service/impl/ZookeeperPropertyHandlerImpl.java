package ru.tele2.andrey.zookeeper.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tele2.andrey.zookeeper.config.ApplicationProperties;
import ru.tele2.andrey.zookeeper.service.ZookeeperPropertyHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZookeeperPropertyHandlerImpl implements ZookeeperPropertyHandler {

    private final ApplicationProperties applicationProperties;

    @Override
    public String getPropertyValue() {
        return applicationProperties.getTargetProperty();
    }

    @Scheduled(fixedDelayString = "#{@applicationSchedulerProperties.scheduler.printPropertyValueInterval.toMillis()}",
                initialDelay = 1000)
    protected void showPropertyValue() {
        log.info("Current property value is: " + applicationProperties.getTargetProperty());
    }
}
