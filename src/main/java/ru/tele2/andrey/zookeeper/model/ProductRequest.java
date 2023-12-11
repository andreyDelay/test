package ru.tele2.andrey.zookeeper.model;

import lombok.Data;
import ru.tele2.andrey.zookeeper.validator.ValidateRequest;

@Data
@ValidateRequest
public class ProductRequest {
    private Long id;
}
