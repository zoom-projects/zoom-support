package com.hb0730.zoom.web.configure;

import com.hb0730.zoom.base.util.StrUtil;
import com.hb0730.zoom.web.core.handler.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web 配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@AutoConfiguration
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Value("${zoom.api.prefix: }")
    private String apiPath;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        if (StrUtil.isBlank(apiPath)) {
            return;
        }
        AntPathMatcher matcher = new AntPathMatcher(".");
        configurer.addPathPrefix(
                apiPath,
                clazz -> clazz.isAnnotationPresent(RestController.class)
                        && matcher.match("com.hb0730.**.controller.**", clazz.getPackage().getName()));
    }

    /**
     * @return 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 配置跨域
     */
    @Bean
    @ConditionalOnProperty(value = "zoom.api.cors", havingValue = "true", matchIfMissing = true)
    public CorsConfigurationSource corsConfigurationSource() {
        // 跨域配置
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 创建 CorsConfiguration 对象
        return source;
    }
}
