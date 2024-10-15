package com.hb0730.zoom.security.core.filter;

import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.security.SecurityUtils;
import com.hb0730.zoom.base.security.UserInfo;
import com.hb0730.zoom.base.utils.ServletUtil;
import com.hb0730.zoom.security.core.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * 认证过滤器
 * 验证 token 有效后将其加入上下文
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;

    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // 获取请求头 token
            Optional<String> tokenOption = SecurityUtils.obtainAuthorization(request);
            if (tokenOption.isPresent()) {
                // 通过 token 获取用户信息
                UserInfo userInfo = userService.getUserByToken(tokenOption.get());
                if (userInfo != null) {
                    // 设置用户信息
                    setLoginUser(userInfo, request);
                }
            }
        } catch (Exception e) {
            log.error("TokenAuthenticationFilter.doFilterInternal parser error", e);
            R<Object> res = R.error(500, "系统异常");
            ServletUtil.writeJson(response, res);
            return;
        }

        chain.doFilter(request, response);
    }


    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request   请求
     */
    public void setLoginUser(UserInfo loginUser, HttpServletRequest request) {
        // 创建 authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                loginUser.getAuthorities());
        if (request != null) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        // 设置上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
