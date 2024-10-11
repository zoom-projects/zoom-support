package com.hb0730.zoom.base.meta;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.hb0730.zoom.base.utils.IdUtil;
import org.slf4j.MDC;

/**
 * 日志链路追踪工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
public class TraceHolder {
    /**
     * 追踪id的名称
     */
    public static final String KEY_TRACE_ID = "traceId";
    /**
     * 追踪id的header
     */
    public static final String TRACE_ID_HEADER = "trace-id";

    private static final ThreadLocal<String> HOLDER = new TransmittableThreadLocal<>();

    private TraceHolder() {
    }

    /**
     * 获取 traceId
     *
     * @return traceId
     */
    public static String get() {
        return HOLDER.get();
    }

    /**
     * 设置 traceId
     */
    public static void set() {
        set(createTraceId());
    }

    /**
     * 设置 traceId
     *
     * @param traceId traceId
     */
    public static void set(String traceId) {
        // 设置应用上下文
        HOLDER.set(traceId);
        // 设置日志上下文
        setMdc(traceId);
    }

    /**
     * 删除 traceId
     */
    public static void remove() {
        // 移除应用上下文
        HOLDER.remove();
        // 移除日志上下文
        removeMDC();
    }

    /**
     * 从应用上下文 设置到日志上下文
     */
    public static void setMdc() {
        setMdc(HOLDER.get());
    }

    /**
     * 设置到日志上下文
     *
     * @param traceId traceId
     */
    public static void setMdc(String traceId) {
        MDC.put(KEY_TRACE_ID, traceId);
    }

    /**
     * 移除日志上下文
     */
    public static void removeMDC() {
        MDC.remove(KEY_TRACE_ID);
    }

    /**
     * 获取traceId
     *
     * @return traceId
     */
    public static String createTraceId() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
