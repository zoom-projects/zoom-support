package com.hb0730.zoom.base.meta;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;

/**
 * 日志链路追踪工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
public class TraceHolder {

    /**
     * 获取OpenTelemetry链路追踪id
     *
     * @return 链路追踪id
     */
    public static String getOtelTraceId() {
        // 获取OpenTelemetry链路追踪id
        SpanContext spanContext = Span.current().getSpanContext();
        return spanContext.getTraceId();
    }
}
