package com.hb0730.zoom.base.meta;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/2/11
 */
public interface ICurrentUserService {

    /**
     * 获取当前用户
     *
     * @return 用户信息
     */
    UserInfo getCurrentUser();
}
