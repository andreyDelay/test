package ru.tele2.andrey.zookeeper.model.uniform;

import org.springframework.http.HttpStatus;

public interface Status {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
