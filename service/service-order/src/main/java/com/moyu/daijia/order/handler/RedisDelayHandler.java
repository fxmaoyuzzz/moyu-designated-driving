package com.moyu.daijia.order.handler;

import com.moyu.daijia.order.service.OrderInfoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 监听订单延时消息
 *
 * @author fxmao
 * @date 2024-06-27 17:43
 */
@Slf4j
@Component
public class RedisDelayHandler {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private OrderInfoService orderInfoService;

    @PostConstruct
    public void listener() {
        new Thread(() -> {
            while (true) {
                // 获取延迟队列里面阻塞队列
                RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("queue_cancel");

                // 从队列获取消息
                try {
                    String orderId = blockingQueue.take();

                    // 取消订单
                    if (StringUtils.hasText(orderId)) {
                        // 调用方法取消订单
                        log.info("接收延时队列成功，订单id：{}", orderId);
                        orderInfoService.orderCancel(Long.parseLong(orderId));
                    }

                } catch (InterruptedException e) {
                    log.error("接收延时队列失败", e);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
