package com.hb0730.zoom.sofa.rpc.core.factory;

import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Component
public class SofaRpcClientFactoryBean implements ClientFactoryAware {
    @Getter
    private ClientFactory clientFactory;

    @Override
    public void setClientFactory(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }
}
