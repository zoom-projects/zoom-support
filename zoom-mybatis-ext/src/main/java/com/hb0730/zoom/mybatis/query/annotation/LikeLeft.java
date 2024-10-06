package com.hb0730.zoom.mybatis.query.annotation;

/**
 * 用于“左模糊匹配某个元素”({@code LIKE '%value'})场景的注解.
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/5
 */
public @interface LikeLeft {
    /**
     * 注解的实体字段属性名称/字段column名称，默认为空或空字符串时将使用属性名称.
     *
     * @return 值
     */
    String value() default "";

    /**
     * 是否使用驼峰命名，默认为 {@code true}.
     *
     * @return 值
     */
    boolean underCamel() default true;
}
