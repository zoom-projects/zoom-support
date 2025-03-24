package com.hb0730.zoom.sofa.rpc.core.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC接口所属应用标识,用于动态注册到Spring容器,Provider服务
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/16
 * @see com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcServiceInitializingBean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface RemoteService {
}
