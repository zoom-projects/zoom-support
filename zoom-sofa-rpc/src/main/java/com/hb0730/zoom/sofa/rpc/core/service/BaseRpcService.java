package com.hb0730.zoom.sofa.rpc.core.service;

import com.alipay.sofa.rpc.boot.runtime.binding.RpcBindingMethodInfo;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import com.hb0730.zoom.base.AppUtil;
import com.hb0730.zoom.base.utils.ClassUtil;
import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.sofa.rpc.core.annotation.RpcMethod;
import com.hb0730.zoom.sofa.rpc.core.config.ConfigManager;
import com.hb0730.zoom.sofa.rpc.core.config.RpcConfigProperties;
import com.hb0730.zoom.sofa.rpc.core.factory.SofaRpcClientFactoryBean;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <T> rpc服务
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Slf4j
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
     * 获取服务配置
     *
     * @return .
     */
    protected RpcConfigProperties getRpcServerConfig() {
        return getRpcConfig(getAppName());
    }

    /**
     * 服务地址
     *
     * @param which 服务名称
     * @return 服务地址
     */
    protected RpcConfigProperties getRpcServerConfig(String which) {
        return getRpcConfig(which);
    }

    /**
     * 获取rpc配置
     *
     * @param which 服务名称
     * @return rpc配置
     */
    protected RpcConfigProperties getRpcConfig(String which) {
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
        RpcConfigProperties server = getRpcServerConfig(appName);
        String key = String.format("%s#%s", server.getName(), this.getRpcInterfaceClazz().getName());
        T RpcService = RPC_SERVER_CACHE.get(key);
        if (null == RpcService) {
            RpcService = this.getRpcService(server, this.getRpcInterfaceClazz());
            RPC_SERVER_CACHE.put(key, RpcService);
        }
        return RpcService;
    }

    public T getRpcService() {
        return this.getRpcService(this.getRpcInterfaceClazz());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getRpcService(Class<V> clazz) {
        RpcConfigProperties config = getRpcServerConfig();
        String key = String.format("%s#%s", config.getName(), clazz.getName());
        T RpcService = RPC_SERVER_CACHE.get(key);
        if (null == RpcService) {
            RpcService = getRpcService(config, (Class<T>) clazz);
            RPC_SERVER_CACHE.put(key, RpcService);
        }
        return (V) RpcService;
    }

    /**
     * 根据接口类型取得RPC接口
     *
     * @param serverConfig 服务配置
     * @param clazz        接口类型
     * @return RPC接口
     */
    protected T getRpcService(RpcConfigProperties serverConfig, Class<T> clazz) {
        log.info("注册RPC CLIENT 服务: {}#{}", serverConfig.getName(), clazz.getName());
        SofaRpcClientFactoryBean clientFactoryBean = AppUtil.getBean(SofaRpcClientFactoryBean.class);
        ReferenceClient referenceClient = clientFactoryBean.getClientFactory().getClient(ReferenceClient.class);
        ReferenceParam<T> referenceParam = new ReferenceParam<>();
        referenceParam.setInterfaceType(clazz);

        //Bolt协议
        BoltBindingParam bindingParam = new BoltBindingParam();
        // 获取类的方法存在@RpcMethod注解
        Method[] methods = ClassUtil.getPublicMethods(clazz);
        List<RpcBindingMethodInfo> methodInfos = getRpcBindingMethodInfos(methods);
        bindingParam.setMethodInfos(methodInfos);
        // 设置服务地址 直连模式
        String address = serverConfig.getAddress();
        if (StrUtil.isNotBlank(address)) {
            bindingParam.setTargetUrl(address);
        }
        //TODO:注册中心 采用Spring方式即可

        referenceParam.setBindingParam(bindingParam);
        return referenceClient.reference(referenceParam);
    }

    private static List<RpcBindingMethodInfo> getRpcBindingMethodInfos(Method[] methods) {
        List<RpcBindingMethodInfo> methodInfos = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RpcMethod.class)) {
                RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
                RpcBindingMethodInfo methodInfo = new RpcBindingMethodInfo();
                methodInfo.setName(method.getName());
                methodInfo.setTimeout(rpcMethod.timeout());
                methodInfo.setType(rpcMethod.type().getCode());
                methodInfos.add(methodInfo);
            }
        }
        return methodInfos;
    }
}
