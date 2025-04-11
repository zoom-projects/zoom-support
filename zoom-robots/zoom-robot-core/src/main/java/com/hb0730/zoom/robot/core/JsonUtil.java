package com.hb0730.zoom.robot.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public class JsonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 设置序列化时忽略null值
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
    }

    /**
     * 将对象转换为json字符串,忽略null值
     *
     * @param object 对象
     * @return json字符串
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json          json字符串
     * @param typeReference 对象类型
     * @param <T>           对象类型
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception ignore) {
            return null;
        }
    }

}
