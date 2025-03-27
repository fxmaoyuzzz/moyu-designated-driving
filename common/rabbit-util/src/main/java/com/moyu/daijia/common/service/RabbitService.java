package com.moyu.daijia.common.service;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MQ消息公共类
 */
@Slf4j
@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param exchange
     * @param routingkey
     * @param message
     * @return
     */
    public boolean sendMessage(String exchange,
                               String routingkey,
                               Object message) {
        log.info("调用RabbitMQ发送消息，exchange：{}， routingkey：{}， message：{}", exchange, routingkey, JSON.toJSONString(message));
        rabbitTemplate.convertAndSend(exchange, routingkey, message);

        return true;
    }

    /**
     * 发送延时消息
     * @param exchange
     * @param routerKey
     * @param message
     * @param delayTime 延时时间 单位：秒
     */
    public void sendDelayMessage(String exchange, String routerKey, String message, int delayTime) {
        rabbitTemplate.convertAndSend(
                exchange,
                routerKey,
                message,
                msg -> {
                    // 设置此消息延时时间
                    msg.getMessageProperties()
                            .setHeader("x-delay", delayTime * 1000);
                    return msg;
                });
    }
}
