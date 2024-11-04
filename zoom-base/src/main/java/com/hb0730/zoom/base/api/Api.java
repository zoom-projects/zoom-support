package com.hb0730.zoom.base.api;

import com.hb0730.zoom.base.R;

import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/24
 */
public interface Api {

    /**
     * 执行
     *
     * @param token  token
     * @param params 参数
     * @return 结果
     */
    R<?> execute(String token, Map<String, Object> params);

    /**
     * 是否跳过权限验证
     *
     * @return 是否跳过
     */
    boolean isSkipAuth();
}
