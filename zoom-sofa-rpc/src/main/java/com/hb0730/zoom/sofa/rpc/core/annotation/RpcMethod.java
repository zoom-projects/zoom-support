package com.hb0730.zoom.sofa.rpc.core.annotation;

/**
 * rpc方法
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/3/2
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface RpcMethod {

    /**
     * 调用类型
     * <ul>
     *     <li>sync 同步</li>
     *     <li>future 异步</li>
     *     <li>callback 回调</li>
     * </ul>
     *
     * @return .
     */
    String type() default "sync";

    /**
     * 超时时间
     *
     * @return .
     */
    int timeout() default 3000;
}
