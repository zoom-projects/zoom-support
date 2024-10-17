package com.hb0730.zoom.sofa.rpc.core.factory;

import com.hb0730.zoom.sofa.rpc.core.RpcApi;
import com.hb0730.zoom.sofa.rpc.core.annotation.RpcAppName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Slf4j
public class SofaRpcBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final String RPC_API_PATH = "classpath*:com/hb0730/**/remote/**/*RpcService.class";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("~~动态注册RPC动态代理客户端~~");
        Map<String, List<Class<? extends RpcApi>>> rpcApi = getRpcApi();
        if (null == rpcApi || rpcApi.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, List<Class<? extends RpcApi>>>> entries = rpcApi.entrySet();
        for (Map.Entry<String, List<Class<? extends RpcApi>>> entry : entries) {
            String appName = entry.getKey();
            List<Class<? extends RpcApi>> rpcApis = entry.getValue();
            for (Class<? extends RpcApi> rpcApiClass : rpcApis) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition();
                beanDefinition.setBeanClass(rpcApiClass);
                SofaRpcClientProxy<?> proxy = new SofaRpcClientProxy<>(appName, rpcApiClass);
                beanDefinition.setInstanceSupplier(proxy::proxy);
                registry.registerBeanDefinition(rpcApiClass.getName(), beanDefinition);
            }
        }
    }

    /**
     * 获取忽略的类
     *
     * @return 忽略的类
     */
    private List<String> getIgnoreItems() {
        List<String> items = new ArrayList<>();
        ServiceLoader.load(CustomRpcProxyConfig.class).forEach(customRpcProxyConfig -> {
            items.addAll(customRpcProxyConfig.getIgnoreItems());
        });
        return items;
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<Class<? extends RpcApi>>> getRpcApi() {
        List<String> ignoreItems = getIgnoreItems();
        Map<String, List<Class<? extends RpcApi>>> rpcApi = new HashMap<>();
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(RPC_API_PATH);
            for (Resource resource : resources) {
                MetadataReader metadataReader = new SimpleMetadataReaderFactory(resourceLoader).getMetadataReader(resource);
                Class<?> aClass = ClassUtils.forName(metadataReader.getClassMetadata().getClassName(),
                        SofaRpcBeanRegistryPostProcessor.class.getClassLoader());
                String appName = null;
                RpcAppName annotation = AnnotationUtils.getAnnotation(aClass, RpcAppName.class);
                if (null == annotation) {
                    log.warn("跳过注册RpcApi:{}没有RpcAppName注解", aClass.getName());
                    continue;
                }
                appName = annotation.value();
                if (null == appName || appName.isEmpty()) {
                    log.warn("跳过注册RpcApi:{} RpcAppName注解为空", aClass.getName());
                    continue;
                }
                if (ignoreItems.contains(aClass.getName())) {
                    log.warn("跳过注册RpcApi:{}#{}在忽略列表中", appName, aClass.getName());
                    continue;
                }
                if (!rpcApi.containsKey(appName)) {
                    rpcApi.put(appName, new ArrayList<>());
                }
                log.info("注册成功RpcApi:{}#{}", appName, aClass.getName());
                rpcApi.get(appName).add((Class<? extends RpcApi>) aClass);
            }

        } catch (Exception e) {
            log.error("~~动态注册RPC动态代理客户端失败~~", e);
        }

        return null;
    }

}
