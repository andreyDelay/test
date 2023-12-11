package ru.tele2.andrey.zookeeper.exception.handler;


import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tele2.andrey.zookeeper.exception.UniformException;
import ru.tele2.andrey.zookeeper.mapper.JsonToObjectMapper;
import ru.tele2.andrey.zookeeper.mapper.UniformResponseTransformer;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApplicationControllerAdvice extends ResponseEntityExceptionHandler {

    private final Tracer tracer;
    private final JsonToObjectMapper jsonToObjectMapper;
    private final UniformResponseTransformer transformer;

    @ExceptionHandler({UniformException.class})
    public ResponseEntity<?> onServiceException(UniformException exception) {
        ResponseEntity<?> response = transformer.setException(exception).getResponseEntity();
        log.error("RESPONSE: {}", response);
        String request = exception.getRequest() instanceof String
                ? (String) exception.getRequest()
                : jsonToObjectMapper.objectToJson(exception.getRequest());
        MDC.put("request", request);
        tracer.currentSpan().error(new Exception(jsonToObjectMapper.objectToJson(response.getBody())));
        return response;
    }
}
