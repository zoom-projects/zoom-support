package com.hb0730.zoom.opentelemetry.configuration;

import com.hb0730.zoom.opentelemetry.sofa.rpc.SofaRpcTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/19
 */
@Configuration
@AutoConfigureAfter({io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration.class})
@ConditionalOnBean(OpenTelemetry.class)
public class OpenTelemetryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "zoom.opentelemetry.sofa.rpc.enabled", havingValue = "true", matchIfMissing = true)
    public SofaRpcTelemetry sofaRpcTelemetry() {
        return SofaRpcTelemetry.create(io.opentelemetry.api.GlobalOpenTelemetry.get());
    }
}
