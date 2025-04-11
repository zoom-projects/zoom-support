package com.hb0730.zoom.robot.core;

/**
 * 消息发送接口
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public interface RobotMessageSend {
    String DEFAULT_CONTENT_TYPE_JSON = "application/json";
    String DEFAULT_CONTENT_TYPE_HEADER = "Content-Type";

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 发送结果
     * @throws RobotException 发送异常
     */
    RobotResponse send(Message message) throws RobotException;

    /**
     * 发送消息
     *
     * @param json 消息
     * @return 发送结果
     * @throws RobotException 发送异常
     */
    RobotResponse send(String json) throws RobotException;
}
