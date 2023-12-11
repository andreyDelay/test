package ru.tele2.andrey.zookeeper.service;

import ru.tele2.andrey.zookeeper.model.ProductRequest;
import ru.tele2.andrey.zookeeper.model.uniform.Response;

public interface ProductService {
    Response getProduct(ProductRequest productRequest);
}
