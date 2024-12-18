package com.hb0730.zoom.social.core;

import com.hb0730.zoom.social.configure.config.SocialConfig;
import com.hb0730.zoom.social.configure.config.SocialConfigProperties;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;

import java.util.Map;
import java.util.Optional;

/**
 * AuthRequest工厂类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/14
 */
@RequiredArgsConstructor
public class SocialAuthRequestFactory {
    private final AuthStateCache authStateCache;
    private final SocialConfig socialConfig;
    private static final Map<String, AuthRequest> AUTH_REQUEST_CACHE = new java.util.concurrent.ConcurrentHashMap<>();


    /**
     * 登录授权
     *
     * @param source 类型
     * @param code   code
     * @param state  state
     * @return AuthResponse
     */
    @SuppressWarnings("unchecked")
    public AuthResponse<AuthUser> loginAuth(String source, String code, String state) {
        Optional<AuthRequest> authRequest = get(source);
        if (authRequest.isEmpty()) {
            return AuthResponse.<AuthUser>builder()
                    .code(-1)
                    .msg("不支持的登录方式")
                    .build();
        }

        AuthCallback callback = new AuthCallback();
        callback.setCode(code);
        callback.setState(state);
        return authRequest.get().login(callback);

    }

    /**
     * 获取AuthRequest
     *
     * @param source 类型
     * @return AuthRequest
     */
    public Optional<AuthRequest> get(String source) {
        // 双重锁
        AuthRequest authRequest = AUTH_REQUEST_CACHE.get(source);
        if (null == authRequest) {
            synchronized (this) {
                authRequest = AUTH_REQUEST_CACHE.get(source);
                if (null == authRequest) {
                    authRequest = getDefaultRequest(source);
                    AUTH_REQUEST_CACHE.put(source, authRequest);
                }
            }
        }
        return Optional.ofNullable(authRequest);
    }

    /**
     * 获取AuthRequest
     *
     * @param source 类型
     * @return AuthRequest
     */
    private AuthRequest getDefaultRequest(String source) {
        SocialConfigProperties properties = socialConfig.getResources().get(source);
        if (null == properties) {
            return null;
        }
        AuthConfig.AuthConfigBuilder builder = AuthConfig.builder()
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .redirectUri(properties.getRedirectUri())
                .scopes(properties.getScopes());

        return switch (source.toUpperCase()) {
            case "GITHUB" -> new AuthGithubRequest(builder.build(), authStateCache);
            case "GITEE" -> new AuthGiteeRequest(builder.build(), authStateCache);
            default -> null;
        };

    }
}
