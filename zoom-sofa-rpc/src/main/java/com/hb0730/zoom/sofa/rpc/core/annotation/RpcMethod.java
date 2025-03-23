package com.hb0730.zoom.sofa.rpc.core.annotation;

import com.hb0730.zoom.base.Pair;
import com.hb0730.zoom.base.PairEnum;

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
     * </ul>
     *
     * @return .
     */
    CallType type() default CallType.SYNC;

    /**
     * 超时时间,默认3s,单位毫秒ms
     *
     * @return .
     */
    int timeout() default 3000;

    /**
     * 调用类型
     *
     * @see <a href="https://www.sofastack.tech/projects/sofa-rpc/invoke-type/">sofa-rpc</a>
     */
    enum CallType implements PairEnum<String, Pair<String, String>> {
        SYNC(new Pair<>("sync", "同步")),
        FUTURE(new Pair<>("future", "异步"));
        private final Pair<String, String> pair;

        CallType(Pair<String, String> pair) {
            this.pair = pair;
        }

        @Override
        public Pair<String, String> getValue() {
            return pair;
        }

        @Override
        public String getCode() {
            return pair.getCode();
        }

        @Override
        public String getMessage() {
            return pair.getMessage();
        }
    }
}
