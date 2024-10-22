package com.hb0730.zoom.opentelemetry.sofa.rpc;

import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.hb0730.zoom.base.AppUtil;
import io.opentelemetry.api.OpenTelemetry;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/22
 */
@Extension("opentelemetryFilter")
@AutoActive(consumerSide = true, providerSide = true)
public class OpenTelemetryFilter extends Filter {
    private final Filter delegate;

    public OpenTelemetryFilter() {
        delegate = SofaRpcTelemetry.create(AppUtil.getBean(OpenTelemetry.class)).newFilter();

    }

    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        return delegate.invoke(invoker, request);
    }
}
