package ru.tele2.andrey.zookeeper.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClient {

    private final RestTemplate restTemplate;

    public <R> ResponseEntity<R> executeGet(String url, HttpHeaders headers, String remoteSystem, Class<R> responseType) {
        String query = UriComponentsBuilder.fromHttpUrl(url).build().getQuery();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return execute(url, query, remoteSystem, HttpMethod.GET, new HttpEntity<>(headers), responseType);
    }

    @NewSpan
    private <R> ResponseEntity<R> execute(String url, String query, String remoteSystem, HttpMethod method,
                                          HttpEntity<R> requestEntity, Class<R> responseType) {
        MDC.put("requestUri", remoteSystem);
        MDC.put("request", query);
        try {
            log.debug("Http request: URL: {}, Data: {}", url, requestEntity);
            ResponseEntity<R> response = restTemplate.exchange(url, method, requestEntity, responseType);
            log.debug("Http response: " + response);
            return response;
        } catch (Exception exception) {
            log.error("Unexpected internal error. Message: {}", exception.getMessage());
            throw exception;
        }
    }
}
