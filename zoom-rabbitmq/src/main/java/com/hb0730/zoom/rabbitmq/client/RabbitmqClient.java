package com.hb0730.zoom.rabbitmq.client;

import cn.hutool.core.util.ObjectUtil;
import com.hb0730.zoom.rabbitmq.annotaion.RabbitComponent;
import com.hb0730.zoom.rabbitmq.exchange.DelayExchangeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2025/4/26
 */
@Slf4j
@Configuration
public class RabbitmqClient {
    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationContext applicationContext;

    public RabbitmqClient(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate,
                          ApplicationContext applicationContext) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.applicationContext = applicationContext;
    }

    @Bean
    public void initQueue() {
        Map<String, Object> beansWithRqbbitComponentMap = this.applicationContext.getBeansWithAnnotation(RabbitComponent.class);
        Class<? extends Object> clazz = null;
        for (Map.Entry<String, Object> entry : beansWithRqbbitComponentMap.entrySet()) {
            //获取到实例对象的class信息
            clazz = entry.getValue().getClass();
            Method[] methods = clazz.getMethods();
            RabbitListener rabbitListener = clazz.getAnnotation(RabbitListener.class);
            if (null != rabbitListener) {
                log.info("[zoom-rabbitmq] 初始化队列...class.........rabbitListener={}", rabbitListener);
                createQueue(rabbitListener);
            }
            for (Method method : methods) {
                RabbitListener methodRabbitListener = method.getAnnotation(RabbitListener.class);
                if (null != methodRabbitListener) {
                    log.info("[zoom-rabbitmq] 初始化队列...method.........rabbitListener={}", methodRabbitListener);
                    createQueue(methodRabbitListener);
                }
            }

        }
    }

    /**
     * 初始化队列
     *
     * @param rabbitListener .
     */
    private void createQueue(RabbitListener rabbitListener) {
        String[] queues = rabbitListener.queues();
        DirectExchange directExchange = createExchange(DelayExchangeBuilder.DELAY_EXCHANGE);
        //创建交换机
        rabbitAdmin.declareExchange(directExchange);
        if (ObjectUtil.isNotEmpty(queues)) {
            for (String queueName : queues) {
                Properties result = rabbitAdmin.getQueueProperties(queueName);
                if (ObjectUtil.isEmpty(result)) {
                    Queue queue = new Queue(queueName);
                    addQueue(queue);
                    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
                    rabbitAdmin.declareBinding(binding);
                    log.info("[zoom-rabbitmq] 创建队列...queueName={}", queueName);
                } else {
                    log.info("[zoom-rabbitmq] 队列已存在...queueName={}", queueName);
                }
            }
        }
    }

    /**
     * 创建自定义队列
     *
     * @param queueName 自定义队列名称
     */
    public boolean createQueue(String queueName) {
        DirectExchange directExchange = createExchange(DelayExchangeBuilder.DELAY_EXCHANGE);
        //创建交换机
        rabbitAdmin.declareExchange(directExchange);
        Properties result = rabbitAdmin.getQueueProperties(queueName);
        if (ObjectUtil.isEmpty(result)) {
            Queue queue = new Queue(queueName);
            addQueue(queue);
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
            rabbitAdmin.declareBinding(binding);
            log.info("[zoom-rabbitmq] 创建队列...queueName={}", queueName);
            return true;
        } else {
            log.info("[zoom-rabbitmq] 队列已存在...queueName={}", queueName);
            return false;
        }
    }

    /**
     * 获取消息
     *
     * @param messageType 消息类型
     * @param msg         消息
     * @return {@link  Message}
     */
    public Message getMessage(String messageType, Object msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(messageType);
        return new Message(msg.toString().getBytes(), messageProperties);
    }

    /**
     * 发送消息到交换机
     *
     * @param topicExchange 交换器
     * @param routingKey    路由键
     * @param msg           消息
     */
    public void sendMessageToExchange(TopicExchange topicExchange, String routingKey, Object msg) {
        Message message = getMessage(MessageProperties.CONTENT_TYPE_JSON, msg);
        rabbitTemplate.send(topicExchange.getName(), routingKey, message);
    }

    /**
     * 没有绑定KEY的Exchange发送
     *
     * @param topicExchange 主题
     * @param exchange      交换器
     * @param msg           消息
     */
    public void sendMessageToExchange(TopicExchange topicExchange, AbstractExchange exchange, String msg) {
        addExchange(exchange);
        log.info("[zoom-rabbitmq] 发送消息到交换机...exchangeName={}, msg={}", topicExchange.getName(), msg);
        rabbitTemplate.convertAndSend(topicExchange.getName(), msg);
    }

    /**
     * 发送消息
     *
     * @param queueName 队列名称
     * @param params    消息内容map
     */
    public void sendMessage(String queueName, Object params) {
        log.debug("[[zoom-rabbitmq] 发送消息到交换机...queueName={}, params={}", queueName, params);
        try {
            rabbitTemplate.convertAndSend(DelayExchangeBuilder.DELAY_EXCHANGE, queueName, params, message -> {
                return message;
            });
        } catch (Exception e) {
            log.error("[zoom-rabbitmq] 发送消息到mq失败", e);
        }
    }


    private final Map sentObj = new HashMap<>();

    /**
     * 发送消息
     *
     * @param queueName 队列名称
     */
    public void sendMessage(String queueName) {
        this.send(queueName, this.sentObj, 0);
        this.sentObj.clear();
    }

    /**
     * 批量发送消息,完毕后可以调用 {@link #sendMessage(String)} 一次性发送
     *
     * @param key   key
     * @param value message
     * @return this
     * @see #sendMessage(String)
     */
    public RabbitmqClient put(String key, Object value) {
        this.sentObj.put(key, value);
        return this;
    }

    /**
     * 延迟发送消息
     *
     * @param queueName  队列名称
     * @param params     消息内容params
     * @param expiration 延迟时间 单位毫秒
     */
    public void sendMessage(String queueName, Object params, Integer expiration) {
        this.send(queueName, params, expiration);
    }

    private void send(String queueName, Object params, Integer expiration) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        CustomExchange customExchange = DelayExchangeBuilder.buildExchange();
        rabbitAdmin.declareExchange(customExchange);
        Binding binding = BindingBuilder.bind(queue).to(customExchange).with(queueName).noargs();
        rabbitAdmin.declareBinding(binding);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("[zoom-rabbitmq] 发送消息到交换机...queueName={}，发送时间: {}", queueName, sf.format(new Date()));
        rabbitTemplate.convertAndSend(DelayExchangeBuilder.DEFAULT_DELAY_EXCHANGE, queueName, params, message -> {
            if (expiration != null && expiration > 0) {
                message.getMessageProperties().setHeader("x-delay", expiration);
            }
            return message;
        });
    }


    /**
     * 从队列接收消息
     *
     * @param queueName 队列名称
     * @return {@link String}
     */
    public String receiveFromQueue(String queueName) {
        return receiveFromQueue(DirectExchange.DEFAULT, queueName);
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param directExchange 交换机
     * @param queueName      队列名称
     * @return {@link String}
     */
    public String receiveFromQueue(DirectExchange directExchange, String queueName) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        rabbitAdmin.declareBinding(binding);
        String messages = (String) rabbitTemplate.receiveAndConvert(queueName);
        log.info("[zoom-rabbitmq] 从队列接收消息...queueName={}, messages={}", queueName, messages);
        return messages;
    }

    /**
     * 添加一个交换器
     *
     * @param exchange .
     */
    public void addExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 删除一个Exchange
     *
     * @param exchangeName 交换器名称
     */
    public boolean deleteExchange(String exchangeName) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }


    /**
     * 声明其名称自动命名的队列。它是用exclusive=true、autoDelete=true和 durable = false
     *
     * @return Queue
     */
    public Queue addQueue() {
        return rabbitAdmin.declareQueue();
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue 队列
     * @return queueName
     */
    public String addQueue(Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * 删除一个队列
     *
     * @param queueName the name of the queue.
     * @param unused    true if the queue should be deleted only if not in use.
     * @param empty     true if the queue should be deleted only if empty.
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    /**
     * 删除一个队列
     *
     * @param queueName 队列名称
     * @return true if the queue existed and was deleted.
     */
    public boolean deleteQueue(String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue      队列
     * @param exchange   主题交换器
     * @param routingKey key
     */
    public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
     *
     * @param exchange      交换器
     * @param topicExchange 主题
     * @param routingKey    key
     */
    public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
        Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 去掉一个binding
     *
     * @param binding {@link Binding}
     */
    public void removeBinding(Binding binding) {
        rabbitAdmin.removeBinding(binding);
    }

    /**
     * 创建交换器
     *
     * @param exchangeName 交换器名称
     * @return {@link DirectExchange}
     */
    public DirectExchange createExchange(String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }
}
