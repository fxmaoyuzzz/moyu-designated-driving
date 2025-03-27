package com.moyu.daijia.payment.receiver;

import com.alibaba.fastjson.JSONObject;
import com.moyu.daijia.common.constant.MqConst;
import com.moyu.daijia.model.form.payment.ProfitsharingForm;
import com.moyu.daijia.payment.service.WxPayService;
import com.moyu.daijia.payment.service.WxProfitsharingService;
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
 * 监听支付完成后消息
 *
 * @author fxmao
 * @date 2024-06-27 15:39
 */
@Slf4j
@Component
public class PaymentReceiver {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WxProfitsharingService wxProfitsharingService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_PAY_SUCCESS, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER),
            key = {MqConst.ROUTING_PAY_SUCCESS}
    ))
    public void paySuccess(String orderNo, Message message, Channel channel) {
        wxPayService.handleOrder(orderNo);
    }



    /**
     * 分账消息
     * @param param
     * @throws IOException
     */
    @RabbitListener(queues = MqConst.QUEUE_PROFITSHARING)
    public void profitsharingMessage(String param, Message message, Channel channel) throws IOException {
        try {
            ProfitsharingForm profitsharingForm = JSONObject.parseObject(param, ProfitsharingForm.class);
            log.info("分账：{}", param);
            wxProfitsharingService.profitsharing(profitsharingForm);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("分账调用失败：{}", e.getMessage());
            //任务执行失败，就退回队列继续执行，优化：设置退回次数，超过次数记录日志
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
