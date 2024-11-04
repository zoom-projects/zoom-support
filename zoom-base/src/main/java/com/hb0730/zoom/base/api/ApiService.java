package com.hb0730.zoom.base.api;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * 服务接口
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/24
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@java.lang.annotation.Inherited
@Component
public @interface ApiService {
    /**
     * 服务名称
     *
     * @return 服务名称
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 服务名称
     *
     * @return 服务名称
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 服务分组
     *
     * @return 服务分组
     */
    String group() default "";

    /**
     * 服务渠道
     *
     * @return 服务渠道
     */
    String channel() default "";

    /**
     * 描述
     *
     * @return 描述
     */
    String desc() default "";
}
