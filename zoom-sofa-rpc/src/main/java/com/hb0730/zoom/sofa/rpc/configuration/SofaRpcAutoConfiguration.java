package com.hb0730.zoom.sofa.rpc.configuration;

import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcBeanRegistryPostProcessor;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcServiceInitializingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@AutoConfiguration
@Slf4j
public class SofaRpcAutoConfiguration {

    @Bean
    public SofaRpcServiceInitializingBean sofaRpcServiceInitializingBean() {
        return new SofaRpcServiceInitializingBean();
    }

    @Bean
    public SofaRpcBeanRegistryPostProcessor sofaRpcBeanRegister() {
        return new SofaRpcBeanRegistryPostProcessor();
    }

}
