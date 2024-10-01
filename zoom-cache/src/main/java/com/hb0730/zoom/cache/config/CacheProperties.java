package com.hb0730.zoom.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/23
 */
@ConfigurationProperties(prefix = "zoom.cache")
@Getter
@Setter
public class CacheProperties {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 缓存前缀
     */
    private String prefix = "zoom";
}
