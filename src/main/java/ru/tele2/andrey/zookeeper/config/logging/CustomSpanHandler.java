package ru.tele2.andrey.zookeeper.config.logging;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tele2.andrey.zookeeper.util.ExceptionUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomSpanHandler extends SpanHandler {

    private final W3CPropagator propagator;

    @Value("${management.metrics.tags.env:local}")
    private String environment;

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
        if (span.tags().containsKey("http.path") && span.tag("http.path").contains("actuator")) {
            return false;
        }

        if (span.name().toLowerCase().startsWith("jdbc")) {
            if (span.tags().containsKey("sql")) {
                MDC.put("request", span.tag("sql"));
                MDC.put("requestUri", "dbConfig.getAlias()");
            }
            return false;
        }

        span.tag("env", environment);
        span.tag("requestId", propagator.getRequestId(context));
        Map contextMap = MDC.getCopyOfContextMap();

        if (contextMap.containsKey("requestUri")) {
            span.remoteServiceName((String) contextMap.get("requestUri"));
            if (span.tags().containsKey("http.method")) {
                span.name(span.tag("http.method") + " " + span.tag("http.path"));
            }
        }

        if (contextMap.containsKey("External service local code response")) {
            span.tag(
                    "External service local code response",
                    (String) contextMap.get("External service local code response")
            );
        }

        if (span.error() != null || span.tags().containsKey("error")) {
            if (contextMap.containsKey("request")) {
                span.tag("request", (String) contextMap.get("request"));
                span.tag("error", span.error().getMessage());
            }
            if (span.error() != null) {
                span.error(ExceptionUtils.getRootCause(span.error()));
            }
        }
        MDC.remove("request");
        MDC.remove("requestUri");
        return true;
    }
}
