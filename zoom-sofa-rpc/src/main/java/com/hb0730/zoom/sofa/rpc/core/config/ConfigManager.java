package com.hb0730.zoom.sofa.rpc.core.config;

import java.util.Properties;

/**
 * 配置管理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
public class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();
    private static Properties properties;

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    /**
     * 加载配置
     *
     * @param path 路径
     */
    public void loadProperties(String path) {
        properties = new Properties();
        try {
            properties.load(ConfigManager.class.getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

    /**
     * 获取配置
     *
     * @param appName 应用名称
     * @return 配置
     */
    public static RpcConfigProperties getConfig(String appName) {
        // 获取配置
        RpcConfigProperties rpcConfigProperties = new RpcConfigProperties();
        rpcConfigProperties.setName(getValue(appName, RpcConfigProperties.NAME, ""));
        rpcConfigProperties.setVersion(getValue(appName, RpcConfigProperties.VERSION, ""));
        rpcConfigProperties.setAddress(getValue(appName, RpcConfigProperties.ADDRESS, ""));
        return rpcConfigProperties;
    }

    /**
     * 获取配置
     *
     * @param prefix       前缀
     * @param key          key
     * @param defaultValue 默认值
     * @return 配置
     */
    private static String getValue(String prefix, String key, String defaultValue) {
        return properties.getProperty(append(prefix, key), defaultValue);
    }

    /**
     * 拼接key
     *
     * @param prefix 前缀
     * @param key    key
     * @return key
     */
    private static String append(String prefix, String key) {
        return prefix + "." + key;
    }
}
