package com.hb0730.zoom.opentelemetry.sofa.rpc;

import com.alipay.sofa.rpc.context.RpcInternalContext;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/22
 */
final class TracerFilter extends Filter {
    private final Instrumenter<SofaRequest, SofaResponse> serverInstrumenter;
    private final Instrumenter<SofaRequest, SofaResponse> clientInstrumenter;

    TracerFilter(Instrumenter<SofaRequest, SofaResponse> serverInstrumenter,
                 Instrumenter<SofaRequest, SofaResponse> clientInstrumenter) {
        this.serverInstrumenter = serverInstrumenter;
        this.clientInstrumenter = clientInstrumenter;
    }

    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        RpcInternalContext _context = RpcInternalContext.getContext();
        Instrumenter<SofaRequest, SofaResponse> _instrumenter = _context.isProviderSide() ? serverInstrumenter : clientInstrumenter;
        if (null == _instrumenter) {
            return invoker.invoke(request);
        }
        Context parentContext = Context.current();
        if (!_instrumenter.shouldStart(parentContext, request)) {
            return invoker.invoke(request);
        }
        Context context = _instrumenter.start(parentContext, request);
        SofaResponse response;
        try (var ignored = context.makeCurrent()) {
            response = invoker.invoke(request);
        } catch (Throwable t) {
            _instrumenter.end(context, request, null, t);
            throw t;
        }

        Object result = response.getAppResponse();
        if (result instanceof Throwable) {
            _instrumenter.end(context, request, response, (Throwable) result);
        } else {
            _instrumenter.end(context, request, response, null);
        }
        return response;
    }
}
