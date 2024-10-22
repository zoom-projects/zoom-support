package com.hb0730.zoom.base.ext.services.proxy;

import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.ext.services.dto.SaveMessageDTO;
import com.hb0730.zoom.base.ext.services.dto.UserInfoDTO;
import com.hb0730.zoom.base.ext.services.remote.SysNotifyRpcService;
import com.hb0730.zoom.base.ext.services.remote.SysUserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 系统消息发送RPC代理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Service
public class SysProxyService implements SysUserRpcService, SysNotifyRpcService {

    @Autowired
    private SysNotifyRpcService notifyRpcService;
    @Autowired
    private SysUserRpcService userRpcService;


    @Override
    public R<?> doSendMessages(Map<String, String> params) {
        return notifyRpcService.doSendMessages(params);
    }

    @Override
    public R<?> saveMessage(SaveMessageDTO dto) {
        return notifyRpcService.saveMessage(dto);
    }

    @Override
    public UserInfoDTO findUsername(String username) {
        return userRpcService.findUsername(username);
    }

}
