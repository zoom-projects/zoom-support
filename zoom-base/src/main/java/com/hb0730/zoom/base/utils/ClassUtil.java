package com.hb0730.zoom.base.utils;

/**
 * class 工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/23
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    /**
     * 获取类名
     *
     * @param className className 全类名,如：<code>com.hb0730.base.util.ClassUtil</code>
     * @return 类名, 如：<code>ClassUtil</code>
     */
    public static String getSimpleName(String className) {
        return StrUtil.isBlank(className) ? null : className.substring(className.lastIndexOf(StrUtil.DOT) + 1);
    }
}
