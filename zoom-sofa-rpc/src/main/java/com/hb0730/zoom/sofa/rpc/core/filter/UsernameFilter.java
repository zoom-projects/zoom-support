package com.hb0730.zoom.sofa.rpc.core.filter;

import com.alipay.sofa.rpc.context.RpcInternalContext;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;
import com.hb0730.zoom.base.meta.UserContext;
import com.hb0730.zoom.sofa.rpc.core.constant.UserConstant;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/22
 */
@Extension("usernameFilter")
@AutoActive(providerSide = true, consumerSide = true)
public class UsernameFilter extends Filter {
    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        RpcInternalContext context = RpcInternalContext.getContext();
        if (context.isProviderSide()) {
            Object requestProp = request.getRequestProp(UserConstant.INVOKE_CTX_USERNAME);
            if (requestProp != null) {
                UserContext.setCurrentUserName((String) requestProp);
            }
        } else {
            String currentUserName = UserContext.getCurrentUserName();
            request.addRequestProp(UserConstant.INVOKE_CTX_USERNAME, currentUserName);
        }
        return invoker.invoke(request);
    }
}
