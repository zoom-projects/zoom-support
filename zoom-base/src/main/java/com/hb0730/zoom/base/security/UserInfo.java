package com.hb0730.zoom.base.security;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录用户信息
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */
@Getter
@Setter
public class UserInfo {
    private String id;
    private String username;
    private String nickname;
}
