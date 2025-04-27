package com.hb0730.zoom.rabbitmq.exchange;

import org.springframework.amqp.core.CustomExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟交换机构建器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/26
 */
public class DelayExchangeBuilder {

    /**
     * 默认延迟消息交换器
     */
    public final static String DEFAULT_DELAY_EXCHANGE = "zoom.delayed.exchange";
    /**
     * 普通交换器
     */
    public final static String DELAY_EXCHANGE = "zoom.direct.exchange";


    /**
     * 构建延迟消息交换器
     *
     * @return {@link CustomExchange}
     */
    public static CustomExchange buildExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DEFAULT_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }
}
