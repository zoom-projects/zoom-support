package com.hb0730.zoom.opentelemetry.configuration.config;

import com.hb0730.zoom.base.meta.TraceHolder;
import io.opentelemetry.sdk.trace.IdGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/5/22
 */
@Slf4j
public class TraceIdGenerator implements IdGenerator {
    public static final TraceIdGenerator INSTANCE = new TraceIdGenerator();

    @Override
    public String generateSpanId() {
//        log.info("[otel] generate spanId");
        return IdGenerator.random().generateSpanId();
    }

    @Override
    public String generateTraceId() {
//        log.info("[otel] generate traceId");
        String traceId = IdGenerator.random().generateTraceId();
        TraceHolder.setTraceId(traceId);
        return traceId;
    }
}
