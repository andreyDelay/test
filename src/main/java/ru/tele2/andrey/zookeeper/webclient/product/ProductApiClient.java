package ru.tele2.andrey.zookeeper.webclient.product;

import ru.tele2.andrey.zookeeper.webclient.product.entity.ProductApiResponse;

public interface ProductApiClient {
    ProductApiResponse getProductFromExternalAPI(Long productId);
}
