package ru.tele2.andrey.zookeeper.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import ru.tele2.andrey.zookeeper.mapper.ProductResponseMapper;
import ru.tele2.andrey.zookeeper.model.ProductRequest;
import ru.tele2.andrey.zookeeper.model.uniform.Response;
import ru.tele2.andrey.zookeeper.service.ProductService;
import ru.tele2.andrey.zookeeper.webclient.product.ProductApiClient;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductResponseMapper mapper;
    private final ProductApiClient productApiClient;

    @NewSpan
    @Override
    public Response getProduct(@Valid ProductRequest productRequest) {
        return mapper.fromProduct(productApiClient.getProductFromExternalAPI(productRequest.getId()).getResult());
    }
}
