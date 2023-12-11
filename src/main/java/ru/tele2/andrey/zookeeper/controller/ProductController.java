package ru.tele2.andrey.zookeeper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tele2.andrey.zookeeper.component.RequestExecutor;
import ru.tele2.andrey.zookeeper.model.ProductRequest;
import ru.tele2.andrey.zookeeper.model.uniform.UniformResponse;
import ru.tele2.andrey.zookeeper.service.ProductService;
import ru.tele2.andrey.zookeeper.webclient.product.entity.ProductApiResponse;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final RequestExecutor requestExecutor;

    @GetMapping("/product")
    public ResponseEntity<UniformResponse<ProductApiResponse>> getProduct(@RequestParam MultiValueMap<String, String> params,
                                                                          @RequestHeader HttpHeaders headers,
                                                                          HttpServletRequest request) {
        return requestExecutor.execute(params, headers, request,
                () -> productService.getProduct(
                        requestExecutor.queryParamsToDataType(params, ProductRequest.class))
        );
    }
}
