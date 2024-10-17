package com.hb0730.zoom.sofa.rpc.core.factory;

import java.util.List;

/**
 * 自定义代理配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
public interface CustomRpcProxyConfig {

    /**
     * 获取忽略的类
     *
     * @return 忽略的类
     */
    List<String> getIgnoreItems();
}
