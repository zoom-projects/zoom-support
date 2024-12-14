package com.hb0730.zoom.social.configure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/14
 */
@Data
@ConfigurationProperties(prefix = "zoom.social")
public class SocialConfig {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 社交登陆配置
     */
    private Map<String, SocialConfigProperties> resources;
}
