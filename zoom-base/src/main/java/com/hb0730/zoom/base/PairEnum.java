package com.hb0730.zoom.base;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/9/25
 */
public interface PairEnum<K, T extends Pair<K, ?>> {
    /**
     * 代码名称对
     *
     * @return 代码名称对
     */
    T getValue();

    /**
     * 代码
     *
     * @return 代码
     */
    K getCode();

    /**
     * 名称
     *
     * @return 名称
     */
    String getMessage();
}
