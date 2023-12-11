package ru.tele2.andrey.zookeeper.exception;

import ru.tele2.andrey.zookeeper.model.uniform.Response;
import ru.tele2.andrey.zookeeper.model.uniform.Status;

public abstract class UniformException extends RuntimeException implements Response {

    public UniformException(String message) {
        super(message);
    }

    public abstract Object getRequest();
    public abstract Status getStatus();
}
