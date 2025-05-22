package com.hb0730.zoom.sofa.rpc.core.filter;

import com.alipay.sofa.rpc.context.RpcInternalContext;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.hb0730.zoom.base.meta.TraceHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * TraceFilter
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/5/22
 */
@com.alipay.sofa.rpc.ext.Extension("traceFilter")
@com.alipay.sofa.rpc.filter.AutoActive(providerSide = true, consumerSide = true)
@Slf4j
public class TraceFilter extends Filter {
    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        // 1. 获取当前上下文
        // 2. 获取当前请求的 traceId
        // 3. 如果 traceId 不为空，则设置到当前上下文中
        // 4. 调用下一个过滤器
        // 5. 返回响应
        RpcInternalContext context = RpcInternalContext.getContext();
        if (context.isProviderSide()) {
            Object requestProp = request.getRequestProp("traceId");
            if (requestProp != null) {
                TraceHolder.setTraceId((String) requestProp);
            }
        } else {
            String traceId = TraceHolder.getTraceId();
            request.addRequestProp("traceId", traceId);

        }
        return invoker.invoke(request);
    }
}
