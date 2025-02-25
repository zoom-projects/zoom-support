package com.hb0730.zoom.sofa.rpc.configuration;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alipay.remoting.NamedThreadFactory;
import com.hb0730.zoom.base.AppUtil;
import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.sofa.rpc.configuration.config.SofaRpcConfig;
import com.hb0730.zoom.sofa.rpc.core.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/2/15
 */
@AutoConfiguration
@EnableConfigurationProperties(SofaRpcConfig.class)
@Slf4j
public class SofaRpcConfigAutoConfiguration {

    public SofaRpcConfigAutoConfiguration(SofaRpcConfig sofaRpcConfig) throws NacosException, IOException {
        log.info("加载RPC配置: {}", sofaRpcConfig);
        if (SofaRpcConfig.LoadType.LOCAL.equals(sofaRpcConfig.getLoadType())) {
            intRpcRouter();
        } else {
            nacosRpcRouter(sofaRpcConfig.getNacos());
        }
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


    /**
     * nacos配置
     */
    private void nacosRpcRouter(SofaRpcConfig.NacosConfig nacos) throws NacosException, IOException {
        if (null == nacos) {
            throw new IllegalArgumentException("加载RPC配置文件: Nacos配置不能为空");
        }
        log.info("加载RPC配置文件: Nacos配置: {}", nacos);
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacos.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacos.getNamespace());
        ConfigService configService = NacosFactory.createConfigService(properties);
        String value = configService.getConfigAndSignListener(nacos.getDataId(), nacos.getGroup(), 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                // 业务线程池
                ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());
                executor.setThreadFactory(new NamedThreadFactory("sofa-rpc-config-listener", true));
                return executor;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("RPC配置文件更新");
                try {
                    Properties properties = new Properties();
                    properties.load(new StringReader(configInfo));
                    ConfigManager.getInstance().loadProperties(properties);
                } catch (IOException e) {
                    log.error("RPC配置文件更新失败", e);
                    throw new RuntimeException("RPC配置文件更新失败", e);
                }
            }
        });
        Properties valueProperties = new Properties();
        valueProperties.load(new StringReader(value));
        ConfigManager.getInstance().loadProperties(valueProperties);
    }
}
