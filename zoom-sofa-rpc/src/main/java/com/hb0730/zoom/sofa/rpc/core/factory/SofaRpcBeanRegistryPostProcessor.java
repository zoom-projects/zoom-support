package com.hb0730.zoom.sofa.rpc.core.factory;

import com.hb0730.zoom.base.utils.StrUtil;
import com.hb0730.zoom.sofa.rpc.core.RpcApi;
import com.hb0730.zoom.sofa.rpc.core.annotation.RpcAppName;
import com.hb0730.zoom.sofa.rpc.core.annotation.RpcPkg;
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
 * RPC服务初始化 将所有的RPC CLIENT注册到Spring容器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Slf4j
public class SofaRpcBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private String rpcApiPath = "classpath*:com/hb0730/**/remote/**/*RpcService.class";

    public SofaRpcBeanRegistryPostProcessor() {
    }

    public SofaRpcBeanRegistryPostProcessor(String rpcApiPath) {
        this.rpcApiPath = rpcApiPath;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("~~动态注册RPC动态代理客户端~~");
        Map<String, List<Class<?>>> rpcApi = getRpcApi();
        if (rpcApi.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, List<Class<?>>>> entries = rpcApi.entrySet();
        for (Map.Entry<String, List<Class<?>>> entry : entries) {
            String appName = entry.getKey();
            List<Class<?>> rpcApis = entry.getValue();
            for (Class<?> rpcApiClass : rpcApis) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition();
                beanDefinition.setBeanClass(rpcApiClass);
                SofaRpcClientProxy<?> proxy = new SofaRpcClientProxy<>(appName, rpcApiClass);
                beanDefinition.setInstanceSupplier(proxy::proxy);
                String beanName = String.format("%s#%s", appName, rpcApiClass.getName());
                registry.registerBeanDefinition(beanName, beanDefinition);
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

    private Map<String, List<Class<?>>> getRpcApi() {
        List<String> ignoreItems = getIgnoreItems();
        Map<String, List<Class<?>>> rpcApi = new HashMap<>();
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources(rpcApiPath);
            for (Resource resource : resources) {
                MetadataReader metadataReader = new SimpleMetadataReaderFactory(resourceLoader).getMetadataReader(resource);
                Class<?> clazz = ClassUtils.forName(metadataReader.getClassMetadata().getClassName(),
                        SofaRpcBeanRegistryPostProcessor.class.getClassLoader());
                String appName = null;
                if (RpcApi.class.isAssignableFrom(clazz)) {
                    appName = getRpcPkgValue(clazz);
                }
                if (StrUtil.isBlank(appName)) {
                    RpcAppName annotation = AnnotationUtils.getAnnotation(clazz, RpcAppName.class);
                    if (null != annotation) {
                        appName = annotation.value();
                    }
                }

                if (StrUtil.isBlank(appName)) {
                    log.warn("跳过注册RpcApi: {} ", clazz.getName());
                    continue;
                }
                if (ignoreItems.contains(appName)) {
                    log.warn("跳过注册RpcApi: {}#{} 在忽略列表中", appName, clazz.getName());
                    continue;
                }
                if (!rpcApi.containsKey(appName)) {
                    rpcApi.put(appName, new ArrayList<>());
                }
                log.info("注册成功RpcApi: {}#{} ", appName, clazz.getName());
                rpcApi.get(appName).add(clazz);
            }

        } catch (Exception e) {
            log.error("~~动态注册RPC动态代理客户端失败~~", e);
        }

        return rpcApi;
    }

    private static String getRpcPkgValue(Class<?> clazz) {
        RpcPkg annotation = clazz.getPackage().getAnnotation(RpcPkg.class);
        if (null != annotation) {
            return annotation.value();
        }
        return null;
    }
}
