package com.hb0730.zoom.base.util;

import com.hb0730.zoom.base.R;
import jakarta.servlet.http.HttpServletResponse;

/**
 * servlet工具
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/24
 */

public class ServletUtil extends cn.hutool.extra.servlet.JakartaServletUtil {


    /**
     * 写出json,并设置contentType为application/json;charset=UTF-8
     *
     * @param response 响应
     * @param message  信息
     */
    public static void writeJson(HttpServletResponse response, R<?> message) {
        String content = JsonUtil.DEFAULT.toJson(message);
        write(response, content, "application/json;charset=UTF-8");
    }

}
