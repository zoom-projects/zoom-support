package com.hb0730.zoom.rabbitmq.listenter;

import com.rabbitmq.client.Channel;

/**
 * RabbitMQ 消息监听器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/26
 */
public interface RabbitmqListener<T> {

    /**
     * 处理消息
     *
     * @param map     消息体
     * @param channel 通道
     */
    default void onMessage(T map, Channel channel) {
    }


}
