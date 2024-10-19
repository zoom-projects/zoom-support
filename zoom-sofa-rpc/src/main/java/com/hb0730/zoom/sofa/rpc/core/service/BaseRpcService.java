package com.hb0730.zoom.sofa.rpc.core.service;

import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.hb0730.zoom.base.AppUtil;
import com.hb0730.zoom.sofa.rpc.core.config.ConfigManager;
import com.hb0730.zoom.sofa.rpc.core.config.RpcConfigProperties;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcClientFactoryBean;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <T> rpc服务
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
public abstract class BaseRpcService<T> implements IRpcService {
    private final Map<String, T> RPC_SERVER_CACHE = new ConcurrentHashMap<>();
    @Getter
    protected Class<T> rpcInterfaceClazz;

    @SuppressWarnings("unchecked")
    public BaseRpcService() {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType
                && ((ParameterizedType) type).getActualTypeArguments().length > 0) {
            Type t = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (!"T".equals(t.getTypeName())) {
                this.rpcInterfaceClazz = (Class<T>) t;
            }
        }
    }

    /**
     * 应用名称
     *
     * @return 应用名称
     */
    protected String getAppName(String which) {
        return this.getAppName();
    }

    /**
     * 应用名称
     *
     * @return 应用名称
     */
    protected abstract String getAppName();

    /**
     * 服务地址
     *
     * @return 服务地址
     */
    protected String getRpcServer() {
        return getRpcServer(getAppName());
    }

    /**
     * 服务地址
     *
     * @param which 服务名称
     * @return 服务地址
     */
    protected String getRpcServer(String which) {
        RpcConfigProperties rpcConfig = getRpcConfig(which);
        return rpcConfig.getAddress();
    }

    /**
     * 获取rpc配置
     *
     * @param which 服务名称
     * @return rpc配置
     */
    private RpcConfigProperties getRpcConfig(String which) {
        return ConfigManager.getConfig(which);
    }

    /**
     * 获取rpc服务
     *
     * @param which 服务名称
     * @return rpc服务
     */
    public T getRpcService(String which) {
        String appName = getAppName(which);
        String server = getRpcServer(appName);
        return this.getRpcService(server, this.getRpcInterfaceClazz());
    }

    public T getRpcService() {
        return this.getRpcService(this.getRpcInterfaceClazz());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getRpcService(Class<V> clazz) {
        T RpcService = RPC_SERVER_CACHE.get(clazz.getName());
        if (null == RpcService) {
            RpcService = getRpcService(getRpcServer(), (Class<T>) clazz);
            RPC_SERVER_CACHE.put(clazz.getName(), RpcService);
        }
        return (V) RpcService;
    }

    /**
     * 根据接口类型取得RPC接口
     *
     * @param server 服务地址
     * @param clazz  接口类型
     * @return RPC接口
     */
    protected T getRpcService(String server, Class<T> clazz) {
        SofaRpcClientFactoryBean clientFactoryBean = AppUtil.getBean(SofaRpcClientFactoryBean.class);
        ReferenceClient referenceClient = clientFactoryBean.getClientFactory().getClient(ReferenceClient.class);
        ReferenceParam<T> referenceParam = new ReferenceParam<>();
        referenceParam.setInterfaceType(clazz);

        //Bolt协议
        BoltBindingParam bindingParam = new BoltBindingParam();
        // 设置服务地址 直连模式
        if (null != server) {
            bindingParam.setTargetUrl(server);
        }
        referenceParam.setBindingParam(bindingParam);
        return referenceClient.reference(referenceParam);
    }
}
