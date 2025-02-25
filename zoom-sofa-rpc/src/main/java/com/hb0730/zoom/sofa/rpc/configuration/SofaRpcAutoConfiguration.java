package com.hb0730.zoom.sofa.rpc.configuration;

import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcBeanRegistryPostProcessor;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcServiceInitializingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Slf4j
@AutoConfiguration
public class SofaRpcAutoConfiguration {
    /**
     * 初始化bean
     *
     * @return {@link SofaRpcServiceInitializingBean}
     */
    @Bean
    @ConditionalOnMissingBean
    public SofaRpcServiceInitializingBean sofaRpcServiceInitializingBean() {
        return new SofaRpcServiceInitializingBean();
    }

    /**
     * 注册bean
     *
     * @return {@link SofaRpcBeanRegistryPostProcessor}
     */
    @Bean
    @ConditionalOnMissingBean
    public SofaRpcBeanRegistryPostProcessor sofaRpcBeanRegister(Environment environment) {
        String property = environment.getProperty("zoom.sofa.rpc.scanRpcService");
        if (StrUtil.isBlank(property)) {
            property = environment.getProperty("zoom.sofa.rpc.scan-rpc-service", "classpath*:com/hb0730/**/remote/**/*RpcService.class");
        }
        return new SofaRpcBeanRegistryPostProcessor(property);
    }
}
