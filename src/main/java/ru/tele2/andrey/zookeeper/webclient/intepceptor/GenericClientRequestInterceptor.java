package ru.tele2.andrey.zookeeper.webclient.intepceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.tele2.andrey.zookeeper.model.uniform.Trace;
import ru.tele2.andrey.zookeeper.model.uniform.UniformResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Any class that will keep implementation of this class should be registered as @Bean in configuration
 * and should be passed as a parameter to method restTemplate.setInterceptors()
 * @param <T> parameter that expected to be received as a response type from external API
 */
public abstract class GenericClientRequestInterceptor<T extends UniformResponse> implements ClientHttpRequestInterceptor {

    private final ObjectMapper mapper;

    protected GenericClientRequestInterceptor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Method returns generic class type
     * for example if we have generic type Foo the return value will be Foo.class
     * @return Class.class value of parametrized generic type
     */
    @SuppressWarnings("unchecked")
    public Class<T> thisClassGenericType() {
        return  (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Method implementation should return a list of valid response codes that expected from API
     * @return List<String>
     */
    public abstract List<String> validLocalCodes();

    /**
     * Method implementation should return a new business exception that will be thrown
     * in case external's API response code (local_code) will be not valid according validLocalCodes() list values
     * @return inheritor of RuntimeException
     */
    public abstract RuntimeException customException(HttpRequest request, String message);

    protected <T> T jsonToObject(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        String responseString = getResponseString(response);

        if (response.getStatusCode().is4xxClientError()) {
            throw new HttpClientErrorException(response.getStatusCode(), responseString);
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new HttpServerErrorException(response.getStatusCode(), responseString);
        }

        Trace trace = jsonToObject(responseString, thisClassGenericType()).getTrace();
        if (isTraceLocalCodeValid(trace)) {
            return response;
        }
        MDC.put("External service local code response", trace.getLocalCode());
        throw customException(request, trace.getMessage());
    }

    private String getResponseString(ClientHttpResponse response) throws IOException {
        return new BufferedReader(new InputStreamReader(response.getBody()))
                .lines()
                .collect(Collectors.joining());
    }

    private boolean isTraceLocalCodeValid(Trace trace) {
        return validLocalCodes().stream()
                .anyMatch(validCode -> validCode.equalsIgnoreCase(trace.getLocalCode()));
    }
}
