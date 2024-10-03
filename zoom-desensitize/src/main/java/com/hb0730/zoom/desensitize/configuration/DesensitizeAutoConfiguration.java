package com.hb0730.zoom.desensitize.configuration;

import com.hb0730.zoom.desensitize.core.serializer.DesensitizeJsonSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/3
 */
@AutoConfiguration
public class DesensitizeAutoConfiguration {

    /**
     * 用于 HTTP 响应序列化
     *
     * @return jackson 序列化脱敏序列化
     */
    @Bean
    @ConditionalOnBean(MappingJackson2HttpMessageConverter.class)
    public DesensitizeJsonSerializer desensitizeJsonSerializer() {
        return new DesensitizeJsonSerializer();
    }
}
