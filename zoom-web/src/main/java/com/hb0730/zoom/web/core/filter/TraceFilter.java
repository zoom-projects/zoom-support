package com.hb0730.zoom.web.core.filter;

import com.hb0730.zoom.base.meta.TraceHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * traceId 过滤器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/11
 */
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取 traceId
            String traceId = TraceHolder.createTraceId();
            // 设置 traceId 上下文
            TraceHolder.set(traceId);
            // 设置响应头
            response.setHeader(TraceHolder.TRACE_ID_HEADER, traceId);
            // 执行请求
            filterChain.doFilter(request, response);
        } finally {
            // 清空 traceId 上下文
            TraceHolder.remove();
        }
    }

}
