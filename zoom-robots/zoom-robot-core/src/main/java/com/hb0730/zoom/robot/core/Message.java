package com.hb0730.zoom.robot.core;

import java.util.Map;

/**
 * 消息接口
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public interface Message {

    /**
     * 将消息转换为Map
     *
     * @return 消息的Map表示
     */
    Map<String, Object> toMessage();
}
