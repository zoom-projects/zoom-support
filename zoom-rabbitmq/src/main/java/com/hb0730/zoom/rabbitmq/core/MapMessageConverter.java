package com.hb0730.zoom.rabbitmq.core;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Map 消息转换器
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/26
 */
public class MapMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType = message.getMessageProperties().getContentType();
        if (null != contentType && contentType.contains("text")) {
            return new String(message.getBody());
        } else {
            ObjectInputStream objInt = null;
            try {
                ByteArrayInputStream byteInt = new ByteArrayInputStream(message.getBody());
                objInt = new ObjectInputStream(byteInt);
                //byte[]转map
                Map map = (HashMap) objInt.readObject();
                return map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
