package com.hb0730.zoom.social.core.cache;

import com.hb0730.zoom.cache.core.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;

/**
 * 授权状态缓存
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/14
 */
@Slf4j
@RequiredArgsConstructor
public class AuthRedisStateCache implements AuthStateCache {
    private final CacheUtil cacheUtil;
    private static final String CACHE_PREFIX = "social_auth_codes:";

    @Override
    public void cache(String key, String value) {
        // 授权超时时间 默认三分钟
        cacheUtil.setString(CACHE_PREFIX + key, value, 180);
    }

    @Override
    public void cache(String key, String value, long timeout) {
        cacheUtil.setString(CACHE_PREFIX + key, value, timeout);
    }

    @Override
    public String get(String key) {
        return cacheUtil.getString(CACHE_PREFIX + key).orElseGet(() -> null);
    }

    @Override
    public boolean containsKey(String key) {
        return cacheUtil.hasKey(CACHE_PREFIX + key);
    }
}
