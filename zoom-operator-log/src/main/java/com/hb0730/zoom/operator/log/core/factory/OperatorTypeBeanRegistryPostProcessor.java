package com.hb0730.zoom.operator.log.core.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/22
 */
@Slf4j
public class OperatorTypeBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final String DEFAULT_SCAN_PACKAGE = "com.hb0730.zoom";
    private final List<String> scanPackages;

    public OperatorTypeBeanRegistryPostProcessor() {
        scanPackages = List.of(DEFAULT_SCAN_PACKAGE);
    }

    public OperatorTypeBeanRegistryPostProcessor(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 将OperatorTypeDefinition所有的实现类注册到spring容器中
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(OperatorTypeDefinition.class));

        // 添加扫描路径
        for (String scanPackage : scanPackages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(scanPackage)) {
                GenericBeanDefinition gbd = (GenericBeanDefinition) bd;
                gbd.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                registry.registerBeanDefinition(Objects.requireNonNull(gbd.getBeanClassName()), gbd);
            }
        }


    }
}
