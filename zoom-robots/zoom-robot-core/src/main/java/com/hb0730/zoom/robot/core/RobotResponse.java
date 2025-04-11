package com.hb0730.zoom.robot.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * response
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
@Getter
@Setter
public class RobotResponse implements Serializable {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;
    /**
     * 是否成功
     */
    private boolean success = false;
}
