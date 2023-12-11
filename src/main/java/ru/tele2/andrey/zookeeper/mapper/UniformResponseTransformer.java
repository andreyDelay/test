package ru.tele2.andrey.zookeeper.mapper;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.tele2.andrey.zookeeper.config.ConfigurationService;
import ru.tele2.andrey.zookeeper.config.logging.W3CPropagator;
import ru.tele2.andrey.zookeeper.exception.UniformException;
import ru.tele2.andrey.zookeeper.model.uniform.Response;
import ru.tele2.andrey.zookeeper.model.uniform.Trace;
import ru.tele2.andrey.zookeeper.model.uniform.UniformResponse;
import ru.tele2.andrey.zookeeper.model.uniform.VoidResponse;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UniformResponseTransformer {
    private final ConfigurationService config;
    private final W3CPropagator propagator;
    private final Tracer tracer;

    public Transformer setResponse(Response response) {
        Trace trace = new Trace();
        trace.setGlobalCode(config.getServiceInfo());
        trace.setMessage(response.getStatus().getMessage());
        trace.setTimestamp(new Date());
        trace.setReqId(propagator.getRequestId(tracer.currentSpan().context()));
        trace.setLocalCode(response.getStatus().getCode());
        UniformResponse uniformResponse = new UniformResponse();
        uniformResponse.setTrace(trace);
        uniformResponse.setResult(response);
        return new Transformer(uniformResponse);
    }

    public Transformer setException(UniformException exception) {
        Trace trace = new Trace();
        trace.setGlobalCode(config.getServiceInfo());
        trace.setMessage(exception.getMessage());
        trace.setTimestamp(new Date());
        trace.setReqId(propagator.getRequestId(tracer.currentSpan().context()));
        trace.setLocalCode(exception.getStatus().getCode());
        UniformResponse uniformResponse = new UniformResponse();
        uniformResponse.setTrace(trace);
        uniformResponse.setResult(new VoidResponse(((Response) exception).getStatus()));
        return new Transformer(uniformResponse);
    }

    public class Transformer {
        private final UniformResponse response;

        public Transformer(UniformResponse response) {
            this.response = response;
        }

        public ResponseEntity getResponseEntity() {
            return new ResponseEntity(response, ((Response) response.getResult()).getStatus().getHttpStatus());
        }
    }
}
