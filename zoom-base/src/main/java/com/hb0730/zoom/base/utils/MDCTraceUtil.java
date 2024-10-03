package com.hb0730.zoom.base.utils;

import org.slf4j.MDC;

/**
 * 日志链路追踪工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
public class MDCTraceUtil {
    /**
     * 追踪id的名称
     */
    public static final String KEY_TRACE_ID = "traceId";

    /**
     * 添加traceId,默认key为traceId{@link #KEY_TRACE_ID},默认值为{@link IdUtil#getSnowflake()}生成
     */
    public static void addTraceId() {
        addTraceId(createTraceId());
    }

    /**
     * 添加traceId,默认key为traceId{@link #KEY_TRACE_ID}
     *
     * @param traceId traceId
     */
    public static void addTraceId(String traceId) {
        addTraceId(KEY_TRACE_ID, traceId);
    }


    /**
     * 添加traceId
     *
     * @param key     key
     * @param traceId traceId
     */
    public static void addTraceId(String key, String traceId) {
        MDC.put(key, traceId);
    }

    /**
     * 获取traceId,默认key为traceId{@link #KEY_TRACE_ID}
     *
     * @return traceId
     */
    public static String getTraceId() {
        return MDC.get(KEY_TRACE_ID);
    }

    /**
     * 获取traceId
     *
     * @param key key
     * @return traceId
     */
    public static String getTraceId(String key) {
        return MDC.get(key);
    }

    /**
     * 移除traceId,默认key为traceId{@link #KEY_TRACE_ID}
     */
    public static void removeTraceId() {
        removeTraceId(KEY_TRACE_ID);
    }

    /**
     * 移除traceId
     *
     * @param key key
     */
    public static void removeTraceId(String key) {
        MDC.remove(key);
    }


    /**
     * 获取traceId
     *
     * @return traceId
     */
    private static String createTraceId() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
