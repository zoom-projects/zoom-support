package com.hb0730.zoom.spring.retry.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * spring retry 配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/3/19
 */
@Configuration
@EnableRetry
@Slf4j
public class SpringRetryAutoConfiguration {

    public SpringRetryAutoConfiguration() {
        log.info("[zoom-spring-retry] spring retry auto configuration");
    }
}
