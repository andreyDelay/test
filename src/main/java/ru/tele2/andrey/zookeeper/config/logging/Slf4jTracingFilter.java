package ru.tele2.andrey.zookeeper.config.logging;

import brave.Tracer;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(2)
@Component
public class Slf4jTracingFilter extends OncePerRequestFilter {
    public static final String TRACE_HEADER = "TraceParent";
    public static final String TOKEN_HEADER = "X-Header-Token";
    public static final String REQUEST_HEADER = "X-Header-RequestId";
    public static final String DEBUG_HEADER = "X-Header-Debug";
    public static final String MDC_UUID_KEY = "Slf4jMDCFilter.UUID";

    private final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    private final Tracer tracer;
    private final W3CPropagator propagator;
    private final String packageName = "ru.tele2.esb";

    private final ThreadLocal<Level> currentLogLevel = new ThreadLocal<>();

    public Slf4jTracingFilter(Tracer tracer, W3CPropagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    public static void initTraceContext(String contextId, String contextName) {
        Thread.currentThread().setName(contextName);
        MDC.put(Slf4jTracingFilter.MDC_UUID_KEY, contextId);
    }

    public static String getContextId() {
        return MDC.get(MDC_UUID_KEY);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
        boolean debug = false;
        try {
            String requestId = propagator.getRequestId(tracer.currentSpan().context());
            MDC.put(MDC_UUID_KEY, requestId);
            if (!StringUtils.isEmpty(request.getHeader(DEBUG_HEADER)) && request.getHeader(DEBUG_HEADER).equals("true")) {
                currentLogLevel.set(loggerContext.getLogger(packageName).getLevel());
                loggerContext.getLogger(packageName).setLevel(Level.DEBUG);
                log.debug("Enabled debug for request [" + requestId + "]");
                debug = true;
            }
            response.addHeader(REQUEST_HEADER, requestId);
            if (debug) {
                response.addHeader(DEBUG_HEADER, "true");
            }
            chain.doFilter(request, response);
        } finally {
            if (debug) {
                log.debug("Restored previous logging level");
                loggerContext.getLogger(packageName).setLevel(currentLogLevel.get());
                currentLogLevel.remove();
            }
            MDC.remove(MDC_UUID_KEY);
        }
    }
}
