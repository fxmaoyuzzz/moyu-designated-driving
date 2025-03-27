package com.moyu.daijia.order.receiver;

import com.moyu.daijia.common.constant.MqConst;
import com.moyu.daijia.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听订单分账消息
 *
 * @author fxmao
 * @date 2024-06-27 18:32
 */
@Slf4j
@Component
public class OrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 订单分账成功，更新分账状态
     *
     * @param orderNo
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PROFITSHARING_SUCCESS, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER),
            key = {MqConst.ROUTING_PROFITSHARING_SUCCESS}
    ))
    public void profitsharingSuccess(String orderNo, Message message, Channel channel) throws IOException {
        orderInfoService.updateProfitsharingStatus(orderNo);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
