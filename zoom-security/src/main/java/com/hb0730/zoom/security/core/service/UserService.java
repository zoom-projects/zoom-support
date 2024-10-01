package com.hb0730.zoom.security.core.service;

import com.hb0730.zoom.base.security.UserInfo;

/**
 * 获取用户信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
public interface UserService {

    /**
     * 通过 token 获取用户信息
     *
     * @param token token
     * @return user
     */
    UserInfo getUserByToken(String token);
}
