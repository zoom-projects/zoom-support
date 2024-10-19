package com.hb0730.zoom.sofa.rpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC包
 * ==========
 * 可以扫描指定的包下的所有的rpc服务
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)

public @interface RpcPkg {
    String value() default "";
}
