package com.hb0730.zoom.desensitize.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.hb0730.zoom.base.enums.MaskEnums;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jackson 脱敏配置元注解
 * <p>
 * Jackson 标注在字段上则标记该字段执行 http 序列化 (返回 VO)时脱敏 (http-message-converts 用的是 jackson)
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
public @interface FieldMask {

    /**
     * 脱敏类型
     *
     * @return {@link MaskEnums}
     */
    MaskEnums value() default MaskEnums.CHINESE;

    /**
     * 是否脱敏
     *
     * @return 是否脱敏
     */
    boolean isDesensitization() default true;
}
