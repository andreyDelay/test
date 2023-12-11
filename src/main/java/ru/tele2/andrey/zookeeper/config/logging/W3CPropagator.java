package ru.tele2.andrey.zookeeper.config.logging;

import brave.internal.codec.HexCodec;
import brave.internal.propagation.StringPropagationAdapter;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import brave.sampler.Sampler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class W3CPropagator extends Propagation.Factory implements Propagation<String> {
    private static final String UUID_PATTERN = "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})";
    private static final String TRACE_PATTERN = "^([a-f|A-F|0-9]{2})-([a-f|A-F|0-9]{32})-([a-f|A-F|0-9]{16})-(0[0|1])$";
    private static final String TOKEN_PATTERN = "^([a-f|A-F|0-9]{8})-([a-f|A-F|0-9]{4})-([a-f|A-F|0-9]{4})-([a-f|A-F|0-9]{4})-([a-f|A-F|0-9]{12})$";

    private final Sampler sampler;

    public W3CPropagator(Sampler sampler) {
        this.sampler = sampler;
    }

    @Override
    public List<String> keys() {
        return Arrays.asList(Slf4jTracingFilter.TRACE_HEADER, Slf4jTracingFilter.TOKEN_HEADER);
    }

    @Override
    public <R> TraceContext.Injector<R> injector(Setter<R, String> setter) {
        return (traceContext, request) -> {
            setter.put(request, Slf4jTracingFilter.TRACE_HEADER, generateTraceString(traceContext));
            setter.put(request, Slf4jTracingFilter.TOKEN_HEADER, getRequestId(traceContext));
        };
    }

    @Override
    public <R> TraceContext.Extractor<R> extractor(Getter<R, String> getter) {
        return request -> {
            String traceString = getter.get(request, Slf4jTracingFilter.TRACE_HEADER);
            String tokenString = getter.get(request, Slf4jTracingFilter.TOKEN_HEADER);
            if (!hasTraceInfo(traceString)) {
                if (!hasTraceInfo(tokenString)) {
                    traceString = generateTraceString();
                } else {
                    traceString = generateTraceStringFromTokenString(tokenString);
                }
            }
            return createTraceContext(traceString);
        };
    }

    @Override
    public boolean requires128BitTraceId() {
        return true;
    }

    @Override
    public <K> Propagation<K> create(KeyFactory<K> keyFactory) {
        return StringPropagationAdapter.create(this, keyFactory);
    }

    private boolean hasTraceInfo(String info) {
        return info != null && !info.isEmpty() && (info.matches(TRACE_PATTERN) || info.matches(TOKEN_PATTERN));
    }

    public String generateTraceString() {
        UUID uuid = UUID.randomUUID();
        long traceId = uuid.getLeastSignificantBits();
        String uuidString = uuid.toString().replace("-", "");
        return "00-" + uuidString + "-" + uuidString.substring(16) + "-" + (sampler.isSampled(traceId) ? "01" : "00");
    }

    private String generateTraceStringFromTokenString(String tokenString) {
        UUID uuid = UUID.fromString(tokenString);
        long traceId = uuid.getLeastSignificantBits();
        tokenString = tokenString.replace("-", "");
        return "00-" + tokenString + "-" + tokenString.substring(16) + "-" + (sampler.isSampled(traceId) ? "01" : "00");
    }

    public String generateTraceString(TraceContext traceContext) {
        String version = traceContext.extra().isEmpty() ? "00" : traceContext.extra().get(0).toString();
        return version + "-" + traceContext.traceIdString() + "-" + traceContext.spanIdString() + "-" + (traceContext.sampled() ? "01" : "00");
    }

    public String getRequestId(TraceContext traceContext) {
        return traceContext.traceIdString().replaceAll(UUID_PATTERN, "$1-$2-$3-$4-$5");
    }

    public TraceContextOrSamplingFlags createTraceContext(String traceString) {
        String[] traceParts = traceString.split("-");
        String version = traceParts[0];
        long traceIdHigh = HexCodec.lowerHexToUnsignedLong(traceParts[1].substring(0, 16));
        long traceIdLow = HexCodec.lowerHexToUnsignedLong(traceParts[1].substring(16));
        long spanId = HexCodec.lowerHexToUnsignedLong(traceParts[2]);
        boolean sampled = Byte.parseByte(traceParts[3]) == 1;
        return TraceContextOrSamplingFlags.create(TraceContext.newBuilder()
                .traceIdHigh(traceIdHigh)
                .traceId(traceIdLow)
                .spanId(spanId)
                .sampled(sampled)
                .addExtra(version)
                .build());
    }
}
