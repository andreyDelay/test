package ru.tele2.andrey.zookeeper.webclient.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;
import ru.tele2.andrey.zookeeper.component.HttpClient;
import ru.tele2.andrey.zookeeper.config.ProductApiProperties;
import ru.tele2.andrey.zookeeper.enumeration.ProductResponseStatus;
import ru.tele2.andrey.zookeeper.exception.ProductException;
import ru.tele2.andrey.zookeeper.webclient.product.entity.ProductApiResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductApiClientImpl implements ProductApiClient {

    private final HttpClient httpClient;
    private final ProductApiProperties productApiProperties;

    @NewSpan
    @Override
    public ProductApiResponse getProductFromExternalAPI(Long productId) {
        ResponseEntity<String> responseEntity;
        String url = UriComponentsBuilder
                .fromHttpUrl(productApiProperties.getUrl())
                .queryParam("id", productId)
                .build().toUri().toString();
        try {
            return httpClient.executeGet(
                    url,
                    new HttpHeaders(),
                    productApiProperties.getRemoteSystem(),
                    ProductApiResponse.class
            ).getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Couldn't get a 2xx response from external API. Message: {}", e.getMessage());
            responseEntity = new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }

        throw new ProductException(url, ProductResponseStatus.BP_BAD_API_RESPONSE, responseEntity.getBody());
    }
}
