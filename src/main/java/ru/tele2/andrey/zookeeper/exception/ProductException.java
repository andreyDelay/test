package ru.tele2.andrey.zookeeper.exception;

import lombok.Getter;
import ru.tele2.andrey.zookeeper.model.uniform.Status;

@Getter
public class ProductException extends UniformException {
    private final Object request;
    private final Status status;

    public ProductException(Object request, Status status) {
        super(status.getMessage());
        this.request = request;
        this.status = status;
    }

    public ProductException(Object request, Status status, Throwable throwable) {
        super(throwable.getMessage());
        this.request = request;
        this.status = status;
    }

    public ProductException(Object request, Status status, String message) {
        super(message);
        this.request = request;
        this.status = status;
    }
}
