package com.hb0730.zoom.sofa.rpc.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/2/15
 */
@Data
@ConfigurationProperties(prefix = "zoom.sofa.rpc")
public class SofaRpcConfig {

    /**
     * 加载类型,默认本地，文件名称为`zoom-app-${profile}.properties` 或者 `zoom-app.properties` 优先级高
     */
    private LoadType loadType = LoadType.LOCAL;

    /**
     * Nacos 配置
     */
    private NacosConfig nacos = new NacosConfig();

    /**
     * 扫描包路径
     */
    private String scanApiPackage = "classpath*:com/hb0730/**/remote/**/*RpcService.class";

    public enum LoadType {
        /**
         * 本地
         */
        LOCAL,
        /**
         * Nacos
         */
        NACOS,
    }

    @Data
    public static class NacosConfig {
        /**
         * 服务地址
         */
        private String serverAddr;
        /**
         * 命名空间
         */
        private String namespace;
        /**
         * 配置 ID
         */
        private String dataId;
        /**
         * 配置分组
         */
        private String group;
    }
}
