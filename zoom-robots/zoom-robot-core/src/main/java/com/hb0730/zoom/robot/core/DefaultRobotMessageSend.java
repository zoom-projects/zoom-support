package com.hb0730.zoom.robot.core;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/11
 */
public abstract class DefaultRobotMessageSend implements RobotMessageSend {

    @Override
    public RobotResponse send(Message message) {
        return send(JsonUtil.toJson(message.toMessage()));
    }

    @Override
    public RobotResponse send(String json) throws RobotException {
        String _json = Assert.isNotBlank(json, "消息不能为空");
        return doSend(_json);
    }

    /**
     * 发送消息
     *
     * @param json 消息
     * @return 发送结果
     * @throws RobotException 发送异常
     */
    protected abstract RobotResponse doSend(String json) throws RobotException;
}
