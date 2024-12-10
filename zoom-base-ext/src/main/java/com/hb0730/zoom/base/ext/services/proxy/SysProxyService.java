package com.hb0730.zoom.base.ext.services.proxy;

import com.hb0730.zoom.base.R;
import com.hb0730.zoom.base.ext.services.dto.SaveMessageDTO;
import com.hb0730.zoom.base.ext.services.dto.SaveOperatorLogDTO;
import com.hb0730.zoom.base.ext.services.dto.UserDTO;
import com.hb0730.zoom.base.ext.services.dto.UserInfoDTO;
import com.hb0730.zoom.base.ext.services.remote.SysDictRpcService;
import com.hb0730.zoom.base.ext.services.remote.SysNotifyRpcService;
import com.hb0730.zoom.base.ext.services.remote.SysOperatorLogRpcService;
import com.hb0730.zoom.base.ext.services.remote.SysUserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统消息发送RPC代理
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/17
 */
@Service
public class SysProxyService implements SysUserRpcService, SysNotifyRpcService, SysOperatorLogRpcService, SysDictRpcService {

    @Autowired
    private SysNotifyRpcService notifyRpcService;
    @Autowired
    private SysUserRpcService userRpcService;
    @Autowired
    private SysOperatorLogRpcService operatorLogRpcService;

    @Autowired
    private SysDictRpcService dictRpcService;


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

    @Override
    public boolean checkOpenApiAuth(String token, String apiName) {
        return userRpcService.checkOpenApiAuth(token, apiName);
    }

    @Override
    public UserDTO findUserByToken(String token) {
        return userRpcService.findUserByToken(token);
    }

    @Override
    public void saveOperatorLog(SaveOperatorLogDTO dto) {
        operatorLogRpcService.saveOperatorLog(dto);
    }


    @Override
    public List<Object> getDictItems(String dictCode) {
        return dictRpcService.getDictItems(dictCode);
    }
}
