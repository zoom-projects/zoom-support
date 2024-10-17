package com.hb0730.zoom.sofa.rpc.core.service;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
public interface IRpcService {
    /**
     * 获取rpc服务
     *
     * @param clazz rpc服务
     * @param <V>   rpc服务
     * @return rpc服务
     */
    <V> V getRpcService(Class<V> clazz);
}
