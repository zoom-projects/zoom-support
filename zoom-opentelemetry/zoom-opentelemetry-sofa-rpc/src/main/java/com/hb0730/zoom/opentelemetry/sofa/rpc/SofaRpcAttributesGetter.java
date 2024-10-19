package com.hb0730.zoom.opentelemetry.sofa.rpc;

import com.alipay.sofa.rpc.core.request.SofaRequest;
import io.opentelemetry.instrumentation.api.incubator.semconv.rpc.RpcAttributesGetter;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/19
 */
enum SofaRpcAttributesGetter implements RpcAttributesGetter<SofaRequest> {
    INSTANCE;

    @Override
    public String getSystem(SofaRequest request) {
        return "sofa-rpc";
    }

    @Override
    public String getService(SofaRequest request) {
        return request.getInterfaceName();
    }

    @Override
    public String getMethod(SofaRequest request) {
        return request.getMethodName();
    }
}
