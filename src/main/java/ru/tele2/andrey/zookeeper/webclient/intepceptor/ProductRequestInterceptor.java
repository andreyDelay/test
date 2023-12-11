package ru.tele2.andrey.zookeeper.webclient.intepceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpRequest;
import ru.tele2.andrey.zookeeper.enumeration.CommonResponseStatus;
import ru.tele2.andrey.zookeeper.exception.ProductException;
import ru.tele2.andrey.zookeeper.webclient.product.entity.ProductApiResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductRequestInterceptor extends GenericClientRequestInterceptor<ProductApiResponse> {

    public ProductRequestInterceptor(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public List<String> validLocalCodes() {
        return Arrays.stream(
                ProductApiResponse.ValidLocalCodes.values())
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @Override
    public RuntimeException customException(HttpRequest request, String message) {
        return new ProductException(request.getURI(), CommonResponseStatus.BP_EXTERNAL_SERVICE_LOCAL_CODE_ERROR, message);
    }
}
