package com.hb0730.zoom.base.api;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * API描述
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/12/10
 */
@Builder
@Data
public class ApiDescription implements Serializable {
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 模块
     */
    private String module;

    /**
     * 风险等级
     *
     * @see com.hb0730.zoom.base.enums.OperatorRiskLevelEnums
     */
    private String riskLevel;

}
