package com.hb0730.zoom.sofa.rpc.core.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.alipay.sofa.rpc.boot.runtime.param.BoltBindingParam;
import com.alipay.sofa.runtime.api.client.ServiceClient;
import com.alipay.sofa.runtime.api.client.param.BindingParam;
import com.alipay.sofa.runtime.api.client.param.ServiceParam;
import com.hb0730.zoom.base.AppUtil;
import com.hb0730.zoom.base.utils.CollectionUtil;
import com.hb0730.zoom.sofa.rpc.core.annotation.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Slf4j
public class SofaRpcServiceInitializingBean implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("~~初始化RPC SERVICE~~");
        SofaRpcClientFactoryBean sofaRpcClientFactoryBean = SpringUtil.getBean(SofaRpcClientFactoryBean.class);
        ServiceClient serviceClient = sofaRpcClientFactoryBean.getClientFactory().getClient(ServiceClient.class);

        Map<String, Object> beans = AppUtil.getBeansWithAnnotation(RemoteService.class, Object.class);
        if (CollectionUtil.isEmpty(beans)) {
            log.warn("~~没有RPC SERVICE~~");
            return;
        }
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object rpcApi = entry.getValue();
            
            log.info("~~初始化RPC SERVICE~~: {} ", rpcApi.getClass().getName());
            ServiceParam serviceParam = new ServiceParam();
            // 服务接口
            Class<?>[] interfaces = rpcApi.getClass().getInterfaces();
            if (interfaces.length == 0) {
                log.warn("~~ {} 没有实现接口~~", rpcApi.getClass().getName());
                continue;
            }
            serviceParam.setInterfaceType(interfaces[0]);
            serviceParam.setInstance(rpcApi);
            List<BindingParam> params = new ArrayList<>();

            BindingParam bindingParam = new BoltBindingParam();
            params.add(bindingParam);
            serviceParam.setBindingParams(params);

            serviceClient.service(serviceParam);
        }
    }
}
