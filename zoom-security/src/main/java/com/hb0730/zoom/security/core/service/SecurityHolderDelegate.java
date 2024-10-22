package com.hb0730.zoom.security.core.service;


import com.hb0730.zoom.base.ext.security.SecurityHolder;
import com.hb0730.zoom.base.ext.security.SecurityUtils;
import com.hb0730.zoom.base.meta.UserInfo;

import java.util.Optional;

/**
 * SecurityHolder 委托类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
public class SecurityHolderDelegate implements SecurityHolder {
    @Override
    public Optional<UserInfo> getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    @Override
    public Optional<String> getLoginUserId() {
        return SecurityUtils.getLoginUserId();
    }
}
