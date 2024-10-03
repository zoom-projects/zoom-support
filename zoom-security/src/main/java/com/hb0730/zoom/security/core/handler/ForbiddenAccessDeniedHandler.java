package com.hb0730.zoom.security.core.handler;

import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.security.SecurityUtils;
import com.hb0730.zoom.base.utils.JsonUtil;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 权限不足处理器
 * <p>
 * {@code @PreAuthorize("hasAuthority('xxx') } 返回 false 会进入此处理器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@Slf4j
public class ForbiddenAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("AccessDeniedHandlerImpl-handle-forbidden {} {}", SecurityUtils.getLoginUserId(), request.getRequestURI());
        R<Object> jr = R.NG(403, "无操作权限");
        String res = JsonUtil.DEFAULT.toJson(jr);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(res);
    }
}
