package com.hb0730.zoom.operator.log.core.factory;

import com.hb0730.zoom.operator.log.core.model.OperatorType;

/**
 * 操作类型定义
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/10/9
 */
public interface OperatorTypeDefinition {
    /**
     * 获取操作类型
     *
     * @return 操作类型
     */
    OperatorType[] types();
}
