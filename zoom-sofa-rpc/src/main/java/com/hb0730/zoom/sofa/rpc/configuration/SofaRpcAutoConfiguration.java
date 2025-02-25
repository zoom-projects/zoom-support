package com.hb0730.zoom.sofa.rpc.configuration;

import com.hb0730.zoom.sofa.rpc.configuration.config.SofaRpcConfig;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcBeanRegistryPostProcessor;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcServiceInitializingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@AutoConfiguration
@Slf4j
@EnableConfigurationProperties(SofaRpcConfig.class)
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
     * @param sofaRpcConfig 配置
     * @return {@link SofaRpcBeanRegistryPostProcessor}
     */
    @Bean
    @ConditionalOnMissingBean
    public SofaRpcBeanRegistryPostProcessor sofaRpcBeanRegister(SofaRpcConfig
                                                                        sofaRpcConfig) {
        return new SofaRpcBeanRegistryPostProcessor(sofaRpcConfig.getScanApiPackage());
    }
}
