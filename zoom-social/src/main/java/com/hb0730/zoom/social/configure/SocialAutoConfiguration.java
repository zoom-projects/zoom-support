package com.hb0730.zoom.social.configure;

import com.hb0730.zoom.cache.core.CacheUtil;
import com.hb0730.zoom.social.configure.config.SocialConfig;
import com.hb0730.zoom.social.core.AuthRequestFactory;
import com.hb0730.zoom.social.core.cache.AuthRedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/14
 */
@AutoConfiguration
@EnableConfigurationProperties(SocialConfig.class)
public class SocialAutoConfiguration {

    @Bean
    @ConditionalOnBean(CacheUtil.class)
    public AuthStateCache authCache(CacheUtil cache) {
        return new AuthRedisStateCache(cache);
    }

    /**
     * AuthRequest工厂类
     *
     * @param authCache    授权缓存
     * @param socialConfig 社交配置
     * @return {@link AuthRequestFactory}
     */
    @Bean
    @ConditionalOnBean({AuthStateCache.class, SocialConfig.class})
    @ConditionalOnProperty(prefix = "zoom.social", name = "enable", havingValue = "true", matchIfMissing = true)
    public AuthRequestFactory authRequestFactory(AuthStateCache authCache, SocialConfig socialConfig) {
        return new AuthRequestFactory(authCache, socialConfig);
    }
}
