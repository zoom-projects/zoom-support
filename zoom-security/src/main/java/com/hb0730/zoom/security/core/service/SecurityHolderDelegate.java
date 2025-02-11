package com.hb0730.zoom.security.core.service;

import com.hb0730.zoom.base.meta.ICurrentUserService;
import com.hb0730.zoom.base.meta.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/2/11
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityHolderDelegate implements SecurityHolder {
    private final ICurrentUserService currentUserService;

    @Override
    public Optional<UserInfo> getLoginUser() {
        return Optional.ofNullable(currentUserService.getCurrentUser());
    }

    @Override
    public Optional<String> getLoginUserId() {
        return Optional.ofNullable(currentUserService.getCurrentUser().getId());
    }
}
