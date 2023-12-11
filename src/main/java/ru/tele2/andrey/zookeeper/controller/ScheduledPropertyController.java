package ru.tele2.andrey.zookeeper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tele2.andrey.zookeeper.service.ZookeeperPropertyHandler;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledPropertyController {

    private final ZookeeperPropertyHandler zookeeperPropertyHandler;

    @GetMapping("/property")
    public ResponseEntity<String> getPropertyValue() {
        log.info("Accepted GET request for path /property.");
        return ResponseEntity
                .ok()
                .body(zookeeperPropertyHandler.getPropertyValue());
    }
}
