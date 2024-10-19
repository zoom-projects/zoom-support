package com.hb0730.zoom.sofa.rpc.core.factory;

import com.hb0730.zoom.sofa.rpc.core.service.BaseRpcService;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
public class SofaRpcClientProxy<T> extends BaseRpcService<T> implements MethodInterceptor {
    private final String appName;
    private final Class<T> rpcApi;

    @SuppressWarnings("unchecked")
    public T proxy() {
        Enhancer var1 = new Enhancer();
        var1.setSuperclass(this.rpcApi);
        var1.setCallback(this);
        return (T) var1.create();
    }

    public SofaRpcClientProxy(String appName, Class<T> rpcApi) {
        this.appName = appName;
        this.rpcApi = rpcApi;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return method.invoke(getRpcService(), args);
    }

    @Override
    protected String getAppName() {
        return this.appName;
    }

    @Override
    public Class<T> getRpcInterfaceClazz() {
        return this.rpcApi;
    }

    @Override
    public T getRpcService() {
        return getRpcService(this.appName);
    }
}
