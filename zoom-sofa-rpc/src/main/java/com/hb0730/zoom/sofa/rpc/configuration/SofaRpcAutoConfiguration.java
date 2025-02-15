package com.hb0730.zoom.sofa.rpc.configuration;

import com.hb0730.zoom.base.AppUtil;
import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.sofa.rpc.core.config.ConfigManager;
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


    private void intRpcRouter() {
        String env = AppUtil.getActiveProfile();

        String filename = String.format("zoom-app-%s.properties", StrUtil.isBlank(env) ? "default" : env);
        try {
            try {

                log.info("加载RPC配置文件: {}", filename);
                ConfigManager.getInstance().loadProperties(filename);
            } catch (Exception e) {
                log.warn("加载RPC配置文件[{}]失败, 加载默认配置: zoom-app.properties", filename);
                ConfigManager.getInstance().loadProperties("zoom-app.properties");
            }

        } catch (Throwable e) {
            log.error("!!!加载RPC配置文件失败!!!", e);
        }
    }
}
