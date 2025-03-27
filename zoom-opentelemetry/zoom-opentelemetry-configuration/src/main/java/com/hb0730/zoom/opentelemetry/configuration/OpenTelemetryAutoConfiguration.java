package com.hb0730.zoom.opentelemetry.configuration;

import com.hb0730.zoom.opentelemetry.sofa.rpc.SofaRpcTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/19
 */
@AutoConfiguration
@AutoConfigureAfter({
        io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration.class
})
public class OpenTelemetryAutoConfiguration {

    private static final String ACTUATOR_TASK_EXECUTOR_BEAN_NAME = "actuatorTaskExecutor";
    private static final String ACTUATOR_TASK_SCHEDULER_BEAN_NAME = "actuatorTaskScheduler";


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "zoom.opentelemetry.sofa.rpc.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(OpenTelemetry.class)
    public SofaRpcTelemetry sofaRpcTelemetry(OpenTelemetry openTelemetry) {
        return SofaRpcTelemetry.create(openTelemetry);
    }

    /**
     * actuator task executor
     *
     * @param taskExecutor task executor
     * @return executor
     */
    @Bean(name = ACTUATOR_TASK_EXECUTOR_BEAN_NAME)
    public Executor actuatorTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        return Context.taskWrapping(taskExecutor);
    }

    /**
     * actuator task scheduler
     *
     * @param taskExecutor task executor
     * @return executor
     */
    @Bean(name = ACTUATOR_TASK_SCHEDULER_BEAN_NAME)
    public Executor actuatorTaskScheduler(ThreadPoolTaskScheduler taskExecutor) {
        return Context.taskWrapping(taskExecutor);
    }


}
