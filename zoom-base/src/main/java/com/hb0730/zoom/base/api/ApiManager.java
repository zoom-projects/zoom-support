package com.hb0730.zoom.base.api;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/24
 */
public interface ApiManager {
    /**
     * 获取api
     *
     * @param apiName api名称
     * @return api
     */
    Api getApi(String apiName);

    /**
     * 验证token是否有效
     *
     * @param token   token
     * @param apiName 接口名称
     * @return 结果
     */
    boolean checkToken(String token, String apiName);
    
}
