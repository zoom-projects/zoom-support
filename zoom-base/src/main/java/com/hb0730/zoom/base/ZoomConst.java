package com.hb0730.zoom.base;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/25
 */
public interface ZoomConst {
    /**
     * UTF-8编码
     */
    String UTF8 = "UTF-8";

    /**
     * UTF8字符集
     */
    Charset UTF8_CHARSET = StandardCharsets.UTF_8;  //Charset.forName(UTF8);

    /**
     * token请求头名称
     */
    String AUTH_HEADER = "authorization";

    /**
     * basic请求头
     */
    String BASIC_TOKEN = "Basic ";

    /**
     * 访问令牌头
     */
    String X_ACCESS_TOKEN = "x-access-token";


    /**
     * 状态(0无效1有效)
     */
    Integer STATUS_0 = 0;
    /**
     * 状态(0无效1有效)
     */
    Integer STATUS_1 = 1;


    /**
     * 伦理删除标志(0正常1删除)
     */
    Integer DEL_FLAG_1 = 1;
    /**
     * 伦理删除标志(0正常1删除)
     */
    Integer DEL_FLAG_0 = 0;
}
