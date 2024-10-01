package com.hb0730.zoom.base.security;

import com.hb0730.zoom.base.ZoomConst;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * 安全工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
public class SecurityUtils {

    /**
     * 获取 token
     *
     * @param request request
     * @return token
     */
    public static Optional<String> obtainAuthorization(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ZoomConst.X_ACCESS_TOKEN)).map(String::trim);
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication());
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static Optional<UserInfo> getLoginUser() {
        return getAuthentication().map(authentication -> {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserInfo) {
                return (UserInfo) principal;
            }
            return null;
        });

    }


    /**
     * 获取当前 userId
     *
     * @return id
     */
    public static Optional<String> getLoginUserId() {
        return getLoginUser().map(UserInfo::getId);
    }

    /**
     * 获取当前 username
     *
     * @return username
     */
    public static Optional<String> getLoginUsername() {
        return getLoginUser().map(UserInfo::getUsername);
    }

    /**
     * 清空用户上下文
     */
    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }


    /**
     * 默认密码加密
     *
     * @return .
     */
    public static PasswordEncoder defaultPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
