package ru.tele2.andrey.zookeeper.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.tele2.andrey.zookeeper.mapper.JsonToObjectMapper;
import ru.tele2.andrey.zookeeper.mapper.UniformResponseTransformer;
import ru.tele2.andrey.zookeeper.model.uniform.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestExecutor {

    private final JsonToObjectMapper jsonToObjectMapper;
    private final UniformResponseTransformer transformer;

    public ResponseEntity execute(MultiValueMap<String, String> params, HttpHeaders headers,
                                  HttpServletRequest request, ServiceMethod serviceMethod) {
        log.debug("REQUEST: Host: {}, URI: {}, Headers: {}",
                request.getRemoteAddr(), getUri(params, request), headers);
        return execute(serviceMethod);
    }

    public ResponseEntity execute(Object body, HttpHeaders headers,
                                  HttpServletRequest request, ServiceMethod serviceMethod) {
        log.debug("REQUEST: Host: {}, URI: {}, Body: {}, Headers: {}",
                request.getRemoteAddr(), request.getRequestURI(), jsonToObjectMapper.objectToJson(body), headers);
        return execute(serviceMethod);
    }

    private ResponseEntity execute(ServiceMethod serviceMethod) {
        ResponseEntity response = transformer.setResponse(serviceMethod.execute()).getResponseEntity();
        log.debug("RESPONSE: {}", response);
        return response;
    }

    private String getUri(MultiValueMap<String, String> params, HttpServletRequest request) {
        return UriComponentsBuilder.fromUriString(request.getRequestURI())
                .queryParams(params)
                .toUriString();
    }

    public <R> R queryParamsToDataType(MultiValueMap<String, String> params, Class<R> targetType) {
        Map map = params.toSingleValueMap();
        map.put("queryParams", UriComponentsBuilder.newInstance().queryParams(params).build().getQuery());
        return jsonToObjectMapper.jsonToObject(jsonToObjectMapper.objectToJson(map), targetType);
    }

    @FunctionalInterface
    public interface ServiceMethod {
        Response execute();
    }
}
